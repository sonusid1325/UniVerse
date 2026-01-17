package com.sonusid.developers.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
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

    val infiniteTransition = rememberInfiniteTransition(label = "welcome_card_infinite")
    
    // Floating movement for blobs
    val blobOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1"
    )

    val blobOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2"
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

    // Entrance 3D rotation and scale
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
            .height(200.dp)
            .graphicsLayer {
                rotationX = (1f - entranceProgress) * 15f
                scaleX = 0.9f + (entranceProgress * 0.1f)
                scaleY = 0.9f + (entranceProgress * 0.1f)
                alpha = entranceProgress
                cameraDistance = 12f * density
            }
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.Transparent,
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(32.dp)
            ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryColor)
        ) {
            // Dynamic Mesh Gradient
            Canvas(modifier = Modifier.fillMaxSize().blur(45.dp)) {
                drawCircle(
                    color = secondaryColor,
                    radius = size.width * 0.7f,
                    center = Offset(
                        x = size.width * 0.8f + blobOffset1 * 2,
                        y = size.height * 0.2f + blobOffset2
                    )
                )
                drawCircle(
                    color = tertiaryColor,
                    radius = size.width * 0.6f,
                    center = Offset(
                        x = size.width * 0.1f - blobOffset2,
                        y = size.height * 0.9f + blobOffset1 * 1.5f
                    )
                )
            }

            // Floating Particles (The "Little Elements")
            Canvas(modifier = Modifier.fillMaxSize()) {
                val time = (System.currentTimeMillis() % 10000) / 10000f
                val particles = listOf(
                    Offset(0.2f, 0.3f), Offset(0.7f, 0.15f), 
                    Offset(0.4f, 0.8f), Offset(0.85f, 0.7f),
                    Offset(0.1f, 0.6f)
                )
                
                particles.forEachIndexed { index, baseOffset ->
                    val x = size.width * baseOffset.x + (sin(time * 2 * Math.PI + index) * 15f).toFloat()
                    val y = size.height * baseOffset.y + (sin(time * 2 * Math.PI + index * 2) * 15f).toFloat()
                    
                    drawCircle(
                        color = Color.White.copy(alpha = 0.2f),
                        radius = 3.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }

            // Shine Sweep
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.12f),
                                Color.White.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            start = Offset(shineProgress * 1000f, 0f),
                            end = Offset(shineProgress * 1000f + 300f, 600f)
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Badge
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)) { -40 } + fadeIn()
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
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
                                tint = onPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "PREMIUM CAMPUS",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = onPrimary,
                                letterSpacing = 1.5.sp
                            )
                        }
                    }
                }

                // Title
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 200)) + 
                            scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy), initialScale = 0.8f)
                ) {
                    Text(
                        "Hey Sonu! 👋",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = onPrimary,
                            letterSpacing = (-1.5).sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 400)) + 
                            slideInVertically(tween(800, delayMillis = 400)) { 20 }
                ) {
                    Text(
                        "Your academic universe is ready for exploration.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = onPrimary.copy(alpha = 0.85f),
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Background Decorative Icon
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(140.dp)
                    .offset(x = 30.dp, y = 30.dp)
                    .graphicsLayer {
                        alpha = 0.12f
                        rotationZ = 15f + blobOffset1 * 0.8f
                        scaleX = 1.15f
                        scaleY = 1.15f
                    },
                tint = Color.White
            )
        }
    }
}
