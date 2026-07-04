package com.evramantony.modrinthapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modrinth Launcher") },
                actions = {
                    IconButton(onClick = { navController.navigate("accounts") }) {
                        Icon(Icons.Filled.Person, "Accounts")
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Minecraft Java Edition Launcher")
            
            Button(onClick = { navController.navigate("instances") }) {
                Text("Game Instances")
            }
            
            Button(onClick = { navController.navigate("mods") }) {
                Text("Browse Mods")
            }
            
            Button(onClick = { navController.navigate("modpacks") }) {
                Text("Browse Modpacks")
            }
            
            Button(onClick = { navController.navigate("resourcepacks") }) {
                Text("Browse Resource Packs")
            }
            
            Button(onClick = { navController.navigate("shaderpacks") }) {
                Text("Browse Shader Packs")
            }
            
            Button(onClick = { navController.navigate("datapacks") }) {
                Text("Browse Data Packs")
            }
        }
    }
}
