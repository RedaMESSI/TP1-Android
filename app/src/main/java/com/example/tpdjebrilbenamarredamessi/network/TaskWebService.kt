package com.example.tpdjebrilbenamarredamessi.network

import com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask.Task
import retrofit2.Response
import retrofit2.http.*

interface TaskWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
    @POST("tasks")
    suspend fun create(@Body task: okhttp3.internal.concurrent.Task): Response<Task>
    @PATCH("tasks/{id}")
    suspend fun update(@Body task: okhttp3.internal.concurrent.Task, @Path("id") id: String = task.name): Response<Task>
    @DELETE("tasks")
    suspend fun delete(@Path("id") id:String ): Response<Task>

} 