package com.sonusid.developers.presentation

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonusid.developers.R
import com.sonusid.developers.modals.Event
import com.sonusid.developers.ui.theme.UniVerseTheme

sealed class EventRegistrationState {
    data object NotRegistered : EventRegistrationState()
    data object Registered : EventRegistrationState()
    data class Going(val qrValue: String) : EventRegistrationState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEvent(
    event: Event,
    onBackClick: () -> Unit = {}
) {
    var registrationState by remember { mutableStateOf<EventRegistrationState>(EventRegistrationState.NotRegistered) }
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Text(
                        "Event Details", 
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Serif, 
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Decorative Elements for better MD3 experience
            DecorativeBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Event Header Card
                ExpressiveEventHeader(event)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Description Section
                Text(
                    "About Event",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Join us for an exciting ${event.title} in ${event.location}. This ${event.category} session will cover the latest trends and hands-on practices to help you excel in your campus journey. Don't miss out on this opportunity to connect with peers and experts!",
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Registration State UI
                AnimatedContent(
                    targetState = registrationState,
                    transitionSpec = {
                        fadeIn() + slideInVertically { it } togetherWith fadeOut() + slideOutVertically { -it }
                    },
                    label = "registration_state"
                ) { state ->
                    when (state) {
                        is EventRegistrationState.NotRegistered -> {
                            Button(
                                onClick = { registrationState = EventRegistrationState.Registered },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                                shape = RoundedCornerShape(20.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Register for Free", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        is EventRegistrationState.Registered -> {
                            WaitingState(
                                onRevoke = { registrationState = EventRegistrationState.NotRegistered }, 
                                onDebugApprove = { registrationState = EventRegistrationState.Going("UV-${event.id}-9942") }
                            )
                        }
                        is EventRegistrationState.Going -> {
                            ExpressiveTicket(state.qrValue, onRevoke = { registrationState = EventRegistrationState.NotRegistered })
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-150).dp, y = (-50).dp)
                .graphicsLayer(alpha = 0.05f)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 100.dp, y = 200.dp)
                .graphicsLayer(alpha = 0.05f)
                .background(MaterialTheme.colorScheme.tertiary, CircleShape)
        )
    }
}

@Composable
fun ExpressiveEventHeader(event: Event) {
    // Gradient background for a more "Expressive" feel similar to WelcomeCard
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.background(gradient).padding(24.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = contentColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            event.category.uppercase(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = contentColor,
                            letterSpacing = 1.2.sp
                        )
                    }
                    
                    if (event.isLive) {
                        LiveBadge(contentColor)
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    event.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        lineHeight = 36.sp
                    ),
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                InfoRow(Icons.Default.CalendarToday, event.date, event.time, contentColor)
                Spacer(modifier = Modifier.height(16.dp))
                InfoRow(Icons.Default.LocationOn, event.location, "Main Campus Area", contentColor)
                Spacer(modifier = Modifier.height(16.dp))
                InfoRow(Icons.Default.People, "${event.attendees}+ Students", "Already Registered", contentColor)
            }
        }
    }
}

@Composable
fun LiveBadge(contentColor: Color) {
    Surface(
        color = contentColor.copy(alpha = 0.2f),
        shape = RoundedCornerShape(100.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, contentColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).background(contentColor, CircleShape))
            Spacer(modifier = Modifier.width(6.dp))
            Text("LIVE", color = contentColor, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WaitingState(onRevoke: () -> Unit, onDebugApprove: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Icon(
                        Icons.Default.AccessTime, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.tertiary, 
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        "Registration Pending",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        "The host is reviewing your request.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(
            onClick = onRevoke,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cancel Registration Request", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onDebugApprove) {
            Text("Simulate Host Approval", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun ExpressiveTicket(qrValue: String, onRevoke: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ticket Top
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Registration Confirmed",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Serif),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "Show this QR at the entrance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                // Dotted Line (Perforation effect)
                TicketDashedLine()

                // Ticket Bottom (QR)
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(220.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = Color.White,
                        tonalElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(R.drawable.qr_code),
                                contentDescription = "Entry QR Code",
                                modifier = Modifier.size(180.dp),
                                tint = Color.Black
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            qrValue,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(
            onClick = onRevoke,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Unable to attend? Revoke Ticket", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun TicketDashedLine() {
    val color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
    Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun InfoRow(
    icon: ImageVector, 
    text: String, 
    subtext: String, 
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            color = contentColor.copy(alpha = 0.15f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = contentColor
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text, 
                style = MaterialTheme.typography.bodyLarge, 
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            if (subtext.isNotEmpty()) {
                Text(
                    subtext, 
                    style = MaterialTheme.typography.bodySmall, 
                    color = contentColor.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
@Composable
fun ViewEventPreview() {
    UniVerseTheme {
        ViewEvent(
            event = Event("1", "Tech Workshop", "Friday, Jan 19", "2:00 PM", "Room 301", 45, "Workshop", true)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
@Composable
fun ViewEventPreviewDark() {
    UniVerseTheme {
        ViewEvent(
            event = Event("1", "Tech Workshop", "Friday, Jan 19", "2:00 PM", "Room 301", 45, "Workshop", true)
        )
    }
}
