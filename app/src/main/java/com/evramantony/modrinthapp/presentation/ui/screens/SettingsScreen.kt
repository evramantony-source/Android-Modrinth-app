package com.evramantony.modrinthapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var defaultRam by remember { mutableStateOf(2048) }
    var maxRam by remember { mutableStateOf(4096) }
    var gameWidth by remember { mutableStateOf(1280) }
    var gameHeight by remember { mutableStateOf(720) }
    var selectedRenderer by remember { mutableStateOf("OpenGL ES") }
    var enableTouchControls by remember { mutableStateOf(true) }
    var autoUpdate by remember { mutableStateOf(true) }
    var showCrashReports by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Memory Management", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Default RAM: ${defaultRam}MB")
                    Slider(
                        value = defaultRam.toFloat(),
                        onValueChange = { defaultRam = it.toInt() },
                        valueRange = 512f..8192f,
                        steps = 7
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Maximum RAM: ${maxRam}MB")
                    Slider(
                        value = maxRam.toFloat(),
                        onValueChange = { maxRam = it.toInt() },
                        valueRange = defaultRam.toFloat()..16384f,
                        steps = 7
                    )
                }
            }

            item {
                Divider()
            }

            item {
                Text("Display Settings", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Game Resolution: ${gameWidth}x${gameHeight}")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = gameWidth.toString(),
                            onValueChange = { gameWidth = it.toIntOrNull() ?: gameWidth },
                            label = { Text("Width") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = gameHeight.toString(),
                            onValueChange = { gameHeight = it.toIntOrNull() ?: gameHeight },
                            label = { Text("Height") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Divider()
            }

            item {
                Text("Graphics", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Renderer: $selectedRenderer")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("OpenGL ES", "Vulkan", "Mobile GLUES").forEach { renderer ->
                            FilterChip(
                                selected = selectedRenderer == renderer,
                                onClick = { selectedRenderer = renderer },
                                label = { Text(renderer) }
                            )
                        }
                    }
                }
            }

            item {
                Divider()
            }

            item {
                Text("Controls", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable Touch Controls")
                    Switch(
                        checked = enableTouchControls,
                        onCheckedChange = { enableTouchControls = it }
                    )
                }
            }

            item {
                Divider()
            }

            item {
                Text("General", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Auto Update")
                    Switch(
                        checked = autoUpdate,
                        onCheckedChange = { autoUpdate = it }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Show Crash Reports")
                    Switch(
                        checked = showCrashReports,
                        onCheckedChange = { showCrashReports = it }
                    )
                }
            }

            item {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Save Settings")
                }
            }
        }
    }
}
