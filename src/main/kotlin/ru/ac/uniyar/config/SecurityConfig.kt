package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.string

class SecurityConfig(val salt: String) {
    companion object {
        val saltLens = EnvironmentKey.string().required("salt", "Salt for user passwords")

        fun createWebConfig(environment: Environment): SecurityConfig = SecurityConfig(
            salt = saltLens(environment),
        )
    }
}
