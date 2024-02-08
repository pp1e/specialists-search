package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment

class AppConfig(
    val webConfig: WebConfig,
    val databaseConfig: DatabaseConfig,
    val securityConfig: SecurityConfig
) {
    companion object {
        val defaultEnv = Environment.defaults()
            .overrides(WebConfig.defaultEnv)
            .overrides(DatabaseConfig.defaultEnv)
    }
}
