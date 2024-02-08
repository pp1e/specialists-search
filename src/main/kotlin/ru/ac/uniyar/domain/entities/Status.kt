package ru.ac.uniyar.domain.entities

enum class Status(val id: Int, val title: String) {
    NEW(1, "Новая"),
    CONFIRMED(2, "Подтверждена"),
    REJECTED(3, "Отклонена");

    companion object {
        fun fromId(id: Int): Status = when (id) {
            2 -> CONFIRMED
            3 -> REJECTED
            else -> NEW
        }
    }
}
