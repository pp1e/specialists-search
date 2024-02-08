package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.Request
import ru.ac.uniyar.domain.tables.RequestTable
import ru.ac.uniyar.domain.tables.ServiceCategoryTable
import ru.ac.uniyar.domain.tables.SpecialistTable

class GetRequestListOperation(private val database: Database) {
    fun list(login: String): List<Request> =
        database
            .from(RequestTable)
            .innerJoin(
                ServiceCategoryTable,
                on = ServiceCategoryTable.id eq RequestTable.serviceCategoryID
            )
            .innerJoin(
                SpecialistTable,
                on = SpecialistTable.userLogin eq RequestTable.userLogin
            )
            .select()
            .where(RequestTable.userLogin eq login)
            .mapNotNull(Request::fromResultSet)

    fun list(): List<Request> =
        database
            .from(RequestTable)
            .innerJoin(
                ServiceCategoryTable,
                on = ServiceCategoryTable.id eq RequestTable.serviceCategoryID
            )
            .innerJoin(
                SpecialistTable,
                on = SpecialistTable.userLogin eq RequestTable.userLogin
            )
            .select()
            .mapNotNull(Request::fromResultSet)
}
