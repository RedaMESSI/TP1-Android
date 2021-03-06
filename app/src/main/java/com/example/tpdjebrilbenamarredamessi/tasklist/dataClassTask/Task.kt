package com.example.tpdjebrilbenamarredamessi.tasklist.dataClassTask

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Task (
    val id: String,
    val title: String,
    val description: String = ""
):

java.io.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String
)