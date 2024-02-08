package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.authentication.Role
import ru.ac.uniyar.domain.tables.UserTable

class UpdateUserOperation(private val database: Database) {
    fun update(
        cityID: Int?,
        name: String,
        login: String
    ) {
        database.update(UserTable) {
            if (cityID != null)
                set(it.cityID, cityID)
            set(it.name, name)
            where {
                it.login eq login
            }
        }
    }

    fun update(
        role: Role,
        login: String
    ) {
        database.update(UserTable) {
            set(it.roleID, role.id)
            where {
                it.login eq login
            }
        }
    }
}
