package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object CityTable : Table<Nothing>("CITY") {
    val id = int("ID").primaryKey()
    val title = varchar("TITLE")
}
