package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int

class WebConfig(
    val webPort: Int,
) {
    companion object {
        val webPortLens = EnvironmentKey.int().required("web.port", "Application web port")

        fun createWebConfig(environment: Environment): WebConfig = WebConfig(
            webPort = webPortLens(environment),
        )

        val defaultEnv = Environment.defaults().set("web.port", "1516")
    }
}
