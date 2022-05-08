package com.example.tpdjebrilbenamarredamessi.network

import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.UserInfo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET


object Api {
    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3MzIsImV4cCI6MTY4MzUwODU3Nn0.mLf8rrlVCLJfyw0jYoAXZ5mZ09psXv_NnjKbilAVYN4"

    private val retrofit by lazy {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->

                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        Retrofit.Builder()
            .baseUrl("https://android-tasks-api.herokuapp.com/api/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    interface UserWebService {
        @GET("users/info")
        suspend fun getInfo(): Response<UserInfo>
    }

    val userWebService : UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val userTaskWebService : TaskWebService by lazy {
        retrofit.create(TaskWebService::class.java)
    }
}