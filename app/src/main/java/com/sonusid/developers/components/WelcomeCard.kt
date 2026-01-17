package com.sonusid.developers.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WelcomeCard() {
    // Animation state
    var visible by remember { mutableStateOf(false) }
    
    // Trigger animation on entry
    LaunchedEffect(Unit) {
        delay(100) // Small delay for smooth transition
        visible = true
    }

    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    )
    val contentColor = MaterialTheme.colorScheme.onPrimary 
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = gradientColors,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    )
                )
        ) {
            // Background Decorative Elements
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .offset(x = 200.dp, y = (-80).dp)
                    .graphicsLayer(alpha = 0.15f)
                    .background(contentColor, RoundedCornerShape(100.dp))
            )
            
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .offset(x = (-40).dp, y = 120.dp)
                    .graphicsLayer(alpha = 0.1f)
                    .background(contentColor, RoundedCornerShape(100.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Staggered animation for the badge
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600, delayMillis = 100)) + 
                            slideInHorizontally(tween(600, delayMillis = 100)) { -it / 2 }
                ) {
                    Surface(
                        color = contentColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = contentColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CAMPUS HUB",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = contentColor,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }
                
                // Staggered animation for the headline
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600, delayMillis = 250)) + 
                            slideInHorizontally(tween(600, delayMillis = 250)) { -it / 2 }
                ) {
                    Text(
                        "Hey Sonu! 👋",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontFamily.Serif,
                            lineHeight = 36.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Staggered animation for the description
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600, delayMillis = 400)) + 
                            slideInHorizontally(tween(600, delayMillis = 400)) { -it / 2 }
                ) {
                    Text(
                        "Discover what's happening in your UniVerse.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = contentColor.copy(alpha = 0.9f),
                        lineHeight = 22.sp
                    )
                }
            }
            
            // Decorative floating icon animation
            this@Card.AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800, delayMillis = 500)) + scaleIn(tween(800, delayMillis = 500)),
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .offset(x = 30.dp, y = 30.dp)
                        .graphicsLayer(
                            alpha = 0.25f,
                            rotationZ = -25f,
                            scaleX = 1.3f,
                            scaleY = 1.3f
                        ),
                    tint = contentColor
                )
            }
        }
    }
}
