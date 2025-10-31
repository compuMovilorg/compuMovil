package com.example.myapplication.data.dtos

data class RegisterUserDto(
    val name: String,
    val username: String,
    val birthdate: String,
    val FCMToken: String,
    val email: String = ""
)
