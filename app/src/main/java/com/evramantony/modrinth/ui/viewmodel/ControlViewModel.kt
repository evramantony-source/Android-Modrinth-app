package com.evramantony.modrinth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evramantony.modrinth.data.model.ControlButton
import com.evramantony.modrinth.data.model.ControlLayout
import com.evramantony.modrinth.data.repository.ControlLayoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val controlLayoutRepository: ControlLayoutRepository
) : ViewModel() {

    private val _activeLayout = MutableStateFlow<ControlLayout?>(null)
    val activeLayout: StateFlow<ControlLayout?> = _activeLayout.asStateFlow()

    private val _allLayouts = MutableStateFlow<List<ControlLayout>>(emptyList())
    val allLayouts: StateFlow<List<ControlLayout>> = _allLayouts.asStateFlow()

    private val _pressedButtons = MutableStateFlow<Set<String>>(emptySet())
    val pressedButtons: StateFlow<Set<String>> = _pressedButtons.asStateFlow()

    private val _joystickLeftPosition = MutableStateFlow(Pair(0f, 0f))
    val joystickLeftPosition: StateFlow<Pair<Float, Float>> = _joystickLeftPosition.asStateFlow()

    private val _joystickRightPosition = MutableStateFlow(Pair(0f, 0f))
    val joystickRightPosition: StateFlow<Pair<Float, Float>> = _joystickRightPosition.asStateFlow()

    init {
        loadLayouts()
    }

    private fun loadLayouts() {
        viewModelScope.launch {
            controlLayoutRepository.getActiveLayout().collect { layout ->
                _activeLayout.value = layout?.let {
                    ControlLayout(
                        id = it.id,
                        name = it.name,
                        buttons = emptyList(), // TODO: Parse JSON
                        joystickLeftPosition = com.evramantony.modrinth.data.model.JoystickPosition(0f, 0f, 80f),
                        joystickRightPosition = com.evramantony.modrinth.data.model.JoystickPosition(0f, 0f, 80f)
                    )
                }
            }
        }
    }

    fun onButtonPress(button: ControlButton) {
        _pressedButtons.value = _pressedButtons.value + button.id
        // TODO: Send key press to game
    }

    fun onButtonRelease(button: ControlButton) {
        _pressedButtons.value = _pressedButtons.value - button.id
        // TODO: Send key release to game
    }

    fun onJoystickLeftMove(x: Float, y: Float) {
        _joystickLeftPosition.value = Pair(x, y)
        // TODO: Update WASD keys based on position
    }

    fun onJoystickRightMove(x: Float, y: Float) {
        _joystickRightPosition.value = Pair(x, y)
        // TODO: Update mouse look based on position
    }

    fun setActiveLayout(layoutId: String) {
        viewModelScope.launch {
            controlLayoutRepository.setActiveLayout(layoutId)
        }
    }

    fun createCustomLayout(name: String) {
        viewModelScope.launch {
            controlLayoutRepository.createCustomLayout(name)
        }
    }

    fun deleteLayout(layoutId: String) {
        viewModelScope.launch {
            controlLayoutRepository.deleteLayout(layoutId)
        }
    }
}
