package com.sonusid.developers.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonusid.developers.R
import com.sonusid.developers.components.EventCard
import com.sonusid.developers.components.QuickActionCard
import com.sonusid.developers.components.SectionHeader
import com.sonusid.developers.components.WelcomeCard
import com.sonusid.developers.modals.ActionIcon
import com.sonusid.developers.modals.Event
import com.sonusid.developers.modals.QuickAction
import com.sonusid.developers.ui.theme.UniVerseTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onProfileClick: () -> Unit = {},
               onEventsClick: () -> Unit = {}) {
    val events = remember {
        listOf(
            Event("1", "Tech Workshop", "Today", "2:00 PM", "Room 301", 45, "Workshop", true),
            Event("2", "Coding Bootcamp", "Tomorrow", "10:00 AM", "Lab A", 32, "Education"),
            Event("3", "Hackathon Meetup", "Sat, Jan 18", "9:00 AM", "Main Hall", 78, "Competition"),
            Event("4", "Design Sprint", "Mon, Jan 20", "3:00 PM", "Creative Space", 24, "Design")
        )
    }

    val quickActions = remember {
        listOf(
            QuickAction(
                "Check In",
                ActionIcon.Drawable(R.drawable.scan_qr_code),
                Color(0xFF4CAF50),
                Color(0xFFE8F5E9),
                {}
            ),
            QuickAction("Events", ActionIcon.Drawable(R.drawable.calendar), Color(0xFF2196F3), Color(0xFFE3F2FD), onEventsClick),
            QuickAction("Community", ActionIcon.Drawable(R.drawable.user_round), Color(0xFF9C27B0), Color(0xFFF3E5F5), {}),
            QuickAction("Trending", ActionIcon.Vector(Icons.AutoMirrored.Filled.TrendingUp), Color(0xFFFF9800), Color(0xFFFFF3E0), {})
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "UniVerse",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Campus Community",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Notifications */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        BadgedBox(badge = { Badge { Text("3") } }) {
                            Icon(Icons.Outlined.Notifications, "Notifications")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* Quick check-in */ },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(painter = painterResource(R.drawable.scan_qr_code), "Scan") },
                text = { Text("Quick Scan") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Welcome Section
            item {
                WelcomeCard()
            }

            // Quick Actions Section
            item {
                SectionHeader(title = "Quick Access", action = "Settings")
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(quickActions) { action ->
                        QuickActionCard(action)
                    }
                }
            }

            // Live Events / Happening Now
            item {
                SectionHeader(title = "Happening Now", action = "See All")
            }

            items(events.filter { it.isLive }) { event ->
                EventCard(event)
            }

            // Upcoming Section
            item {
                SectionHeader(title = "Upcoming for You", action = "Explore")
            }

            items(events.filter { !it.isLive }) { event ->
                EventCard(event)
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    UniVerseTheme{
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen()
        }
    }
}
@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun HomeScreenPreviewDark() {
    UniVerseTheme{
        Surface(color = MaterialTheme.colorScheme.background) {
            HomeScreen()
        }
    }
}

