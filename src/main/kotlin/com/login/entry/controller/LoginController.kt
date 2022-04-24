package com.login.entry.controller

import com.login.core.port.KeycloakSignUpServicePort
import com.login.core.port.KeyclockLoginSevicePort
import com.login.entry.dto.UserRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/v1/")
class LoginController(
    private val userSignUpServicePort: KeycloakSignUpServicePort,
    private val keyclockLoginSevicePort: KeyclockLoginSevicePort
    ) {

    @Post("register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body user: UserRequest) {
        userSignUpServicePort.signUp(user)
    }

    @Post("login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun loginAccount(@Body user: UserRequest): HttpResponse<String>{
        val result = keyclockLoginSevicePort.getTokenUser(user)
        return HttpResponse.ok(user).status(200).body(result)
    }
}