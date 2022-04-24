package com.login.core.port

import com.login.entry.dto.UserRequest

interface KeyclockLoginSevicePort {
    fun getTokenUser(user: UserRequest): String?
}
