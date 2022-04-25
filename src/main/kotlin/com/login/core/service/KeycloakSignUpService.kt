package com.login.core.service

import com.login.commons.extensions.TestLogging
import com.login.commons.extensions.logger
import com.login.commons.handler.KeycloakException
import com.login.core.config.KeycloakConfiguration
import com.login.core.port.KeycloakSignUpServicePort
import com.google.gson.JsonParser
import com.login.entry.dto.UserRequest
import com.nimbusds.jose.jwk.JWK
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import io.jsonwebtoken.Jwts
import io.micronaut.context.annotation.Prototype
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import java.time.Instant

@Prototype
class KeycloakSignUpService(
    private val keycloakConfiguration: KeycloakConfiguration,
    private val keycloakCacheService: KeycloakCacheService,
) : KeycloakSignUpServicePort, TestLogging {

    val client = OkHttpClient()

    override fun signUp(user: UserRequest): UserRequest {
        logger().info("signUp - Inicio do serviÃ§o keycloak")

        val tokenAdminCliCache = keycloakCacheService.readTokenAdminCliCache()

        logger().info("signUp - Verificando o Token do Admin Cli (CACHE)")
        val accessToken = if (tokenAdminCliCache != null && this.verifyExpTokenAdminCli(tokenAdminCliCache)) {
            logger().info("signUp - Token do admin cli (cache) Ã© vÃ¡lido")
            tokenAdminCliCache
        } else {
            logger().info("signUp - Token do admin cli (cache) invÃ¡lido, gerando um novo...")
            keycloakCacheService.deleteTokenAdminCliCache()

            val accessTokenAdminCli = this.getAccessTokenAdminCli()
            keycloakCacheService.saveTokenAdminCliCache(accessTokenAdminCli)

            println(accessTokenAdminCli)
            accessTokenAdminCli
        }

        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, "{\"firstName\":\"${user.firstName}\"," +
                "\"lastName\":\"${user.lastName}\"," +
                "\"username\":\"${user.userName}\"," +
                "\"email\":\"${user.email}\"," +
                "\"emailVerified\":${true}," +
                "\"credentials\":[{\"type\":\"password\",\"value\":\"${user.password}\",\"temporary\":false}],\"enabled\":true}")
        val request = Request.Builder()
            .url(keycloakConfiguration.usersRegisterUrl)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw KeycloakException("KeycloakSingUpService - NÃ£o foi possÃ­vel cadastrar o usuÃ¡rio")
        }

        logger().info("signUp - usuÃ¡rio registrado no keycloak!")
        return UserRequest(user.userName, user.firstName, user.lastName, user.email, user.password)
    }

    private fun getAccessTokenAdminCli(): String {
        logger().info("getAccessTokenAdminCli - capturando o access_token do admin-cli")

        val mediaType = MediaType.parse("application/x-www-form-urlencoded")
        val body = RequestBody.create(mediaType,
            "grant_type=${keycloakConfiguration.grantType}&client_id=${keycloakConfiguration.clientId}&client_secret=${keycloakConfiguration.clientSecret}")
        val request = Request.Builder()
            .url(keycloakConfiguration.accessTokenAdminCliUrl)
            .post(body)
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
            throw KeycloakException("KeycloakSingUpService - Token not received")
        }
    }

        fun verifyExpTokenAdminCli(token: String): Boolean {
        logger().info("verifyExpTokenAdminCli - verificando expiraÃ§Ã£o do token do admin-cli")

        try {
            val jwk = JWK.parse(this.getJWKCerts())

            val rsaKey = jwk.toRSAKey()

            val factory = KeyFactory.getInstance(jwk.keyType.value)
            val rsaPublicKeySpec = RSAPublicKeySpec(
                rsaKey.modulus.decodeToBigInteger(), // n
                rsaKey.publicExponent.decodeToBigInteger() //e
            )
            val publicKeySpec: PublicKey = factory.generatePublic(rsaPublicKeySpec)

            val claims = Jwts.parser().setSigningKey(publicKeySpec).parseClaimsJws(token)

            val expiration = claims.body["exp"] as Int
            val expirationInstant = Instant.ofEpochMilli(expiration.toLong())

            return Instant.now().isAfter(expirationInstant)

        } catch (e: Exception) {
            logger().error("verifyExpTokenAdminCli - token expirado ou invÃ¡lido")
            return false
        }
    }

    private fun getJWKCerts(): String {
        logger().info("getJWKCerts - obtendo certs do keycloak e gerando a chave pÃºblica (RSA)")
        // Obtendo o certificado para gerar a public key
        val request = Request.Builder()
            .url(keycloakConfiguration.certsRSAUrl)
            .get()
            .build()

        val response = client.newCall(request).execute()
        val responseJson = JsonParser.parseString(response.body().string())
        val keys = responseJson.asJsonObject["keys"].asJsonArray

        return keys[1].toString()
    }
}