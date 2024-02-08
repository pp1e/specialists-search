package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.database.asIterable
import ru.ac.uniyar.domain.tables.UserTable

class CheckPasswordOperation(private val database: Database, private val salt: String) {
    val preparedSql = """
        SELECT *
         FROM ${UserTable.tableName}
         WHERE ${UserTable.login.name}=?
         AND ${UserTable.password.name}=HASH('SHA-256', ?, 10)
    """.trimIndent()

    fun check(login: String, password: String): Boolean {
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql).use { statement ->
                statement.setString(1, login)
                statement.setString(2, password + salt)
                return (
                    statement
                        .executeQuery()
                        .asIterable()
                        .count()
                    ) > 0
            }
        }
    }
}
