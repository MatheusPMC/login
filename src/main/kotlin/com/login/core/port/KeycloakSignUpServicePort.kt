package com.login.core.port

import com.login.entry.dto.UserRequest


interface KeycloakSignUpServicePort {
    fun signUp(user: UserRequest): UserRequest
}