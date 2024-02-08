package ru.ac.uniyar.domain.entities

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.SpecialistTable
import java.time.LocalDate

data class Specialist(
    val id: Int = -1,
    val userLogin: String = "",
    val date: LocalDate = LocalDate.now(),
    val fullName: String = "",
    val education: String = "",
    val phoneNumber: String = "",
) {
    companion object {
        fun fromResultSet(row: QueryRowSet): Specialist? =
            try {
                Specialist(
                    row[SpecialistTable.id]!!,
                    row[SpecialistTable.userLogin]!!,
                    row[SpecialistTable.date]!!,
                    row[SpecialistTable.fullName]!!,
                    row[SpecialistTable.education]!!,
                    row[SpecialistTable.phoneNumber]!!,
                )
            } catch (npe: NullPointerException) {
                null
            }
    }
}
