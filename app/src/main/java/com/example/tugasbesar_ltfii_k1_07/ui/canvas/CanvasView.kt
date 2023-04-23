package com.example.tugasbesar_ltfii_k1_07.ui.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.tugasbesar_ltfii_k1_07.MainActivity
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasFragment.Companion.paintBrush
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasFragment.Companion.path
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32


class PaintView : View {

        var params : ViewGroup.LayoutParams? = null

        companion object {
            var pathList = ArrayList<Path>()
            var colorList = ArrayList<Int>()
            var currentBrush = Color.BLACK;
            var isDrawing = true
        }

        constructor(context: Context) : this(context, null) {
            setupDrawing()
        }

        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
            setupDrawing()
        }

        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
            setupDrawing()
        }



        private fun setupDrawing() {
            paintBrush.isAntiAlias = true
            paintBrush.color = currentBrush
            paintBrush.style = Paint.Style.STROKE
            paintBrush.strokeJoin = Paint.Join.ROUND
            paintBrush.strokeWidth = 8f;

            params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                if (isDrawing) {
                    esp32.sendMessage("CANVAS PEN_DOWN $x $y 0")
                }
                else {
                    esp32.sendMessage("CANVAS MOVE_XY $x $y 0")
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
                esp32.sendMessage("CANVAS MOVE_XY $x $y 0")
            }

            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
                esp32.sendMessage("CANVAS PEN_UP $x $y 0")
            }

            else -> return false
        }
        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas) {

        for (i in pathList.indices) {
            paintBrush.setColor(colorList[i])
            canvas.drawPath(pathList[i], paintBrush)
            invalidate()
        }
    }

}