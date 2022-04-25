package com.login.entry.dto

data class UserRequest(
    var userName: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = ""
)