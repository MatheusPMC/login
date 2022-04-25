package com.login.core.port

import com.login.entry.dto.LoginRequest
import com.login.entry.dto.UserRequest

interface KeyclockLoginSevicePort {
    fun getTokenUser(user: LoginRequest): String?
}