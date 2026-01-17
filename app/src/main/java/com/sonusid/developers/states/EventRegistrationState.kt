package com.sonusid.developers.states

sealed class EventRegistrationState {
    data object NotRegistered : EventRegistrationState()
    data object Registered : EventRegistrationState()
    data class Going(val qrValue: String) : EventRegistrationState()
}