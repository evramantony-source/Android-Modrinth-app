package com.evramantony.modrinthapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.evramantony.modrinthapp.presentation.ui.screens.HomeScreen
import com.evramantony.modrinthapp.presentation.ui.screens.ModBrowserScreen
import com.evramantony.modrinthapp.presentation.ui.screens.InstanceManagerScreen
import com.evramantony.modrinthapp.presentation.ui.screens.AccountManagerScreen
import com.evramantony.modrinthapp.presentation.ui.screens.SettingsScreen
import com.evramantony.modrinthapp.presentation.ui.theme.ModrinthAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModrinthAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("mods") {
                            ModBrowserScreen(navController)
                        }
                        composable("instances") {
                            InstanceManagerScreen(navController)
                        }
                        composable("accounts") {
                            AccountManagerScreen(navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
