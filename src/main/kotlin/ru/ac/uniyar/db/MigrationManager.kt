package ru.ac.uniyar.db

import org.flywaydb.core.Flyway
import ru.ac.uniyar.config.AppConfig

fun performMigrations(config: AppConfig) {
    val flyway = Flyway
        .configure()
        .locations("ru/ac/uniyar/db/migrations")
        .validateMigrationNaming(true)
        .dataSource(
            config.databaseConfig.dbServerHost,
            config.databaseConfig.dbUser,
            config.databaseConfig.dbPasswd
        )
        .load()
    flyway.migrate()
}
