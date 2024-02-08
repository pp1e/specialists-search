package ru.ac.uniyar.db

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import ru.ac.uniyar.config.AppConfig

fun connectToDatabase(config: AppConfig) = Database.connect(
    url = config.databaseConfig.dbServerHost,
    driver = "org.h2.Driver",
    user = config.databaseConfig.dbUser,
    dialect = MySqlDialect(),
)
