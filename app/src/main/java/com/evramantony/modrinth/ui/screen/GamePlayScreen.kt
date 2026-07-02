package com.evramantony.modrinth.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.evramantony.modrinth.ui.viewmodel.ControlViewModel
import com.evramantony.modrinth.data.model.ControlButton
import com.evramantony.modrinth.data.model.JoystickPosition
import com.evramantony.modrinth.ui.component.ControlOverlay

@Composable
fun GamePlayScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val activeLayout by viewModel.activeLayout.collectAsState()
    val pressedButtons by viewModel.pressedButtons.collectAsState()
    val joystickLeftPos by viewModel.joystickLeftPosition.collectAsState()
    val joystickRightPos by viewModel.joystickRightPosition.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Game view would go here
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Minecraft Game View",
                color = Color.White
            )
        }

        // Control Overlay
        if (activeLayout != null) {
            ControlOverlay(
                modifier = Modifier.fillMaxSize(),
                buttons = activeLayout!!.buttons,
                opacity = activeLayout!!.opacity,
                scale = activeLayout!!.scale,
                onButtonPress = { button ->
                    viewModel.onButtonPress(button)
                },
                onButtonRelease = { button ->
                    viewModel.onButtonRelease(button)
                },
                onJoystickLeftMove = { x, y ->
                    viewModel.onJoystickLeftMove(x, y)
                },
                onJoystickRightMove = { x, y ->
                    viewModel.onJoystickRightMove(x, y)
                },
                joystickLeftPos = JoystickPosition(100f, 400f, 80f),
                joystickRightPos = JoystickPosition(900f, 400f, 80f)
            )
        }
    }
}

@Composable
fun ControlSettingsScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val allLayouts by viewModel.allLayouts.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var newLayoutName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Control Layouts",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { showCreateDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Custom Layout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        allLayouts.forEach { layout ->
            ControlLayoutCard(
                layout = layout,
                onSelect = { viewModel.setActiveLayout(layout.id) },
                onDelete = { viewModel.deleteLayout(layout.id) },
                onEdit = { /* TODO: Open editor */ }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create Layout") },
            text = {
                TextField(
                    value = newLayoutName,
                    onValueChange = { newLayoutName = it },
                    label = { Text("Layout Name") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.createCustomLayout(newLayoutName)
                    showCreateDialog = false
                    newLayoutName = ""
                }) {
                    Text("Create")
                }
            },
            dismissButton = {
                Button(onClick = { showCreateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ControlLayoutCard(
    layout: com.evramantony.modrinth.data.database.entity.ControlLayoutEntity,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = layout.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (layout.isCustom) "Custom" else "Default",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row {
                Button(onClick = onEdit) { Text("Edit") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onSelect) { Text("Select") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete) { Text("Delete") }
            }
        }
    }
}
