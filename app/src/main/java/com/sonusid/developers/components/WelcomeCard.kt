package com.sonusid.developers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
fun WelcomeCard(onExploreClick: () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "welcome_lava_infinite")
    
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

    val iconPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_pulse"
    )

    val shineProgress by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shine"
    )

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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
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
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // High-Quality Lava Lamp Background
            Canvas(modifier = Modifier.fillMaxSize().blur(60.dp)) {
                drawCircle(
                    color = primaryColor.copy(alpha = 0.45f),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.2f, size.height * blob1Y)
                )
                drawCircle(
                    color = secondaryColor.copy(alpha = 0.4f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.8f, size.height * blob2Y)
                )
                drawCircle(
                    color = tertiaryColor.copy(alpha = 0.35f),
                    radius = (size.width * 0.4f) * blob3Scale,
                    center = Offset(size.width * 0.5f, size.height * 0.5f)
                )
            }

            // Expressive Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Modern Badge
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally { -40 } + fadeIn()
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(100.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp).graphicsLayer {
                                        scaleX = iconPulse
                                        scaleY = iconPulse
                                    },
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "PREMIUM CAMPUS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 1.5.sp
                                )
                            }
                        }
                    }

                    // Title with high contrast
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
                                letterSpacing = (-1.5).sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Subtitle
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(tween(800, delayMillis = 400)) + slideInVertically { 20 }
                    ) {
                        Text(
                            "Your academic universe is ready.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Expressive Call to Action Button
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(800, delayMillis = 600)) + scaleIn(spring(dampingRatio = Spring.DampingRatioLowBouncy), initialScale = 0.8f)
                ) {
                    ExpressiveExploreButton(onClick = onExploreClick)
                }
            }

            // Decorative background element
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(180.dp)
                    .offset(x = 50.dp, y = 50.dp)
                    .graphicsLayer {
                        alpha = 0.08f
                        rotationZ = 12f + (blob1Y * 15f)
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ExpressiveExploreButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "button_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                spotColor = MaterialTheme.colorScheme.primary
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            )
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Explore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Explore Events",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
