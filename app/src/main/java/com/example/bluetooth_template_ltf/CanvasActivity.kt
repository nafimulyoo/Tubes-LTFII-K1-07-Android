package com.example.bluetooth_template_ltf

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import com.example.bluetooth_template_ltf.PaintView.Companion.colorList
import com.example.bluetooth_template_ltf.PaintView.Companion.currentBrush
import com.example.bluetooth_template_ltf.PaintView.Companion.pathList
import com.example.bluetooth_template_ltf.PaintView.Companion.isDrawing
import com.example.bluetooth_template_ltf.databinding.ActivityCanvasBinding




class CanvasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCanvasBinding

//    Butuh Kalibrasi
    private var delta_z = 0.2

    companion object {
        var path = Path()
        var paintBrush = Paint()
    }

    override fun onMessageSent(message: String) {
    }

    override fun onMessageReceived(message: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)



        val paintButton = findViewById<ImageButton>(R.id.paintButton)
        val clearButton = findViewById<ImageButton>(R.id.clearButton)
        val upButton = findViewById<Button>(R.id.upButton)
        val downButton = findViewById<Button>(R.id.downButton)
        val pointButton = findViewById<Button>(R.id.pointButton)


        paintButton.setOnClickListener {
            isDrawing = true
            paintBrush.setColor(Color.BLACK)
            currentColor(paintBrush.color)
        }

        pointButton.setOnClickListener {
            isDrawing = false
            paintBrush.setColor(Color.TRANSPARENT)
            currentColor(paintBrush.color)
        }

        clearButton.setOnClickListener {
            isDrawing = false
            pathList.clear()
            colorList.clear()
            path.reset()

        }

        upButton.setOnClickListener {

            sendBluetoothMessage("MOVE_Z 0 0 $delta_z")
        }

        downButton.setOnClickListener {
            (context as MainActivity)sendMessageToESP32("MOVE_Z 0 0 -$delta_z")
        }


    }


    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }


}
