package com.evramantony.modrinthapp.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InstanceManagerScreen(navController: NavController) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var instances by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Instances") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(Icons.Filled.Add, "New Instance")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Filled.Add, "Create Instance")
            }
        }
    ) { innerPadding ->
        if (instances.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No instances created yet", style = MaterialTheme.typography.headlineSmall)
                Text("Tap + to create your first instance", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(instances) { instance ->
                    InstanceCard(
                        instanceName = instance,
                        onEdit = { },
                        onDelete = { instances = instances - instance },
                        onLaunch = { }
                    )
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateInstanceDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name ->
                instances = instances + name
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun InstanceCard(
    instanceName: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onLaunch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(instanceName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onLaunch,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Launch")
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, "Delete")
                }
            }
        }
    }
}

@Composable
fun CreateInstanceDialog(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    var instanceName by remember { mutableStateOf("") }
    var gameVersion by remember { mutableStateOf("") }
    var javaVersion by remember { mutableStateOf("Java 8") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Instance") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = instanceName,
                    onValueChange = { instanceName = it },
                    label = { Text("Instance Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gameVersion,
                    onValueChange = { gameVersion = it },
                    label = { Text("Minecraft Version") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Java version dropdown
                Text("Java Version: $javaVersion")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (instanceName.isNotEmpty() && gameVersion.isNotEmpty()) {
                        onCreate(instanceName)
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
