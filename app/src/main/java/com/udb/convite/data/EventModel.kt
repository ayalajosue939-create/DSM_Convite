package com.udb.convite.data

data class EventModel(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = "",
    val category: String = "Cumpleaños",
    val reminderTime: String = "1 día antes | 9:00am",
    val reminders: List<String> = emptyList(),
    val imageUrl: String = "",
    val reminderActive: Boolean = true,
    val groupTag: String = "Esta semana"
)
