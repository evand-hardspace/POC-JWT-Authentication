package com.evandhardspace.plugins

import com.evandhardspace.authenticate
import com.evandhardspace.data.user.UserDataSource
import com.evandhardspace.secret
import com.evandhardspace.security.hashing.HashingService
import com.evandhardspace.security.token.TokenConfig
import com.evandhardspace.security.token.TokenService
import com.evandhardspace.signIn
import com.evandhardspace.signUp
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        get("hello") {
            call.respond("Hello World!")
        }
        signUp(
            hashingService = hashingService,
            userDataSource = userDataSource,
        )
        signIn(
            userDataSource = userDataSource,
            hashingService = hashingService,
            tokenService = tokenService,
            tokenConfig = tokenConfig,
        )
        authenticate()
        secret()
    }
}
