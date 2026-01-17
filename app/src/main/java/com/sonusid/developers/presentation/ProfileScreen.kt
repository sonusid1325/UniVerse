package com.sonusid.developers.presentation

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonusid.developers.ui.theme.UniVerseTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

data class SettingsItemData(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

val accountSettings = listOf(
    SettingsItemData("Personal Info", Icons.Default.PersonOutline, Color(0xFF2196F3)),
    SettingsItemData("My Tickets", Icons.Default.ConfirmationNumber, Color(0xFF4CAF50)),
    SettingsItemData("Joined Hubs", Icons.Default.Groups, Color(0xFF9C27B0))
)

val preferences = listOf(
    SettingsItemData("Notifications", Icons.Default.NotificationsNone, Color(0xFFFF9800)),
    SettingsItemData("Dark Theme", Icons.Default.DarkMode, Color(0xFF607D8B)),
    SettingsItemData("Language", Icons.Default.Language, Color(0xFFE91E63))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        contentVisible = true
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "My Profile",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(600)) + scaleIn(tween(600, easing = EaseOutBack), initialScale = 0.8f)
                ) {
                    ProfileHeaderExpressive()
                }
            }

            item {
                StaggeredProfileItem(visible = contentVisible, index = 1) {
                    StatsSectionExpressive()
                }
            }

            item {
                StaggeredProfileItem(visible = contentVisible, index = 2) {
                    SectionLabelRefined("Personal Hub")
                }
            }

            itemsIndexed(accountSettings) { index, item ->
                StaggeredProfileItem(visible = contentVisible, index = index + 3) {
                    SettingsItemRefined(item)
                }
            }

            item {
                StaggeredProfileItem(visible = contentVisible, index = accountSettings.size + 3) {
                    SectionLabelRefined("Preferences")
                }
            }

            itemsIndexed(preferences) { index, item ->
                StaggeredProfileItem(visible = contentVisible, index = index + accountSettings.size + 4) {
                    SettingsItemRefined(item)
                }
            }

            item {
                StaggeredProfileItem(visible = contentVisible, index = accountSettings.size + preferences.size + 5) {
                    LogoutButtonExpressive()
                }
            }
            
            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun StaggeredProfileItem(visible: Boolean, index: Int, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(tween(600, delayMillis = index * 100)) { 40 } + 
                fadeIn(tween(600, delayMillis = index * 100))
    ) {
        content()
    }
}

@Composable
fun ProfileHeaderExpressive() {
    val infiniteTransition = rememberInfiniteTransition(label = "profile_lava")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "time"
    )

    val isDark = isSystemInDarkTheme()
    val primary = MaterialTheme.colorScheme.primary
    val tertiary = MaterialTheme.colorScheme.tertiary
    
    val glowColor1 = if (isDark) primary.copy(alpha = 0.4f) else primary.copy(alpha = 0.2f)
    val glowColor2 = if (isDark) tertiary.copy(alpha = 0.3f) else tertiary.copy(alpha = 0.15f)

    // Custom Ring Shape for Clipping the Glow
    val ringShape = remember {
        object : Shape {
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                val path = Path().apply {
                    addOval(Rect(Offset.Zero, size))
                    // Clip out the center to make it a ring
                    addOval(Rect(size.width * 0.15f, size.height * 0.15f, size.width * 0.85f, size.height * 0.85f))
                    fillType = PathFillType.EvenOdd
                }
                return Outline.Generic(path)
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            // Enhanced Lava Lamp Glow Behind Avatar - Clipped as Ring
            Box(
                modifier = Modifier
                    .size(190.dp)
                    .clip(ringShape) // Ensures the glow is ring-shaped and not square
            ) {
                Canvas(modifier = Modifier.fillMaxSize().blur(45.dp)) {
                    drawCircle(
                        color = glowColor1,
                        radius = size.width / 1.8f,
                        center = Offset(
                            size.width * (0.5f + 0.2f * sin(time).toFloat()), 
                            size.height * (0.5f + 0.2f * cos(time).toFloat())
                        )
                    )
                    drawCircle(
                        color = glowColor2,
                        radius = size.width / 2.2f,
                        center = Offset(
                            size.width * (0.5f - 0.25f * cos(time).toFloat()), 
                            size.height * (0.5f - 0.25f * sin(time).toFloat())
                        )
                    )
                }
            }

            Box(contentAlignment = Alignment.BottomEnd) {
                // Outer ring/border
                Surface(
                    modifier = Modifier
                        .size(140.dp)
                        .border(
                            BorderStroke(
                                2.dp, 
                                Brush.sweepGradient(listOf(primary, tertiary, primary))
                            ),
                            CircleShape
                        )
                        .padding(6.dp),
                    shape = CircleShape,
                    color = Color.Transparent
                ) {
                    // Avatar Container
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                shadowElevation = 12.dp.toPx()
                                shape = CircleShape
                                clip = true
                            },
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            // Subtle background pattern or gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                primary.copy(alpha = 0.05f),
                                                tertiary.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                            )
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(75.dp),
                                tint = primary.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
                
                // Redesigned Edit Button
                Surface(
                    modifier = Modifier
                        .size(44.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .shadow(8.dp, CircleShape),
                    shape = CircleShape,
                    color = primary,
                    border = BorderStroke(3.dp, MaterialTheme.colorScheme.surface)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.CameraAlt, 
                            "Edit Profile Picture", 
                            modifier = Modifier.size(20.dp), 
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Sonu Sid",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black, 
                letterSpacing = (-0.5).sp
            )
        )
        
        Surface(
            color = primary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Icon(
                    Icons.Default.Verified, 
                    null, 
                    modifier = Modifier.size(16.dp), 
                    tint = primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Computer Science • Level 12",
                    style = MaterialTheme.typography.bodyMedium,
                    color = primary,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "XP", 
                    style = MaterialTheme.typography.labelSmall, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(8.dp))
                LinearProgressIndicator(
                    progress = { 0.65f },
                    modifier = Modifier
                        .width(160.dp)
                        .height(10.dp)
                        .clip(CircleShape),
                    color = primary,
                    trackColor = primary.copy(alpha = 0.1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "650/1000", 
                    style = MaterialTheme.typography.labelSmall, 
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatsSectionExpressive() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(28.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItemRefined("Events", "12", Icons.Default.Event)
            VerticalDivider(modifier = Modifier.height(32.dp), color = MaterialTheme.colorScheme.outlineVariant)
            StatItemRefined("Points", "450", Icons.Default.Token)
            VerticalDivider(modifier = Modifier.height(32.dp), color = MaterialTheme.colorScheme.outlineVariant)
            StatItemRefined("Hubs", "8", Icons.Default.Groups)
        }
    }
}

@Composable
fun StatItemRefined(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, modifier = Modifier.size(22.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SectionLabelRefined(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 2.sp,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
    )
}

@Composable
fun SettingsItemRefined(item: SettingsItemData) {
    Surface(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(18.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(item.color.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, null, tint = item.color, modifier = Modifier.size(24.dp))
            }
            
            Spacer(modifier = Modifier.width(18.dp))
            
            Text(item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
            
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
fun LogoutButtonExpressive() {
    OutlinedButton(
        onClick = { /* Logout */ },
        modifier = Modifier.fillMaxWidth().height(64.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, null)
        Spacer(modifier = Modifier.width(12.dp))
        Text("Sign Out from UniVerse", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
    }
}
