package com.sonusid.developers.modals

data class Community(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val logoUrl: String = "",
    val isAdmin: Boolean = false
)
