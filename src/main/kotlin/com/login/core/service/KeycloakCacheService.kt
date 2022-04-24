package com.login.core.service

import io.lettuce.core.RedisClient
import io.micronaut.context.annotation.Prototype
import java.time.Duration

@Prototype
class KeycloakCacheService(
    private var redisClient: RedisClient
) {

    var connection = redisClient.connect()
    var commands = connection.sync()

    fun saveTokenAdminCliCache(tokenAdminCli: String) {
        commands.set("tokenAdminCli", tokenAdminCli)
        commands.expire("tokenAdminCli", Duration.ofMinutes(40))
    }

    fun readTokenAdminCliCache(): String? {
        return commands.get("tokenAdminCli")
    }

    fun deleteTokenAdminCliCache(): Long? {
        return commands.del("tokenAdminCli")
    }
}