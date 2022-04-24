package com.dashBoardUniversary.commons.handler

import com.dashBoardUniversary.commons.extensions.TestLogging
import com.dashBoardUniversary.commons.extensions.logger
import io.micronaut.aop.Around
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.context.annotation.Prototype
import io.micronaut.http.HttpResponse
import java.util.Date
import javax.validation.ConstraintViolationException

@Around
@Retention(AnnotationRetention.RUNTIME)
annotation class HandlerException()

@InterceptorBean(HandlerException::class)
@Prototype
class HandlerExceptions : MethodInterceptor<Any, Any>, TestLogging {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        return try {
            context.proceed()
        } catch (e: java.lang.RuntimeException) {
            when (e) {
                is ConstraintViolationException -> {
                    val errors = ArrayList<FieldError>()
                    for (cv in e.constraintViolations)  {
                        errors.add(FieldError(cv.propertyPath.first().name, cv.message))
                    }
                    return HttpResponse.badRequest(ValidateErrorDto(e.message!!, Date().time, errors))
                }
                is MongoException -> {
                    return logger().error(e.message)
                }
                is AwsConnectionException -> {
                    return logger().error(e.message)
                }
                is KeycloakException -> {
                    return logger().error(e.message)
                }
                else -> {
                    return logger().error(e.message)
                }
            }
        }
    }
}