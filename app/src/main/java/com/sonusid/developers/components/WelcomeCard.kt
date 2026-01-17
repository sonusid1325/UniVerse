package com.sonusid.developers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun WelcomeCard() {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "welcome_lava_infinite")
    
    // Lava Lamp movement - Vertical floating
    val blob1Y by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )

    val blob2Y by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
    )

    val blob3Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob3"
    )

    // Pulse for the icon in the badge
    val iconPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_pulse"
    )

    // Shine effect sweep
    val shineProgress by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shine"
    )

    // Entrance animation progress
    val entranceProgress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "entrance"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .graphicsLayer {
                rotationX = (1f - entranceProgress) * 12f
                scaleX = 0.95f + (entranceProgress * 0.05f)
                scaleY = 0.95f + (entranceProgress * 0.05f)
                alpha = entranceProgress
                cameraDistance = 15f * density
            }
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f),
                        Color.Transparent,
                        Color.White.copy(alpha = 0.15f)
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
        ) {
            // Lava Lamp Mesh - tricolor mix
            Canvas(modifier = Modifier.fillMaxSize().blur(50.dp)) {
                drawCircle(
                    color = primaryColor.copy(alpha = 0.4f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.2f, size.height * blob1Y)
                )
                drawCircle(
                    color = secondaryColor.copy(alpha = 0.35f),
                    radius = size.width * 0.45f,
                    center = Offset(size.width * 0.8f, size.height * blob2Y)
                )
                drawCircle(
                    color = tertiaryColor.copy(alpha = 0.3f),
                    radius = (size.width * 0.35f) * blob3Scale,
                    center = Offset(size.width * 0.5f, size.height * 0.5f)
                )
            }

            // Floating Particles
            Canvas(modifier = Modifier.fillMaxSize()) {
                val time = (System.currentTimeMillis() % 10000) / 10000f
                val particles = listOf(
                    Offset(0.15f, 0.25f), Offset(0.75f, 0.2f), 
                    Offset(0.35f, 0.85f), Offset(0.9f, 0.75f),
                    Offset(0.05f, 0.65f)
                )
                
                particles.forEachIndexed { index, baseOffset ->
                    val x = size.width * baseOffset.x + (sin(time * 2 * Math.PI + index) * 10f).toFloat()
                    val y = size.height * baseOffset.y + (sin(time * 2 * Math.PI + index * 2) * 10f).toFloat()
                    
                    drawCircle(
                        color = Color.White.copy(alpha = 0.25f),
                        radius = 2.5.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }

            // Subtle Shine Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            start = Offset(shineProgress * 1200f, 0f),
                            end = Offset(shineProgress * 1200f + 400f, 800f)
                        )
                    )
            )

            // Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Badge Section
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally { -40 } + fadeIn()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(14.dp)
                                    .graphicsLayer {
                                        scaleX = iconPulse
                                        scaleY = iconPulse
                                    },
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PREMIUM CAMPUS HUB",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }

                // Greeting Title
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 200)) + 
                            scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy), initialScale = 0.85f)
                ) {
                    Text(
                        "Hey Sonu! 👋",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-1.2).sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Welcome Subtitle
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 400)) + 
                            slideInVertically { 20 }
                ) {
                    Text(
                        "Your academic universe is ready for exploration.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Large Decorative Background Icon
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(160.dp)
                    .offset(x = 40.dp, y = 40.dp)
                    .graphicsLayer {
                        alpha = 0.1f
                        rotationZ = 12f + (blob1Y * 10f)
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
