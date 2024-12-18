package com.example.tp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.tp.navigation.TaskNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.example.tp.ui.theme.TaskAppTheme
import com.example.tp.ui.theme.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            TaskAppTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                TaskNavHost(navController = navController)
            }
        }
    }
}