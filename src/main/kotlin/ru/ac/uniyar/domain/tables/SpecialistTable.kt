package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object SpecialistTable : Table<Nothing>("SPECIALIST") {
    val id = int("ID").primaryKey()
    val userLogin = varchar("USER_LOGIN")
    val date = date("DATE")
    val fullName = varchar("FULL_NAME")
    val education = varchar("EDUCATION")
    val phoneNumber = varchar("PHONE_NUMBER")
}
