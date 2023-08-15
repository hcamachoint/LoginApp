package com.webkingve.loginapp.model

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val username: String,
    val email: String,
    val password: String
)
