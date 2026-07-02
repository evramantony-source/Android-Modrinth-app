package com.evramantony.modrinth.ui.component

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evramantony.modrinth.data.model.ControlButton
import com.evramantony.modrinth.data.model.JoystickPosition
import kotlin.math.sqrt

@Composable
fun VirtualJoystick(
    modifier: Modifier = Modifier,
    position: JoystickPosition,
    radius: Float = 80f,
    onPositionChange: (x: Float, y: Float) -> Unit
) {
    val innerRadius = radius / 2
    var thumbOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size((radius * 2).dp)
            .background(Color.Gray.copy(alpha = 0.2f), CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        val newX = thumbOffset.x + dragAmount.x
                        val newY = thumbOffset.y + dragAmount.y
                        
                        val distance = sqrt(newX * newX + newY * newY)
                        if (distance <= radius) {
                            thumbOffset = Offset(newX, newY)
                        } else {
                            val ratio = radius / distance
                            thumbOffset = Offset(newX * ratio, newY * ratio)
                        }
                        
                        onPositionChange(
                            thumbOffset.x / radius,
                            thumbOffset.y / radius
                        )
                    },
                    onDragEnd = {
                        thumbOffset = Offset.Zero
                        onPositionChange(0f, 0f)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Outer circle
        Box(
            modifier = Modifier
                .size((radius * 2).dp)
                .background(Color.Transparent, CircleShape)
        )

        // Inner joystick thumb
        Box(
            modifier = Modifier
                .size((innerRadius).dp)
                .background(Color.Blue, CircleShape)
                .offset(x = (thumbOffset.x / 2).dp, y = (thumbOffset.y / 2).dp)
        )
    }
}

@Composable
fun TouchButton(
    modifier: Modifier = Modifier,
    button: ControlButton,
    isPressed: Boolean = false,
    onPress: () -> Unit,
    onRelease: () -> Unit,
    onLongPress: () -> Unit = {}
) {
    val density = LocalDensity.current
    val buttonColor = try {
        Color(android.graphics.Color.parseColor(button.color))
    } catch (e: Exception) {
        Color(0xFF6C63FF)
    }

    Button(
        modifier = modifier
            .width(button.width.dp)
            .height(button.height.dp)
            .pointerInput(button.id) {
                detectDragGestures(
                    onPress = {
                        onPress()
                    }
                )
            },
        onClick = onPress,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPressed) buttonColor.copy(alpha = 1f) else buttonColor.copy(alpha = 0.7f),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = button.label,
            fontSize = (button.width / 3).sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ControlOverlay(
    modifier: Modifier = Modifier,
    buttons: List<ControlButton>,
    opacity: Float = 0.8f,
    scale: Float = 1.0f,
    onButtonPress: (ControlButton) -> Unit,
    onButtonRelease: (ControlButton) -> Unit,
    onJoystickLeftMove: (x: Float, y: Float) -> Unit,
    onJoystickRightMove: (x: Float, y: Float) -> Unit,
    joystickLeftPos: JoystickPosition,
    joystickRightPos: JoystickPosition
) {
    var pressedButtonId by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        // Left Joystick (Movement)
        VirtualJoystick(
            modifier = Modifier
                .offset(x = joystickLeftPos.x.dp, y = joystickLeftPos.y.dp)
                .align(Alignment.TopStart),
            position = joystickLeftPos,
            radius = joystickLeftPos.radius,
            onPositionChange = { x, y -> onJoystickLeftMove(x, y) }
        )

        // Right Joystick (Look around)
        VirtualJoystick(
            modifier = Modifier
                .offset(x = joystickRightPos.x.dp, y = joystickRightPos.y.dp)
                .align(Alignment.TopStart),
            position = joystickRightPos,
            radius = joystickRightPos.radius,
            onPositionChange = { x, y -> onJoystickRightMove(x, y) }
        )

        // Buttons
        buttons.forEach { button ->
            if (button.isVisible) {
                TouchButton(
                    modifier = Modifier
                        .offset(x = button.x.dp, y = button.y.dp)
                        .align(Alignment.TopStart),
                    button = button,
                    isPressed = pressedButtonId == button.id,
                    onPress = {
                        pressedButtonId = button.id
                        onButtonPress(button)
                    },
                    onRelease = {
                        pressedButtonId = null
                        onButtonRelease(button)
                    }
                )
            }
        }
    }
}
