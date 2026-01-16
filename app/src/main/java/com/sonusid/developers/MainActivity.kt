package com.sonusid.developers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sonusid.developers.presentation.HomeScreen
import com.sonusid.developers.presentation.ProfileScreen
import com.sonusid.developers.ui.theme.UniVerseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf("home") }
            
            UniVerseTheme {
                if (currentScreen == "home") {
                    HomeScreen(onProfileClick = { currentScreen = "profile" })
                } else {
                    ProfileScreen(onBackClick = { currentScreen = "home" })
                }
            }
        }
    }
}
