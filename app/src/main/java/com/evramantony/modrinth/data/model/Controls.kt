package com.evramantony.modrinth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ControlLayout(
    val id: String,
    val name: String,
    val buttons: List<ControlButton>,
    val joystickLeftPosition: JoystickPosition,
    val joystickRightPosition: JoystickPosition,
    val opacity: Float = 0.8f,
    val scale: Float = 1.0f,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

@Serializable
data class ControlButton(
    val id: String,
    val label: String,
    val keyCode: Int,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val type: ButtonType,
    val color: String = "#6C63FF",
    val isVisible: Boolean = true,
    val isHeld: Boolean = false
)

@Serializable
data class JoystickPosition(
    val x: Float,
    val y: Float,
    val radius: Float
)

enum class ButtonType {
    MOVEMENT,      // WASD
    JUMP,          // SPACE
    SNEAK,         // SHIFT
    SPRINT,        // CTRL
    ATTACK,        // Left Click
    USE,           // Right Click
    DROP,          // Q
    INVENTORY,     // E
    CREATIVE,      // F2 or creative mode
    CHAT,          // T
    HOTBAR,        // 1-9
    PERSPECTIVE,   // F5
    FULLSCREEN,    // F11
    MOUSE_LOOK,    // Right joystick
    ESCAPE         // ESC
}

@Serializable
data class KeyBinding(
    val action: String,
    val keyCode: Int,
    val isMod: Boolean = false
)

object MinecraftKeyBindings {
    const val KEY_W = 87           // Move forward
    const val KEY_A = 65           // Move left
    const val KEY_S = 83           // Move backward
    const val KEY_D = 68           // Move right
    const val KEY_SPACE = 32       // Jump
    const val KEY_SHIFT = 16       // Sneak
    const val KEY_CTRL = 17        // Sprint
    const val KEY_Q = 81           // Drop
    const val KEY_E = 69           // Inventory
    const val KEY_T = 84           // Chat
    const val KEY_F5 = 244         // Perspective
    const val KEY_F11 = 252        // Fullscreen
    const val KEY_ESC = 27         // Close GUI
    const val KEY_1 = 49           // Hotbar 1
    const val KEY_9 = 57           // Hotbar 9
}
