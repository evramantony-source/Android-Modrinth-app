package com.evramantony.modrinthapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ModBrowserScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSource by remember { mutableStateOf("Modrinth") }
    var selectedLoader by remember { mutableStateOf("Fabric") }
    var selectedGameVersion by remember { mutableStateOf("1.20.1") }
    var mods by remember { mutableStateOf(listOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse Mods") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search mods") },
                leadingIcon = { Icon(Icons.Filled.Search, "Search") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Filters
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Modrinth", "CurseForge").forEach { source ->
                    FilterChip(
                        selected = selectedSource == source,
                        onClick = { selectedSource = source },
                        label = { Text(source, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Fabric", "Forge", "Quilt").forEach { loader ->
                    FilterChip(
                        selected = selectedLoader == loader,
                        onClick = { selectedLoader = loader },
                        label = { Text(loader, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            // Mod list
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (mods.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No mods found. Try searching!")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mods) { mod ->
                        ModCard(
                            modName = mod,
                            onInstall = { }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModCard(
    modName: String,
    onInstall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(modName, style = MaterialTheme.typography.titleSmall)
                Text("Author • Downloads • Updated", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onInstall) {
                Icon(Icons.Filled.FileDownload, "Install")
            }
        }
    }
}
