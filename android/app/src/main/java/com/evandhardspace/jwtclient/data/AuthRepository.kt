package com.evandhardspace.jwtclient.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import retrofit2.HttpException

class AuthRepository(
    context: Context,
    private val api: AuthApi = AuthApi,
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE),
) {
    suspend fun signUp(username: String, password: String): AuthResult<Unit> = try {
        api.signUp(AuthRequest(username, password))
        signIn(username, password)
    } catch (e: HttpException) {
        if (e.code() == 401) AuthResult.Unauthorized()
        else AuthResult.UnknownError(e)
    } catch (e: Throwable) {
        AuthResult.UnknownError(e)
    }

    suspend fun signIn(username: String, password: String): AuthResult<Unit> = try {
        val response = api.signIn(AuthRequest(username, password))
        prefs.edit {
            putString("jwt", response.token)
        }
        AuthResult.Authorized(Unit)
    } catch (e: HttpException) {
        if (e.code() == 401) AuthResult.Unauthorized()
        else AuthResult.UnknownError(e)
    } catch (e: Throwable) {
        AuthResult.UnknownError(e)
    }

    suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", "") ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized(Unit)
        } catch (e: HttpException) {
            if (e.code() == 401) AuthResult.Unauthorized()
            else AuthResult.UnknownError(e)
        } catch (e: Throwable) {
            AuthResult.UnknownError(e)
        }
    }
}