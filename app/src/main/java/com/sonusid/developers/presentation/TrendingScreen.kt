package com.sonusid.developers.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonusid.developers.components.EventCard
import com.sonusid.developers.modals.Event
import com.sonusid.developers.viewmodels.EventViewModel
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    viewModel: EventViewModel,
    onBackClick: () -> Unit,
    onEventClick: (Event) -> Unit
) {
    val trendingEvents = viewModel.events.filter { it.attendees > 50 } // Example logic for trending
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        contentVisible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "trending_lava")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Trending Now",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Minimal Lava Background
            Box(modifier = Modifier.fillMaxSize().blur(60.dp)) {
                Spacer(
                    modifier = Modifier
                        .size(250.dp)
                        .offset(x = (sin(time) * 50).dp, y = (cos(time) * 30).dp)
                        .align(Alignment.TopStart)
                        .background(colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                )
                Spacer(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = (cos(time * 0.7f) * 40).dp, y = (sin(time * 0.7f) * 60).dp)
                        .align(Alignment.BottomEnd)
                        .background(colorScheme.tertiary.copy(alpha = 0.08f), CircleShape)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AnimatedVisibility(
                        visible = contentVisible,
                        enter = fadeIn() + expandVertically()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            color = colorScheme.primaryContainer.copy(alpha = 0.3f)
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        "Hot Topics",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Check out what's buzzing in your campus.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                itemsIndexed(trendingEvents) { index, event ->
                    AnimatedVisibility(
                        visible = contentVisible,
                        enter = fadeIn(tween(600, delayMillis = index * 100)) + 
                                slideInVertically(tween(600, delayMillis = index * 100)) { 40 }
                    ) {
                        EventCard(event = event, onClick = { onEventClick(event) })
                    }
                }
            }
        }
    }
}
