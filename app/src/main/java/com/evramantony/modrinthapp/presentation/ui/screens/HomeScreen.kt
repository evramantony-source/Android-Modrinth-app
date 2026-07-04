package com.evramantony.modrinthapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Modrinth Launcher")
            
            Button(onClick = { navController.navigate("instances") }) {
                Text("Instances")
            }
            
            Button(onClick = { navController.navigate("mods") }) {
                Text("Browse Mods")
            }
            
            Button(onClick = { navController.navigate("accounts") }) {
                Text("Accounts")
            }
            
            Button(onClick = { navController.navigate("settings") }) {
                Text("Settings")
            }
        }
    }
}
