package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.RequestTable

data class Request(
    val id: Int,
    val specialist: Specialist,
    val status: Status = Status.NEW,
    val serviceCategory: ServiceCategory,
    val experience: String,
    val commentary: String = "",
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Request? =
            try {
                Request(
                    row[RequestTable.id]!!,
                    Specialist.fromResultSet(row)!!,
                    Status.fromId(row[RequestTable.status]!!),
                    ServiceCategory.fromResultAndCount(row, 0)!!,
                    row[RequestTable.experience]!!,
                    row[RequestTable.commentary]!!
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
