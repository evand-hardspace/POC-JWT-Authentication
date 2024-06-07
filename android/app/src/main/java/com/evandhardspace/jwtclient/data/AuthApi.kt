package com.evandhardspace.jwtclient.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthApi {

    @POST("signup")
    suspend fun signUp(
        @Body request: AuthRequest,
    )

    @POST("signin")
    suspend fun signIn(
        @Body request: AuthRequest,
    ): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String,
    )

    companion object : AuthApi by authApiRetrofitProxy
}

//show Network information in to the logcat
private val interceptor = HttpLoggingInterceptor().apply {
    this.level = HttpLoggingInterceptor.Level.BODY
}
private val client = OkHttpClient.Builder().apply {
    this.addInterceptor(interceptor)
        // time out setting
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)

}.build()

private val authApiRetrofitProxy: AuthApi
    get() = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
        .create(AuthApi::class.java)