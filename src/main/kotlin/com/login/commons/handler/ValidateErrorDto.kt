package com.login.commons.handler


class ValidateErrorDto (
    val message: String,
    val timestamp: Long,
    val errors: ArrayList<FieldError>
    )