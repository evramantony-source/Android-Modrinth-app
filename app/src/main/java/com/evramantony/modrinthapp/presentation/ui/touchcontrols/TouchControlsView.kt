package com.evramantony.modrinthapp.presentation.ui.touchcontrols

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt

class TouchControlsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var touchListener: TouchControlListener? = null
    private var opacity = 0.7f
    private var size = 1.0f

    // Joystick state
    private var joystickCenterX = 0f
    private var joystickCenterY = 0f
    private var joystickRadius = 80f
    private var joystickThumbX = 0f
    private var joystickThumbY = 0f
    private var joystickActive = false

    // Button positions
    private val buttons = mutableMapOf<String, ButtonData>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    data class ButtonData(
        val name: String,
        val centerX: Float,
        val centerY: Float,
        val radius: Float,
        var isPressed: Boolean = false,
        val keyCode: Int
    )

    init {
        setupButtons()
    }

    private fun setupButtons() {
        val width = resources.displayMetrics.widthPixels.toFloat()
        val height = resources.displayMetrics.heightPixels.toFloat()
        val buttonSize = 60f * size

        // Right side buttons (WASD equivalent)
        buttons["jump"] = ButtonData("jump", width - 100f, height - 150f, buttonSize, keyCode = 32) // Space
        buttons["sneak"] = ButtonData("sneak", width - 100f, height - 50f, buttonSize, keyCode = 341) // Shift
        buttons["sprint"] = ButtonData("sprint", width - 200f, height - 100f, buttonSize, keyCode = 341) // Ctrl
        buttons["interact"] = ButtonData("interact", width - 50f, height - 100f, buttonSize, keyCode = 69) // E

        // Joystick
        joystickCenterX = 100f
        joystickCenterY = height - 150f
        joystickRadius = 80f * size
        joystickThumbX = joystickCenterX
        joystickThumbY = joystickCenterY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw joystick
        drawJoystick(canvas)

        // Draw buttons
        buttons.values.forEach { button ->
            drawButton(canvas, button)
        }
    }

    private fun drawJoystick(canvas: Canvas) {
        paint.color = 0x88FFFFFF.toInt()
        paint.alpha = (255 * opacity).toInt()

        // Draw joystick base circle
        canvas.drawCircle(joystickCenterX, joystickCenterY, joystickRadius, paint)

        // Draw joystick thumb
        paint.color = 0xAAFFFFFF.toInt()
        paint.alpha = (255 * opacity).toInt()
        canvas.drawCircle(joystickThumbX, joystickThumbY, joystickRadius / 2, paint)
    }

    private fun drawButton(canvas: Canvas, button: ButtonData) {
        paint.color = if (button.isPressed) 0xFFFFFFFF.toInt() else 0x88FFFFFF.toInt()
        paint.alpha = (255 * opacity).toInt()
        canvas.drawCircle(button.centerX, button.centerY, button.radius, paint)

        paint.color = 0xFF000000.toInt()
        paint.textSize = 20f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(button.name.first().toString(), button.centerX, button.centerY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y

                // Check joystick
                val joystickDistance = sqrt(
                    (x - joystickCenterX) * (x - joystickCenterX) +
                    (y - joystickCenterY) * (y - joystickCenterY)
                )

                if (joystickDistance <= joystickRadius) {
                    joystickActive = true
                    joystickThumbX = x.coerceIn(
                        joystickCenterX - joystickRadius,
                        joystickCenterX + joystickRadius
                    )
                    joystickThumbY = y.coerceIn(
                        joystickCenterY - joystickRadius,
                        joystickCenterY + joystickRadius
                    )

                    val moveX = (joystickThumbX - joystickCenterX) / joystickRadius
                    val moveY = (joystickThumbY - joystickCenterY) / joystickRadius
                    touchListener?.onJoystickMoved(moveX, moveY)
                    invalidate()
                    return true
                }

                // Check buttons
                for (button in buttons.values) {
                    val distance = sqrt(
                        (x - button.centerX) * (x - button.centerX) +
                        (y - button.centerY) * (y - button.centerY)
                    )
                    if (distance <= button.radius) {
                        button.isPressed = true
                        touchListener?.onButtonPressed(button.name)
                        invalidate()
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                joystickActive = false
                joystickThumbX = joystickCenterX
                joystickThumbY = joystickCenterY

                buttons.values.forEach { button ->
                    if (button.isPressed) {
                        touchListener?.onButtonReleased(button.name)
                        button.isPressed = false
                    }
                }
                invalidate()
                return true
            }
        }
        return true
    }

    fun setTouchControlListener(listener: TouchControlListener) {
        this.touchListener = listener
    }

    fun setOpacity(opacity: Float) {
        this.opacity = opacity.coerceIn(0f, 1f)
        invalidate()
    }

    fun setSize(size: Float) {
        this.size = size.coerceIn(0.5f, 2f)
        setupButtons()
        invalidate()
    }

    interface TouchControlListener {
        fun onJoystickMoved(x: Float, y: Float)
        fun onButtonPressed(buttonName: String)
        fun onButtonReleased(buttonName: String)
    }
}
