package com.login.entry.dto

data class LoginRequest(
    var usuario: String = "",
    var senha: String = "",
)