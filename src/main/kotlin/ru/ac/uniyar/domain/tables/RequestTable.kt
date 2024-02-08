package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object RequestTable : Table<Nothing>(tableName = "REQUEST") {
    val id = int("ID").primaryKey()
    val userLogin = varchar("USER_LOGIN")
    val serviceCategoryID = int("SERVICE_CATEGORY_ID")
    val status = int("STATUS")
    val experience = varchar("EXPERIENCE")
    val commentary = varchar("COMMENTARY")
}
