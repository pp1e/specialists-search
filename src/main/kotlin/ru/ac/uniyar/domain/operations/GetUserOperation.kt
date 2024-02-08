package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.authentication.User
import ru.ac.uniyar.domain.tables.CityTable
import ru.ac.uniyar.domain.tables.RoleTable
import ru.ac.uniyar.domain.tables.UserTable

class GetUserOperation(private val database: Database) {
    /*val preparedSql = """
        SELECT *
         FROM ${UserTable.tableName}
         INNER JOIN ROLE ON USER.ROLE_ID=ROLE.ID
         INNER JOIN CITY ON USER.CITY_ID=CITY.ID
         WHERE ${UserTable.login.name}=?
         AND ${UserTable.password.name}=HASH('SHA-256', ?, 10)
    """.trimIndent()

    fun get(login: String, password: String): User? {
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql).use { statement ->
                statement.setString(1, login)
                statement.setString(2, password + salt)
                return (statement
                        .executeQuery()
                        .asIterable()
                        .map {
                            User(
                                    login = it.getString(2),
                                    password = it.getString(3),
                                    name = it.getString(4),
                                    role = Role(
                                            id = it.getInt(6),
                                            name = it.getString(7),
                                            permissions = Permissions(
                                                    canWatchContactInfo = it.getBoolean(8),
                                                    canChangeCity = it.getBoolean(9),
                                                    canSubmitRequest = it.getBoolean(10),
                                                    canWatchSelfRequests= it.getBoolean(11),
                                                    canOperateSelfRequests = it.getBoolean(12),
                                                    canWatchAllRequests = it.getBoolean(13),
                                                    canOperateAllRequests = it.getBoolean(14),
                                                    canOperateAdverts = it.getBoolean(15)
                                            )
                                    ),
                                    city = City(
                                            id = it.getInt(16),
                                            title = it.getString(17)
                                    )
                            )
                        }
                        .firstOrNull())
            }
        }
    }*/

    fun get(login: String): User? =
        database
            .from(UserTable)
            .innerJoin(RoleTable, on = RoleTable.id eq UserTable.roleID)
            .leftJoin(CityTable, on = CityTable.id eq UserTable.cityID)
            .select()
            .where(UserTable.login eq login)
            .map(User::fromResultSet)
            .firstOrNull()
}
