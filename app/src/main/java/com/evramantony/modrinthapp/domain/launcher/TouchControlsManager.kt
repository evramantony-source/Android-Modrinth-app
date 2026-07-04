package com.evramantony.modrinthapp.domain.launcher

interface TouchControlsManager {
    fun enableTouchControls(processId: Int): Boolean
    fun disableTouchControls(): Boolean
    fun injectMouseInput(x: Float, y: Float)
    fun injectKeyPress(keyCode: Int)
    fun injectKeyRelease(keyCode: Int)
    fun setControlsLayout(layout: ControlsLayout)
}

enum class ControlsLayout {
    JOYSTICK_ONLY,
    BUTTONS_ONLY,
    HYBRID,
    CUSTOM
}
