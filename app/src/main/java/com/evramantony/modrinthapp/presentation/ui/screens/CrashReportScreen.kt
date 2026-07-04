package com.evramantony.modrinthapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CrashReportScreen(
    crashId: Int,
    navController: NavController
) {
    var crashMessage by remember { mutableStateOf("OutOfMemoryError: Java heap space") }
    var errorType by remember { mutableStateOf("java.lang.OutOfMemoryError") }
    var suggestions by remember {
        mutableStateOf(
            listOf(
                "Increase allocated RAM in launcher settings",
                "Remove unnecessary mods",
                "Update mods to latest versions"
            )
        )
    }
    var affectedMods by remember { mutableStateOf(listOf("Mod1", "Mod2")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crash Report") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Crash Detected",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        crashMessage,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Error Type: $errorType",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Affected Mods
            if (affectedMods.isNotEmpty()) {
                Text(
                    "Potentially Affected Mods",
                    style = MaterialTheme.typography.titleSmall
                )
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        affectedMods.forEach { mod ->
                            Text("• $mod", modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }

            // Suggestions
            Text(
                "Suggestions to Fix",
                style = MaterialTheme.typography.titleSmall
            )
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    suggestions.forEachIndexed { index, suggestion ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("${index + 1}. ")
                            Text(suggestion, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Full Log")
                }
            }
        }
    }
}
