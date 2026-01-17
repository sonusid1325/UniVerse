package com.sonusid.developers.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class NavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavItem("home", Icons.Default.Home, "Home")
    object Events : NavItem("events", Icons.Default.CalendarToday, "Events")
    object Scan : NavItem("scan", Icons.Default.QrCodeScanner, "Scan")
    object Community : NavItem("community", Icons.Default.Groups, "Hubs")
    object Profile : NavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun ExpressiveNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NavItem.Home,
        NavItem.Events,
        NavItem.Scan,
        NavItem.Community,
        NavItem.Profile
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 8.dp,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                
                ExpressiveNavItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
fun ExpressiveNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val transition = updateTransition(isSelected, label = "navItemTransition")
    
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    
    val color by transition.animateColor(label = "color") { selected ->
        if (selected) selectedColor else unselectedColor
    }
    
    val scale by transition.animateFloat(
        label = "scale",
        transitionSpec = {
            if (false isTransitioningTo true) {
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }
    ) { selected -> if (selected) 1.1f else 1f }

    val indicatorWidth by transition.animateDp(label = "indicatorWidth") { selected ->
        if (selected) 40.dp else 0.dp
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Background Indicator Glow
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(width = 44.dp, height = 44.dp)
                            .background(
                                color = selectedColor.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                    )
                }
                
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = color,
                    modifier = Modifier
                        .size(26.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 10.sp
                ),
                color = color
            )
            
            // Minimal Dot Indicator
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(indicatorWidth)
                    .height(2.dp)
                    .background(
                        color = if (isSelected) selectedColor else Color.Transparent,
                        shape = CircleShape
                    )
            )
        }
    }
}
