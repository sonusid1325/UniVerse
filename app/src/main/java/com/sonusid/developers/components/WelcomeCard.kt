package com.sonusid.developers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeCard() {
    // Using MaterialTheme.colorScheme to determine the right content color
    val backgroundColor1 = MaterialTheme.colorScheme.primary
    val backgroundColor2 = MaterialTheme.colorScheme.tertiary
    val contentColor = MaterialTheme.colorScheme.onPrimary // This automatically flips for Dark/Light
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            backgroundColor1,
                            backgroundColor2
                        )
                    )
                )
        ) {
            // Expressive Decorative Elements (MD3 style)
            // Using contentColor with alpha ensures visibility in both themes
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .offset(x = 220.dp, y = (-60).dp)
                    .graphicsLayer(alpha = 0.12f)
                    .background(contentColor, RoundedCornerShape(100.dp))
            )
            
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(x = (-30).dp, y = 110.dp)
                    .graphicsLayer(alpha = 0.08f)
                    .background(contentColor, RoundedCornerShape(100.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Category Chip using Surface for MD3 compliance
                Surface(
                    color = contentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = contentColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CAMPUS HUB",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = contentColor,
                            letterSpacing = 1.2.sp
                        )
                    }
                }
                
                Text(
                    "Hey Sonu! 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif, // Claude-like elegant font
                        lineHeight = 36.sp,
                        letterSpacing = (-0.5).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    "Discover what's happening in your UniVerse.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = contentColor.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            }
            
            // Large floating icon for expressive visual
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
                    .graphicsLayer(
                        alpha = 0.2f,
                        rotationZ = -20f,
                        scaleX = 1.2f,
                        scaleY = 1.2f
                    ),
                tint = contentColor
            )
        }
    }
}
