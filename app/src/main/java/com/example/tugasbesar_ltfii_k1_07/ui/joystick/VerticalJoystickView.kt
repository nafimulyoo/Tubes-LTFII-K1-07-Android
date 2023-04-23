package com.example.tugasbesar_ltfii_k1_07.ui.joystick

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VerticalJoystickView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val placeholderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 4f
    }
    private val joystickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#81a984") // moss_green_200
    }

    private val center = PointF()
    private val joystick = PointF()
    private var radius: Float = 0f
    private var joystickRadius: Float = 0f
    private val slotRect = RectF()
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

        val slotWidth = radius * 0.4f
        val slotHeight = radius * 3f
        slotRect.set(
            center.x - slotWidth * 0.5f,
            center.y - slotHeight * 0.5f,
            center.x + slotWidth * 0.5f,
            center.y + slotHeight * 0.5f
        )

        updateJoystick()
    }

    private fun updateJoystick() {
        joystick.set(center.x, center.y)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(slotRect, slotRect.width() / 2, slotRect.width() / 2, placeholderPaint)
        canvas.drawCircle(joystick.x, joystick.y, joystickRadius, joystickPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val dy = event.y - center.y
                val distance = Math.abs(dy)

                if (distance < radius) {
                    joystick.set(center.x, event.y)
                } else {
                    joystick.set(center.x, center.y + Math.signum(dy) * radius)
                }
                if (distance < radius) {
                    val z = dy / radius
                    onMoveListener?.onMove(z)
                } else {
                    if (dy > 0) {
                        onMoveListener?.onMove(1f)
                    } else {
                        onMoveListener?.onMove(-1f)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                updateJoystick()
                onMoveListener?.onMove(0f)
            }
        }
        invalidate()
        return true
    }

    interface OnMoveListener {
        fun onMove(position: Float)
    }
}
