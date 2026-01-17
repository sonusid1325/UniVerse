package com.sonusid.developers.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sonusid.developers.modals.Community
import com.sonusid.developers.modals.Event
import com.sonusid.developers.states.EventRegistrationState

class EventViewModel : ViewModel() {
    var events by mutableStateOf(
        listOf(
            Event("1", "Tech Workshop", "Today", "2:00 PM", "Room 301", 45, "Workshop", true, "c1"),
            Event("2", "Coding Bootcamp", "Tomorrow", "10:00 AM", "Lab A", 32, "Education", false, "c2"),
            Event("3", "Hackathon Meetup", "Sat, Jan 18", "9:00 AM", "Main Hall", 78, "Competition", false, "c1"),
            Event("4", "Design Sprint", "Mon, Jan 20", "3:00 PM", "Creative Space", 24, "Design", false, "c3")
        )
    )
        private set

    var communities by mutableStateOf(
        listOf(
            Community("c1", "Google Developer Group", "Official GDG on campus for developers and enthusiasts.", 1200, isAdmin = true),
            Community("c2", "Code Wizards", "A community for competitive programming and algorithms.", 450, isAdmin = true),
            Community("c3", "Design Hub", "Focusing on UI/UX and product design.", 800),
            Community("c4", "AI Explorers", "Discovering the latest in Machine Learning and AI.", 600)
        )
    )
        private set

    var selectedEvent by mutableStateOf<Event?>(null)
        private set

    var registrationState by mutableStateOf<EventRegistrationState>(EventRegistrationState.NotRegistered)
        private set

    fun selectEvent(event: Event) {
        selectedEvent = event
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

    fun getEventsByCommunity(communityId: String): List<Event> {
        return events.filter { it.hostCommunityId == communityId }
    }

    fun addEvent(event: Event) {
        events = events + event
    }
}
