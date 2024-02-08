package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.Status
import ru.ac.uniyar.domain.tables.RequestTable
import ru.ac.uniyar.domain.tables.ServiceCategoryTable

class GetServiceCategoryTitlesFromRequestsOperation(private val database: Database) {
    fun list(login: String, status: Status): Map<Int, String> =
        database
            .from(RequestTable)
            .innerJoin(
                ServiceCategoryTable,
                on = ServiceCategoryTable.id eq RequestTable.serviceCategoryID
            )
            .select(
                RequestTable.userLogin,
                RequestTable.status,
                ServiceCategoryTable.id,
                ServiceCategoryTable.title
            )
            .where((RequestTable.userLogin eq login) and (RequestTable.status eq status.id))
            .mapNotNull { it[ServiceCategoryTable.id]!! to it[ServiceCategoryTable.title]!! }
            .toMap()
}
