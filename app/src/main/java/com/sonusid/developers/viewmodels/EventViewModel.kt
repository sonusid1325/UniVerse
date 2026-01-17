package com.sonusid.developers.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sonusid.developers.modals.Event
import com.sonusid.developers.states.EventRegistrationState

class EventViewModel : ViewModel() {
    var events by mutableStateOf(
        listOf(
            Event("1", "Tech Workshop", "Today", "2:00 PM", "Room 301", 45, "Workshop", true),
            Event("2", "Coding Bootcamp", "Tomorrow", "10:00 AM", "Lab A", 32, "Education"),
            Event("3", "Hackathon Meetup", "Sat, Jan 18", "9:00 AM", "Main Hall", 78, "Competition"),
            Event("4", "Design Sprint", "Mon, Jan 20", "3:00 PM", "Creative Space", 24, "Design")
        )
    )
        private set

    var selectedEvent by mutableStateOf<Event?>(null)
        private set

    var registrationState by mutableStateOf<EventRegistrationState>(EventRegistrationState.NotRegistered)
        private set

    fun selectEvent(event: Event) {
        selectedEvent = event
        // Reset registration state for the new event or fetch from a repository
        registrationState = EventRegistrationState.NotRegistered
    }

    fun registerForEvent() {
        registrationState = EventRegistrationState.Registered
    }

    fun approveRegistration(qrValue: String) {
        registrationState = EventRegistrationState.Going(qrValue)
    }

    fun revokeRegistration() {
        registrationState = EventRegistrationState.NotRegistered
    }
}
