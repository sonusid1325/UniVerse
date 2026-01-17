package com.sonusid.developers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sonusid.developers.presentation.EventsManagementScreen
import com.sonusid.developers.presentation.HomeScreen
import com.sonusid.developers.presentation.ProfileScreen
import com.sonusid.developers.presentation.ViewEvent
import com.sonusid.developers.ui.theme.UniVerseTheme
import com.sonusid.developers.viewmodels.EventViewModel

class MainActivity : ComponentActivity() {
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf("home") }
            
            UniVerseTheme {
                when (currentScreen) {
                    "home" -> HomeScreen(
                        viewModel = eventViewModel,
                        onProfileClick = { currentScreen = "profile" },
                        onEventsClick = { currentScreen = "events" },
                        onEventClick = { event ->
                            eventViewModel.selectEvent(event)
                            currentScreen = "event"
                        }
                    )
                    "profile" -> ProfileScreen(onBackClick = { currentScreen = "home" })
                    "events" -> EventsManagementScreen(onBackClick = { currentScreen = "home" })
                    "event" -> {
                        eventViewModel.selectedEvent?.let { event ->
                            ViewEvent(
                                event = event,
                                viewModel = eventViewModel,
                                onBackClick = { currentScreen = "home" }
                            )
                        } ?: run {
                            currentScreen = "home"
                        }
                    }
                }
            }
        }
    }
}
