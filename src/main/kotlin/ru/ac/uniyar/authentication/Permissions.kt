package ru.ac.uniyar.authentication

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.RoleTable

class Permissions(
    val canWatchContactInfo: Boolean = false,
    val canChangeCity: Boolean = false,
    val canSubmitRequest: Boolean = false,
    val canWatchSelfRequests: Boolean = false,
    val canOperateSelfRequests: Boolean = false,
    val canWatchAllRequests: Boolean = false,
    val canOperateAllRequests: Boolean = false,
    val canOperateAdverts: Boolean = false
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Permissions? =
            try {
                Permissions(
                    row[RoleTable.canWatchContactInfo]!!,
                    row[RoleTable.canChangeCity]!!,
                    row[RoleTable.canSubmitRequest]!!,
                    row[RoleTable.canWatchSelfRequests]!!,
                    row[RoleTable.canOperateSelfRequests]!!,
                    row[RoleTable.canWatchAllRequests]!!,
                    row[RoleTable.canOperateAllRequests]!!,
                    row[RoleTable.canOperateAdverts]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
