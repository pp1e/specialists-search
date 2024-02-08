package ru.ac.uniyar.authentication

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.RoleTable

data class Role(
    val id: Int = -1,
    val name: String = "",
    val permissions: Permissions = Permissions()
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Role? =
            try {
                Role(
                    row[RoleTable.id]!!,
                    row[RoleTable.name]!!,
                    Permissions.fromResultSet(row)!!
                )
            } catch (npe: NullPointerException) {
                null
            }

        val GUEST = Role(1, "Гость")
        val USER = Role(2, "Пользователь")
        val ADMIN = Role(3, "Администратор")
        val SPECIALIST = Role(4, "Специалист")
    }
}
