package com.evramantony.modrinth.data.repository

import com.evramantony.modrinth.data.database.ControlLayoutDao
import com.evramantony.modrinth.data.database.KeyBindingDao
import com.evramantony.modrinth.data.database.entity.ControlLayoutEntity
import com.evramantony.modrinth.data.database.entity.KeyBindingEntity
import com.evramantony.modrinth.data.model.ControlButton
import com.evramantony.modrinth.data.model.ControlLayout
import com.evramantony.modrinth.data.model.JoystickPosition
import com.evramantony.modrinth.data.model.ButtonType
import com.evramantony.modrinth.data.model.MinecraftKeyBindings
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ControlLayoutRepository(
    private val layoutDao: ControlLayoutDao,
    private val bindingDao: KeyBindingDao
) {
    private val gson = Gson()

    fun getAllLayouts(): Flow<List<ControlLayoutEntity>> = layoutDao.getAllLayouts()

    fun getActiveLayout(): Flow<ControlLayoutEntity?> = layoutDao.getActiveLayout()

    fun getCustomLayouts(): Flow<List<ControlLayoutEntity>> = layoutDao.getCustomLayouts()

    suspend fun createDefaultLayout(): ControlLayoutEntity {
        val defaultButtons = createDefaultButtons()
        val defaultLayout = ControlLayoutEntity(
            id = UUID.randomUUID().toString(),
            name = "Default Layout",
            buttonsJson = gson.toJson(defaultButtons),
            joystickLeftJson = gson.toJson(JoystickPosition(100f, 400f, 80f)),
            joystickRightJson = gson.toJson(JoystickPosition(900f, 400f, 80f)),
            opacity = 0.8f,
            scale = 1.0f,
            isCustom = false,
            isActive = true
        )
        layoutDao.insertLayout(defaultLayout)
        return defaultLayout
    }

    suspend fun createCustomLayout(name: String): ControlLayoutEntity {
        val customButtons = createDefaultButtons()
        val customLayout = ControlLayoutEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            buttonsJson = gson.toJson(customButtons),
            joystickLeftJson = gson.toJson(JoystickPosition(100f, 400f, 80f)),
            joystickRightJson = gson.toJson(JoystickPosition(900f, 400f, 80f)),
            opacity = 0.8f,
            scale = 1.0f,
            isCustom = true,
            isActive = false
        )
        layoutDao.insertLayout(customLayout)
        return customLayout
    }

    suspend fun setActiveLayout(layoutId: String) {
        layoutDao.deactivateAllLayouts()
        layoutDao.setActiveLayout(layoutId)
    }

    suspend fun updateLayout(layout: ControlLayoutEntity) {
        layoutDao.updateLayout(
            layout.copy(lastModified = System.currentTimeMillis())
        )
    }

    suspend fun deleteLayout(layoutId: String) {
        val layout = layoutDao.getLayoutById(layoutId)
        if (layout != null) {
            bindingDao.deleteBindingsForLayout(layoutId)
            layoutDao.deleteLayout(layout)
        }
    }

    suspend fun updateButtonInLayout(layoutId: String, button: ControlButton) {
        val layout = layoutDao.getLayoutById(layoutId) ?: return
        val buttons = gson.fromJson(layout.buttonsJson, Array<ControlButton>::class.java).toMutableList()
        val index = buttons.indexOfFirst { it.id == button.id }
        if (index >= 0) {
            buttons[index] = button
            layoutDao.updateLayout(
                layout.copy(
                    buttonsJson = gson.toJson(buttons),
                    lastModified = System.currentTimeMillis()
                )
            )
        }
    }

    private fun createDefaultButtons(): List<ControlButton> {
        val buttons = mutableListOf<ControlButton>()
        var yOffset = 300

        // Movement D-pad (left side)
        buttons.add(ControlButton(
            id = "btn_w",
            label = "W",
            keyCode = MinecraftKeyBindings.KEY_W,
            x = 80f,
            y = yOffset.toFloat(),
            width = 60f,
            height = 60f,
            type = ButtonType.MOVEMENT
        ))

        buttons.add(ControlButton(
            id = "btn_a",
            label = "A",
            keyCode = MinecraftKeyBindings.KEY_A,
            x = 20f,
            y = yOffset + 60f,
            width = 60f,
            height = 60f,
            type = ButtonType.MOVEMENT
        ))

        buttons.add(ControlButton(
            id = "btn_s",
            label = "S",
            keyCode = MinecraftKeyBindings.KEY_S,
            x = 80f,
            y = yOffset + 60f,
            width = 60f,
            height = 60f,
            type = ButtonType.MOVEMENT
        ))

        buttons.add(ControlButton(
            id = "btn_d",
            label = "D",
            keyCode = MinecraftKeyBindings.KEY_D,
            x = 140f,
            y = yOffset + 60f,
            width = 60f,
            height = 60f,
            type = ButtonType.MOVEMENT
        ))

        // Action buttons (right side)
        yOffset = 300
        var xOffset = 1000

        buttons.add(ControlButton(
            id = "btn_jump",
            label = "JUMP",
            keyCode = MinecraftKeyBindings.KEY_SPACE,
            x = xOffset.toFloat(),
            y = yOffset.toFloat(),
            width = 80f,
            height = 50f,
            type = ButtonType.JUMP,
            color = "#4ECDC4"
        ))

        buttons.add(ControlButton(
            id = "btn_sneak",
            label = "SNEAK",
            keyCode = MinecraftKeyBindings.KEY_SHIFT,
            x = xOffset.toFloat(),
            y = yOffset + 60f,
            width = 80f,
            height = 50f,
            type = ButtonType.SNEAK,
            color = "#95E1D3"
        ))

        buttons.add(ControlButton(
            id = "btn_sprint",
            label = "SPRINT",
            keyCode = MinecraftKeyBindings.KEY_CTRL,
            x = xOffset.toFloat(),
            y = yOffset + 120f,
            width = 80f,
            height = 50f,
            type = ButtonType.SPRINT,
            color = "#FF6B6B"
        ))

        // Utility buttons (bottom)
        yOffset = 500
        xOffset = 50

        buttons.add(ControlButton(
            id = "btn_inventory",
            label = "INV",
            keyCode = MinecraftKeyBindings.KEY_E,
            x = xOffset.toFloat(),
            y = yOffset.toFloat(),
            width = 70f,
            height = 50f,
            type = ButtonType.INVENTORY,
            color = "#6C63FF"
        ))

        buttons.add(ControlButton(
            id = "btn_drop",
            label = "DROP",
            keyCode = MinecraftKeyBindings.KEY_Q,
            x = xOffset + 80f,
            y = yOffset.toFloat(),
            width = 70f,
            height = 50f,
            type = ButtonType.DROP,
            color = "#F0A500"
        ))

        buttons.add(ControlButton(
            id = "btn_chat",
            label = "CHAT",
            keyCode = MinecraftKeyBindings.KEY_T,
            x = xOffset + 160f,
            y = yOffset.toFloat(),
            width = 70f,
            height = 50f,
            type = ButtonType.CHAT,
            color = "#38B6FF"
        ))

        buttons.add(ControlButton(
            id = "btn_perspective",
            label = "F5",
            keyCode = MinecraftKeyBindings.KEY_F5,
            x = xOffset + 240f,
            y = yOffset.toFloat(),
            width = 70f,
            height = 50f,
            type = ButtonType.PERSPECTIVE,
            color = "#A78BFA"
        ))

        // Hotbar buttons
        for (i in 1..9) {
            buttons.add(ControlButton(
                id = "btn_hotbar_$i",
                label = i.toString(),
                keyCode = MinecraftKeyBindings.KEY_1 + i - 1,
                x = (50 + (i - 1) * 85).toFloat(),
                y = 560f,
                width = 70f,
                height = 40f,
                type = ButtonType.HOTBAR,
                color = "#6C63FF"
            ))
        }

        // Attack/Use buttons (corners)
        buttons.add(ControlButton(
            id = "btn_attack",
            label = "ATTACK",
            keyCode = 1001,  // Left click
            x = 50f,
            y = 50f,
            width = 100f,
            height = 100f,
            type = ButtonType.ATTACK,
            color = "#FF0000"
        ))

        buttons.add(ControlButton(
            id = "btn_use",
            label = "USE",
            keyCode = 1002,  // Right click
            x = 900f,
            y = 50f,
            width = 100f,
            height = 100f,
            type = ButtonType.USE,
            color = "#00FF00"
        ))

        return buttons
    }
}
