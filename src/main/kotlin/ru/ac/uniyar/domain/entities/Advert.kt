package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.AdvertTable
import java.time.LocalDate

data class Advert(
    val id: Int = -1,
    val date: LocalDate = LocalDate.now(),
    val serviceCategory: ServiceCategory = ServiceCategory(),
    val city: City = City(),
    val specialist: Specialist = Specialist(),
    val title: String = "",
    val description: String = "",
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Advert? =
            try {
                Advert(
                    id = row[AdvertTable.id]!!,
                    date = row[AdvertTable.date]!!,
                    serviceCategory = ServiceCategory.fromResultAndCount(row, 0)!!,
                    city = City.fromResultSet(row)!!,
                    specialist = Specialist.fromResultSet(row)!!,
                    title = row[AdvertTable.title]!!,
                    description = row[AdvertTable.description]!!,
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
