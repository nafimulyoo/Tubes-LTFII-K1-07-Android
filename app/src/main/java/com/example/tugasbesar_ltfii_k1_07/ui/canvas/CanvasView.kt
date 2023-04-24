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
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasFragment.Companion.paintBrush
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasFragment.Companion.path
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


class CanvasView : View {

    var params : ViewGroup.LayoutParams? = null

    companion object {
        var pathList = ArrayList<Path>()
        var colorList = ArrayList<Int>()
        var currentBrush = Color.BLACK;
        var isDrawing = true
        var canvasWidth: Int = 0
        var canvasHeight: Int = 0
    }

    private val moveThrottleFlow = MutableSharedFlow<Pair<Float, Float>>(extraBufferCapacity = 1)
    private var throttleJob: Job? = null

    init {
        throttleJob = CoroutineScope(Dispatchers.Main).launch {
            moveThrottleFlow
                .debounce(timeoutMillis = esp32.settings.canvasTimeout) // Atur waktu tunda dalam milidetik sesuai kebutuhan
                .collect { (paperX, paperY) ->
                    esp32.sendMessage("CANVAS MOVE_XY $paperX $paperY 0")
                }
        }
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasWidth = w
        canvasHeight = h

        params = ViewGroup.LayoutParams(canvasWidth, canvasHeight)
        layoutParams = params
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x.coerceIn(0f, canvasWidth.toFloat())
        var y = event.y.coerceIn(0f, canvasHeight.toFloat())

        val paperCoordinates = screenToPaperCoordinates(x, y)
        val paperX = paperCoordinates.first
        val paperY = paperCoordinates.second

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                if (isDrawing) {
                    esp32.sendMessage("CANVAS PEN_DOWN $paperX $paperY 0")
                }
                else {
                    esp32.sendMessage("CANVAS MOVE_XY $paperX $paperY 0")
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
                moveThrottleFlow.tryEmit(Pair(paperX, paperY))
            }

            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                pathList.add(path)
                colorList.add(currentBrush)
                if (isDrawing) {
                    esp32.sendMessage("CANVAS PEN_UP $paperX $paperY 0")
                }
                else {
                    esp32.sendMessage("CANVAS MOVE_XY $paperX $paperY 0")
                }
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

    private fun screenToPaperCoordinates(x: Float, y: Float): Pair<Float, Float> {
        val paperX = y * esp32.settings.canvasScalingFactor
        val paperY = x * esp32.settings.canvasScalingFactor
        return Pair(paperX, paperY)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        throttleJob?.cancel()
    }

}