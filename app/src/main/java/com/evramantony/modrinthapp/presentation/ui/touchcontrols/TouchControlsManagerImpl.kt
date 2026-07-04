package com.evramantony.modrinthapp.presentation.ui.touchcontrols

import android.content.Context
import android.view.KeyEvent
import com.evramantony.modrinthapp.domain.launcher.TouchControlsManager
import com.evramantony.modrinthapp.domain.launcher.ControlsLayout

class TouchControlsManagerImpl(
    private val context: Context,
    private val touchControlsView: TouchControlsView?
) : TouchControlsManager {

    private var currentLayout = ControlsLayout.HYBRID
    private var isEnabled = false

    init {
        touchControlsView?.setTouchControlListener(object : TouchControlsView.TouchControlListener {
            override fun onJoystickMoved(x: Float, y: Float) {
                injectMouseInput(x * 50, y * 50)
            }

            override fun onButtonPressed(buttonName: String) {
                val keyCode = getKeyCodeForButton(buttonName)
                injectKeyPress(keyCode)
            }

            override fun onButtonReleased(buttonName: String) {
                val keyCode = getKeyCodeForButton(buttonName)
                injectKeyRelease(keyCode)
            }
        })
    }

    override fun enableTouchControls(processId: Int): Boolean {
        isEnabled = true
        return true
    }

    override fun disableTouchControls(): Boolean {
        isEnabled = false
        return true
    }

    override fun injectMouseInput(x: Float, y: Float) {
        if (!isEnabled) return
        try {
            val cmd = arrayOf(
                "sh", "-c",
                "input mouse move ${x.toInt()} ${y.toInt()}"
            )
            Runtime.getRuntime().exec(cmd)
        } catch (e: Exception) {
            // Log error
        }
    }

    override fun injectKeyPress(keyCode: Int) {
        if (!isEnabled) return
        try {
            val cmd = arrayOf(
                "sh", "-c",
                "input keyevent $keyCode"
            )
            Runtime.getRuntime().exec(cmd)
        } catch (e: Exception) {
            // Log error
        }
    }

    override fun injectKeyRelease(keyCode: Int) {
        // Android doesn't have separate key release events
        // This is simulated by monitoring key press duration
    }

    override fun setControlsLayout(layout: ControlsLayout) {
        currentLayout = layout
    }

    private fun getKeyCodeForButton(buttonName: String): Int {
        return when (buttonName.lowercase()) {
            "jump" -> KeyEvent.KEYCODE_SPACE
            "sneak" -> KeyEvent.KEYCODE_SHIFT_LEFT
            "sprint" -> KeyEvent.KEYCODE_CTRL_LEFT
            "interact" -> KeyEvent.KEYCODE_E
            else -> KeyEvent.KEYCODE_UNKNOWN
        }
    }
}
