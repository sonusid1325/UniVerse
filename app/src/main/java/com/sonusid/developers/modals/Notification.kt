package com.sonusid.developers.modals

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val communityName: String,
    val isRead: Boolean = false
)
