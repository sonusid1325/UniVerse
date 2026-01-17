package com.sonusid.developers.presentation

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.sonusid.developers.modals.Community
import com.sonusid.developers.modals.Event
import com.sonusid.developers.states.EventRegistrationState
import com.sonusid.developers.viewmodels.EventViewModel
import kotlinx.coroutines.delay
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEvent(
    event: Event,
    viewModel: EventViewModel,
    onBackClick: () -> Unit = {},
    onCommunityClick: (Community) -> Unit = {}
) {
    val registrationState = viewModel.registrationState
    val scrollState = rememberScrollState()
    val hostCommunity = viewModel.communities.find { it.id == event.hostCommunityId }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        contentVisible = true
    }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Text(
                        "Event Explorer", 
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
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
                        Icon(Icons.Default.IosShare, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                // High-End Lava Lamp Header
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(600)) + scaleIn(tween(600, easing = EaseOutBack), initialScale = 0.92f)
                ) {
                    Box(modifier = Modifier.padding(20.dp)) {
                        EventLavaBannerExpressive(event)
                    }
                }

                // Staggered Entrance for Content
                StaggeredVerticalEntrance(visible = contentVisible, delay = 200) {
                    // Information Grid
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoChipExpressive(Icons.Default.CalendarToday, event.date, "Date", Modifier.weight(1f))
                        InfoChipExpressive(Icons.Default.Schedule, event.time, "Time", Modifier.weight(1f))
                    }
                }

                StaggeredVerticalEntrance(visible = contentVisible, delay = 300) {
                    Spacer(modifier = Modifier.height(12.dp))
                    // Location Section
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Place, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Venue Location", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Text(event.location, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }

                StaggeredVerticalEntrance(visible = contentVisible, delay = 400) {
                    Spacer(modifier = Modifier.height(24.dp))
                    TagsSectionExpressive(listOf("Technology", "Workshop", "Campus", "SkillUp"))
                }

                StaggeredVerticalEntrance(visible = contentVisible, delay = 500) {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
                        Text(
                            "About this Event",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Dive into the world of ${event.category} with this exclusive session. Hosted by professionals, this event aims to provide hands-on experience and deep insights into modern industry practices. Join your peers for an unforgettable learning journey.",
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        hostCommunity?.let { community ->
                            HostSectionExpressiveRefined(community, onCommunityClick)
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        // Registration CTA
                        AnimatedContent(
                            targetState = registrationState,
                            transitionSpec = {
                                fadeIn() + scaleIn(initialScale = 0.9f) togetherWith fadeOut() + scaleOut(targetScale = 0.9f)
                            },
                            label = "registration_state"
                        ) { state ->
                            when (state) {
                                is EventRegistrationState.NotRegistered -> {
                                    Button(
                                        onClick = { viewModel.registerForEvent() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(68.dp)
                                            .graphicsLayer {
                                                shadowElevation = 8.dp.toPx()
                                                shape = RoundedCornerShape(24.dp)
                                                clip = true
                                            },
                                        shape = RoundedCornerShape(24.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Bolt, null)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Register Now", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                                        }
                                    }
                                }
                                is EventRegistrationState.Registered -> {
                                    WaitingStateRefined(
                                        onRevoke = { viewModel.revokeRegistration() }, 
                                        onDebugApprove = { viewModel.approveRegistration("UV-${event.id}-CONFIRMED") }
                                    )
                                }
                                is EventRegistrationState.Going -> {
                                    ExpressiveTicketRefined(state.qrValue, onRevoke = { viewModel.revokeRegistration() })
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StaggeredVerticalEntrance(visible: Boolean, delay: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(tween(600, delayMillis = delay)) { it / 2 } + fadeIn(tween(600, delayMillis = delay))
    ) {
        content()
    }
}

@Composable
fun EventLavaBannerExpressive(event: Event) {
    val infiniteTransition = rememberInfiniteTransition(label = "event_lava_pro")
    
    val blob1X by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "blob1"
    )
    val blob2Y by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "blob2"
    )
    val blob3Scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearOutSlowInEasing), RepeatMode.Reverse),
        label = "blob3"
    )
    val shineProgress by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "shine"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val primary = MaterialTheme.colorScheme.primary
            val secondary = MaterialTheme.colorScheme.secondary
            val tertiary = MaterialTheme.colorScheme.tertiary

            // Vibrant Lava Lamp Mesh
            Canvas(modifier = Modifier.fillMaxSize().blur(60.dp)) {
                drawCircle(color = primary.copy(alpha = 0.5f), radius = size.width / 1.8f, center = Offset(size.width * blob1X, size.height * 0.3f))
                drawCircle(color = secondary.copy(alpha = 0.45f), radius = size.width / 2f, center = Offset(size.width * 0.5f, size.height * blob2Y))
                drawCircle(color = tertiary.copy(alpha = 0.4f), radius = (size.width / 2.2f) * blob3Scale, center = Offset(size.width * (1f - blob1X), size.height * 0.7f))
            }

            // Shine Sweep
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.1f), Color.Transparent),
                            start = Offset(shineProgress * 1500f, 0f),
                            end = Offset(shineProgress * 1500f + 300f, 800f)
                        )
                    )
            )

            // Readability Gradient
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.6f)))
            ))

            // Content
            Column(
                modifier = Modifier.fillMaxSize().padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = event.category.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Column {
                    Text(
                        event.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 38.sp,
                            letterSpacing = (-1).sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(28.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(Icons.Default.TrendingUp, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(6.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Join 120+ attendees interested in ${event.category}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChipExpressive(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HostSectionExpressiveRefined(community: Community, onCommunityClick: (Community) -> Unit) {
    Text(
        "Presented By",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp
    )
    Spacer(modifier = Modifier.height(16.dp))
    Card(
        onClick = { onCommunityClick(community) },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)),
                        CircleShape
                    )
                    .padding(2.dp)
            ) {
                Surface(modifier = Modifier.fillMaxSize(), shape = CircleShape, color = MaterialTheme.colorScheme.surface) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Group, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(community.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                    if (community.isAdmin) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.Verified, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
                Text("${community.memberCount} active members", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun TagsSectionExpressive(tags: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(tags) { tag ->
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                Text(
                    text = tag,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun WaitingStateRefined(onRevoke: () -> Unit, onDebugApprove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp), 
                strokeWidth = 5.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Confirming your spot...", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
            Text("We're validating your request with the community.", textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = onRevoke) { Text("Cancel", fontWeight = FontWeight.Bold) }
                Button(onClick = onDebugApprove, shape = RoundedCornerShape(16.dp)) { Text("Skip Wait", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun ExpressiveTicketRefined(qrValue: String, onRevoke: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("OFFICIAL PASS", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, letterSpacing = 3.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(28.dp))
            Surface(
                modifier = Modifier
                    .size(200.dp)
                    .padding(4.dp)
                    .graphicsLayer {
                        shadowElevation = 4.dp.toPx()
                        shape = RoundedCornerShape(24.dp)
                        clip = true
                    },
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
            ) {
                val bitmap = remember(qrValue) { generateQRCode(qrValue) }
                bitmap?.let { Image(bitmap = it.asImageBitmap(), contentDescription = "QR", modifier = Modifier.fillMaxSize().padding(16.dp)) }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(qrValue, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(28.dp))
            Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                drawLine(color = Color.LightGray, start = Offset(0f, 0f), end = Offset(size.width, 0f), pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f), strokeWidth = 2.dp.toPx())
            }
            Spacer(modifier = Modifier.height(28.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("STATUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text("CONFIRMED", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color(0xFF2E7D32))
                }
                IconButton(
                    onClick = onRevoke,
                    modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f), CircleShape)
                ) { Icon(Icons.Default.DeleteOutline, null, tint = MaterialTheme.colorScheme.error) }
            }
        }
    }
}

private fun generateQRCode(content: String): Bitmap? {
    return try {
        val size = 512
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.MARGIN] = 1
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
