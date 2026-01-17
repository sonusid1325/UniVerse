package com.sonusid.developers.presentation

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sonusid.developers.R
import com.sonusid.developers.components.EventCard
import com.sonusid.developers.components.ExpressiveNavigationBar
import com.sonusid.developers.components.QuickActionCard
import com.sonusid.developers.components.SectionHeader
import com.sonusid.developers.components.WelcomeCard
import com.sonusid.developers.modals.ActionIcon
import com.sonusid.developers.modals.Event
import com.sonusid.developers.modals.QuickAction
import com.sonusid.developers.viewmodels.EventViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EventViewModel,
    onProfileClick: () -> Unit = {},
    onEventsClick: () -> Unit = {},
    onCheckInClick: () -> Unit = {},
    onCommunityClick: () -> Unit = {},
    onTrendingClick: () -> Unit = {},
    onEventClick: (Event) -> Unit = {}
) {
    val events = viewModel.events
    var showNotificationSheet by remember { mutableStateOf(false) }

    val quickActions = remember {
        listOf(
            QuickAction(
                "Check In",
                ActionIcon.Drawable(R.drawable.scan_qr_code),
                Color(0xFF4CAF50),
                Color(0xFFE8F5E9),
                onCheckInClick
            ),
            QuickAction("Events", ActionIcon.Drawable(R.drawable.calendar), Color(0xFF2196F3), Color(0xFFE3F2FD), onEventsClick),
            QuickAction("Community", ActionIcon.Drawable(R.drawable.user_round), Color(0xFF9C27B0), Color(0xFFF3E5F5), onCommunityClick),
            QuickAction("Trending", ActionIcon.Vector(Icons.AutoMirrored.Filled.TrendingUp), Color(0xFFFF9800), Color(0xFFFFF3E0), onTrendingClick)
        )
    }

    if (showNotificationSheet) {
        NotificationSheet(
            viewModel = viewModel,
            onDismiss = { showNotificationSheet = false }
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
                        onClick = { showNotificationSheet = true },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        val unreadCount = viewModel.notifications.count { !it.isRead }
                        if (unreadCount > 0) {
                            BadgedBox(badge = { Badge { Text(unreadCount.toString()) } }) {
                                Icon(Icons.Outlined.Notifications, "Notifications")
                            }
                        } else {
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
        bottomBar = {
            ExpressiveNavigationBar(
                currentRoute = "home",
                onNavigate = { route ->
                    when (route) {
                        "home" -> { /* Already here */ }
                        "events" -> onEventsClick()
                        "scan" -> onCheckInClick()
                        "community" -> onCommunityClick()
                        "profile" -> onProfileClick()
                    }
                }
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
                WelcomeCard(onExploreClick = onEventsClick)
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
                EventCard(event, onClick = { onEventClick(event) })
            }

            // Upcoming Section
            item {
                SectionHeader(title = "Upcoming for You", action = "Explore")
            }

            items(events.filter { !it.isLive }) { event ->
                EventCard(event, onClick = { onEventClick(event) })
            }

            // Bottom spacing to avoid overlap with Nav Bar
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
