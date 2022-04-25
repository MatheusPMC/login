package com.login.entry.controller

import com.login.core.mapper.ContactConverter
import com.login.core.port.ContactServicePort
import com.login.core.port.KeycloakSignUpServicePort
import com.login.core.port.KeyclockLoginSevicePort
import com.login.entry.dto.ContactDto
import com.login.entry.dto.LoginRequest
import com.login.entry.dto.UserRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/v1/")
class LoginController(
    private val userSignUpServicePort: KeycloakSignUpServicePort,
    private val keyclockLoginSevicePort: KeyclockLoginSevicePort,
    private val contactServicePort: ContactServicePort
    ) {

    @Post("register")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun createAccount(@Body user: UserRequest): HttpResponse<UserRequest>? {
        println(user)
        var test = userSignUpServicePort.signUp(user)
        return HttpResponse.ok(test).status(200).body(test)
    }

    @Post("login")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun loginAccount(@Body user: LoginRequest): HttpResponse<String>{
        val result = keyclockLoginSevicePort.getTokenUser(user)
        return HttpResponse.ok(result).status(200).body(result)
    }

    @Get("test")
    @Secured("admin")
    fun loginAccount2() {
        println("entrou test")
    }

    @Get
    @Secured("viewer")
    @Produces
    fun getAllContacts(): MutableHttpResponse<List<ContactDto?>>? {
        var result = contactServicePort.getAllContacts()
        return HttpResponse.ok(result).status(200)
    }

    @Get("/{id}")
    @Secured("viewer")
    @Produces
    fun getOneContact(@PathVariable id: String): MutableHttpResponse<ContactDto>? {
        val result = contactServicePort.getOneContact(id)
        return HttpResponse.ok(result).status(200)
    }

    @Post
    @Secured("viewer")
    @Produces
    fun postContact(@Body contactDto: ContactDto): MutableHttpResponse<ContactDto>? {
        println(contactDto)
        val result = contactServicePort.postContact(ContactConverter.contactDtoToContact(contactDto))
        return HttpResponse.created(result).status(201)
    }

    @Put("/{id}")
    @Secured("admin")
    @Produces
    fun putContact(@Body contactDto: ContactDto, @PathVariable id: String): MutableHttpResponse<ContactDto>? {
        contactDto.id = id
        val result = contactServicePort.putContact(ContactConverter.contactDtoToContact(contactDto))
        return HttpResponse.ok(result).status(200)
    }

    @Delete("/{id}")
    @Secured("admin")
    @Produces
    fun delContact(@PathVariable id: String): MutableHttpResponse<String>? {
        val result = contactServicePort.delContact(id)
        return HttpResponse.ok(result).status(200)
    }
}
