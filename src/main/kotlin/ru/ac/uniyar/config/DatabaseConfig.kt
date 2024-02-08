package ru.ac.uniyar.config

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.string

class DatabaseConfig(
    val dbServerHost: String,
    val dbServerPort: Int,
    val dbName: String,
    val dbUser: String,
    val dbPasswd: String?
) {
    companion object {
        val dbHostLens = EnvironmentKey.string().required("db.host", "Database server host name")
        val dbPortLens = EnvironmentKey.int().required("db.port", "Database server web port")
        val dbNameLens = EnvironmentKey.string().required("db.name", "Database name")
        val dbUserLens = EnvironmentKey.string().required("db.user", "Database user")
        val dbPasswdLens = EnvironmentKey.string().required("deb.passwd", "Database password")

        fun createWebConfig(environment: Environment): DatabaseConfig = DatabaseConfig(
            dbServerHost = dbHostLens(environment),
            dbServerPort = dbPortLens(environment),
            dbName = dbNameLens(environment),
            dbUser = dbUserLens(environment),
            dbPasswd = null,
        )

        val defaultEnv = Environment.defaults()
            .set("db.host", "jdbc:h2:tcp://localhost/database.h2")
            .set("db.port", "8082")
            .set("db.name", "")
            .set("db.user", "sa")
    }
}
