package com.sonusid.developers.modals

data class Event(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val attendees: Int,
    val category: String,
    val isLive: Boolean = false
)