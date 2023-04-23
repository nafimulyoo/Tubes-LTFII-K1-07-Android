package com.example.tugasbesar_ltfii_k1_07.ui.joystick

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class JoystickView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val placeholderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 4f
    }
    private val joystickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#81a984")
    }

    private val center = PointF()
    private val joystick = PointF()
    private var radius: Float = 0f
    private var joystickRadius: Float = 0f
    private var onMoveListener: OnMoveListener? = null

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null) // Required for some devices to display transparent background
    }

    fun setOnMoveListener(listener: OnMoveListener) {
        onMoveListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.set(w * 0.5f, h * 0.5f)
        radius = Math.min(w, h) * 0.25f
        joystickRadius = radius * 0.4f
        updateJoystick()
    }

    private fun updateJoystick() {
        joystick.set(center.x, center.y)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, radius, placeholderPaint)
        canvas.drawCircle(joystick.x, joystick.y, joystickRadius, joystickPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val dx = event.x - center.x
                val dy = -(event.y - center.y)
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                if (distance < radius) {
                    joystick.set(event.x, event.y)
                } else {
                    val ratio = radius / distance
                    joystick.set(center.x + dx * ratio, center.y - dy * ratio)
                }

                // Calculate x and y in the range of -1 to 1
                // don't exceed 1 or -1
                val x = Math.min(1f, Math.max(-1f, -(joystick.x - center.x) / radius))
                val y = Math.min(1f, Math.max(-1f, (joystick.y - center.y) / radius))

                onMoveListener?.onMove(x, y)
            }
            MotionEvent.ACTION_UP -> {
                updateJoystick()
                onMoveListener?.onMove(0f, 0f)
            }
        }
        invalidate()
        return true
    }


    interface OnMoveListener {
        fun onMove(x: Float, y: Float)
    }
}
