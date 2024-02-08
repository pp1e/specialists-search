package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment

val appEnv =
    Environment.fromResource("ru/ac/uniyar/config/app.properties") overrides
        Environment.JVM_PROPERTIES overrides
        Environment.ENV overrides
        AppConfig.defaultEnv

fun readConfiguration(): AppConfig = AppConfig(
    webConfig = WebConfig.createWebConfig(appEnv),
    securityConfig = SecurityConfig.createWebConfig(appEnv),
    databaseConfig = DatabaseConfig.createWebConfig(appEnv)
)
