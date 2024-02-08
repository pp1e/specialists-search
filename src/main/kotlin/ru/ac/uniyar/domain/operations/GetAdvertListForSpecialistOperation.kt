package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.entities.Advert
import ru.ac.uniyar.domain.entities.Advert.Companion.fromResultSet
import ru.ac.uniyar.domain.tables.AdvertTable
import ru.ac.uniyar.domain.tables.CityTable
import ru.ac.uniyar.domain.tables.ServiceCategoryTable
import ru.ac.uniyar.domain.tables.SpecialistTable

class GetAdvertListForSpecialistOperation(
    private val database: Database
) {
    fun list(specialistID: Int): List<Advert> =
        database
            .from(AdvertTable)
            .innerJoin(SpecialistTable, on = SpecialistTable.id eq AdvertTable.specialistID)
            .innerJoin(ServiceCategoryTable, on = ServiceCategoryTable.id eq AdvertTable.serviceCategoryID)
            .innerJoin(CityTable, on = CityTable.id eq AdvertTable.cityID)
            .select(
                AdvertTable.id,
                AdvertTable.date,
                AdvertTable.serviceCategoryID,
                AdvertTable.cityID,
                AdvertTable.specialistID,
                AdvertTable.title,
                AdvertTable.description,
                SpecialistTable.id,
                SpecialistTable.date,
                SpecialistTable.phoneNumber,
                SpecialistTable.education,
                SpecialistTable.userLogin,
                SpecialistTable.fullName,
                ServiceCategoryTable.id,
                ServiceCategoryTable.date,
                ServiceCategoryTable.title,
                CityTable.id,
                CityTable.title,
            )
            .where(specialistID eq AdvertTable.specialistID)
            .orderBy(AdvertTable.date.desc())
            .mapNotNull(::fromResultSet)
}
