package ru.ac.uniyar.authentication

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.entities.City
import ru.ac.uniyar.domain.tables.UserTable

data class User(
    val role: Role = Role(),
    val login: String = "",
    val password: String = "",
    val name: String = "",
    val city: City? = null
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): User? =
            try {
                User(
                    Role.fromResultSet(row)!!,
                    row[UserTable.login]!!,
                    row[UserTable.password]!!,
                    row[UserTable.name]!!,
                    City.fromResultSet(row)
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
