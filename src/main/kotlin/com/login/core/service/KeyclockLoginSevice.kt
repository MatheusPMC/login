package com.login.core.service

import com.dashBoardUniversary.commons.extensions.TestLogging
import com.dashBoardUniversary.commons.extensions.logger
import com.dashBoardUniversary.config.KeycloakConfiguration
import com.login.core.port.KeyclockLoginSevicePort
import com.google.gson.JsonParser
import com.login.entry.dto.UserRequest
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import io.micronaut.context.annotation.Prototype

@Prototype
class KeyclockLoginSevice(
    private val keycloakConfiguration: KeycloakConfiguration
        ): KeyclockLoginSevicePort, TestLogging {
    override fun getTokenUser(user: UserRequest): String? {
            val client = OkHttpClient()

            val mediaType = MediaType.parse("application/x-www-form-urlencoded")
            val body = RequestBody.create(mediaType,
                    "username=${user.username}&password=${user.password}&grant_type=password&client_id=${keycloakConfiguration.clientId}&client_secret=${keycloakConfiguration.clientSecret}")
            val request = Request.Builder()
                .url(keycloakConfiguration.accessTokenAdminCliUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build()
            val response = client.newCall(request).execute()

        val responseBodyToString = response.body().string()
        val responseBodyToJson = JsonParser.parseString(responseBodyToString)

        if (responseBodyToString.contains("access_token")) {
            logger().info("getAccessTokenAdminCli - access_token capturado!")
            return responseBodyToJson.asJsonObject["access_token"].asString
        } else {
            logger().error("getAccessTokenAdminCli - access_token nÃ£o capturado!")
            throw java.lang.RuntimeException("KeycloakSingUpService - Token not received")
        }
    }
}