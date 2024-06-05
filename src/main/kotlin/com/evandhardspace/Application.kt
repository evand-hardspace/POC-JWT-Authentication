package com.evandhardspace

import com.evandhardspace.data.user.MongoUserDataSource
import com.evandhardspace.plugins.*
import com.evandhardspace.security.hashing.SHA256HashingService
import com.evandhardspace.security.token.JwtTokenService
import com.evandhardspace.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import kotlin.time.Duration.Companion.days

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused")
fun Application.module() {
    val mongoPassword = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://ivankravchenko217:$mongoPassword@cluster0.swpnb9w.mongodb.net/$dbName?retryWrites=true&w=majority&appName=Cluster0"
    ).coroutine.getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365.days.inWholeMilliseconds,
        secret = System.getenv("JWT_SECRET"),
    )
    val hashService = SHA256HashingService()

    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
    configureRouting(
        userDataSource,
        hashService,
        tokenService,
        tokenConfig,
    )
}
