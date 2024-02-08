package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.ServiceCategoryTable
import java.time.LocalDate

data class ServiceCategory(
    val id: Int = -1,
    val date: LocalDate = LocalDate.now(),
    val title: String = "",
    val advertsCount: Int = 0,
) {
    companion object {
        fun fromResultAndCount(row: QueryRowSet, advertsCount: Int): ServiceCategory? =
            try {
                ServiceCategory(
                    id = row[ServiceCategoryTable.id]!!,
                    date = row[ServiceCategoryTable.date]!!,
                    title = row[ServiceCategoryTable.title]!!,
                    advertsCount = advertsCount
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
