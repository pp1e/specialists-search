package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object RoleTable : Table<Nothing>(tableName = "ROLE") {
    val id = int("ROLE_ID").primaryKey()
    val name = varchar("NAME")
    val canWatchContactInfo = boolean("CAN_WATCH_CONTACT_INFO")
    val canChangeCity = boolean("CAN_CHANGE_CITY")
    val canSubmitRequest = boolean("CAN_SUBMIT_REQUEST")
    val canWatchSelfRequests = boolean("CAN_WATCH_SELF_REQUESTS")
    val canOperateSelfRequests = boolean("CAN_OPERATE_SELF_REQUESTS")
    val canWatchAllRequests = boolean("CAN_WATCH_ALL_REQUESTS")
    val canOperateAllRequests = boolean("CAN_OPERATE_ALL_REQUESTS")
    val canOperateAdverts = boolean("CAN_OPERATE_ADVERTS")
}
