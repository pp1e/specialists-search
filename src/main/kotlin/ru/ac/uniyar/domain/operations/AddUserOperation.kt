package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.tables.UserTable
import java.sql.Types

class AddUserOperation(private val database: Database, private val salt: String) {
    val preparedSql = """
        INSERT INTO ${UserTable.tableName}
         (${UserTable.roleID.name}, ${UserTable.login.name}, ${UserTable.password.name},
         ${UserTable.name.name}, ${UserTable.cityID.name})
         VALUES (?, ?, HASH('SHA-256', ?, 10), ?, ?);
    """.trimIndent()

    fun add(user: User) {
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql).use { statement ->
                statement.setInt(1, user.role.id)
                statement.setString(2, user.login)
                statement.setString(3, user.password + salt)
                statement.setString(4, user.name)
                if (user.city != null)
                    statement.setInt(5, user.city.id)
                else
                    statement.setNull(5, Types.INTEGER)
                statement.execute()
            }
        }
    }
}
