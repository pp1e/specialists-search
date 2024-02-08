package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object AdvertTable : Table<Nothing>("ADVERT") {
    val id = int("ID").primaryKey()
    val date = date("DATE")
    val title = varchar("TITLE")
    val description = varchar("DESCRIPTION")
    val serviceCategoryID = int("SERVICE_CATEGORY_ID")
    val cityID = int("CITY_ID")
    val specialistID = int("SPECIALIST_ID")
}
