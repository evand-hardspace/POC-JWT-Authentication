package com.evandhardspace

import com.evandhardspace.data.user.MongoUserDataSource
import com.evandhardspace.data.user.User
import com.evandhardspace.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

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

//    GlobalScope.launch {
//        val user = User(
//            userName = "test",
//            password = "1234",
//            salt = "salt"
//        )
//        userDataSource.insertUser(user)
//    }
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
