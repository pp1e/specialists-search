package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserTable : Table<Nothing>(tableName = "SITE_USER") {
    val roleID = int("ROLE_ID")
    val login = varchar("LOGIN").primaryKey()
    val password = varchar("PASSWORD")
    val name = varchar("NAME")
    val cityID = int("CITY_ID")
}
