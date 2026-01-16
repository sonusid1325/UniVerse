package com.sonusid.developers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sonusid.developers.presentation.HomeScreen
import com.sonusid.developers.ui.theme.UniVerseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniVerseTheme {
                HomeScreen()
            }
        }
    }
}
