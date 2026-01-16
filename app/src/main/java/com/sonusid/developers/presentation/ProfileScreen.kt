package com.sonusid.developers.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonusid.developers.ui.theme.UniVerseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBackClick: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
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
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                ProfileHeader()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                StatsSection()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                SectionLabel("Account Settings")
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(accountSettings) { item ->
                SettingsItem(item)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                SectionLabel("Preferences")
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(preferences) { item ->
                SettingsItem(item)
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                LogoutButton()
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            IconButton(
                onClick = { /* Edit photo */ },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Sonu Sid",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold
            )
        )
        
        Text(
            "Computer Science Student",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            shape = RoundedCornerShape(100.dp)
        ) {
            Text(
                "Level 12 • 2,450 XP",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("Events", "12")
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        StatItem("Points", "450")
        VerticalDivider(
            modifier = Modifier
                .height(40.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        StatItem("Friends", "89")
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun SectionLabel(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
    }
}

data class SettingsItemData(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(item: SettingsItemData) {
    Surface(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    item.icon,
                    contentDescription = null,
                    tint = item.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                item.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun LogoutButton() {
    Button(
        onClick = { /* Logout */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            "Logout",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

val accountSettings = listOf(
    SettingsItemData("Personal Info", Icons.Default.PersonOutline, Color(0xFF2196F3)),
    SettingsItemData("My QR Code", Icons.Default.QrCode, Color(0xFF4CAF50)),
    SettingsItemData("Payment Methods", Icons.Default.CreditCard, Color(0xFFFF9800))
)

val preferences = listOf(
    SettingsItemData("Notifications", Icons.Default.NotificationsNone, Color(0xFF9C27B0)),
    SettingsItemData("Dark Mode", Icons.Default.DarkMode, Color(0xFF607D8B)),
    SettingsItemData("Language", Icons.Default.Language, Color(0xFFE91E63))
)

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    UniVerseTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreen()
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreviewDark() {
    UniVerseTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreen()
        }
    }
}
