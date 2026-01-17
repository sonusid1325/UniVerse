package com.sonusid.developers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.sonusid.developers.presentation.UniVerseNavHost
import com.sonusid.developers.ui.theme.UniVerseTheme
import com.sonusid.developers.viewmodels.EventViewModel

class MainActivity : ComponentActivity() {
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            UniVerseTheme {
                UniVerseNavHost(
                    navController = navController,
                    eventViewModel = eventViewModel
                )
            }
        }
    }
}
