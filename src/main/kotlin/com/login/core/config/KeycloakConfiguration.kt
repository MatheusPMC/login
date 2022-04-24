package com.dashBoardUniversary.config

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires

@Factory
@ConfigurationProperties(KeycloakConfiguration.KEYCLOAK_CONF_PREFIX)
@Requires(KeycloakConfiguration.KEYCLOAK_CONF_PREFIX)
class KeycloakConfiguration(private val applicationContext: ApplicationContext) {

    var usersRegisterUrl: String = ""
    var accessTokenAdminCliUrl: String = ""
    var grantType: String = ""
    var clientId: String = ""
    var clientSecret: String = ""
    var certsRSAUrl: String = ""
    var authUrl: String = ""

    companion object {
        const val KEYCLOAK_CONF_PREFIX = "keycloak"
    }
}