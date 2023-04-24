package com.example.tugasbesar_ltfii_k1_07.ui.canvas

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import com.example.tugasbesar_ltfii_k1_07.R
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentCanvasBinding
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.colorList
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.currentBrush
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.isDrawing
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.pathList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class CanvasFragment : Fragment() {
    private var _binding: FragmentCanvasBinding? = null
    private val binding get() = _binding!!
    var upDownJob: Job? = null

    companion object {
        var path = Path()
        var paintBrush = Paint()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCanvasBinding.inflate(inflater, container, false)
        val view = binding.root

        setupButtons()
        enablePointMode()

        return view
    }

    private fun setupButtons() {
        binding.paintButton.setOnClickListener {
            enableDrawingMode()
        }

        binding.pointButton.setOnClickListener {
            enablePointMode()
        }

        binding.clearButton.setOnClickListener {
            clearCanvas()
        }

        setupTouchListener(binding.upButton, calculateDpadStepZ())
        setupTouchListener(binding.downButton, -calculateDpadStepZ())
    }

    private fun enableDrawingMode() {
        isDrawing = true
        paintBrush.color = Color.BLACK
        setCurrentColor(paintBrush.color)
        binding.paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
        binding.pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
    }

    private fun enablePointMode() {
        isDrawing = false
        paintBrush.color = Color.TRANSPARENT
        setCurrentColor(paintBrush.color)
        binding.pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
        binding.paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
    }

    private fun clearCanvas() {
        pathList.clear()
        colorList.clear()
        path.reset()
        isDrawing = false
        paintBrush.color = Color.TRANSPARENT
        setCurrentColor(paintBrush.color)
        binding.paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
        binding.pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
    }


    private fun setupTouchListener(button: FloatingActionButton, dz: Float) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val upDownJob = CoroutineScope(Dispatchers.Main).launch {
                        while (true) {
                            esp32.sendMessage("CANVAS MOVE_Z 0 0 $dz")
                            delay(esp32.settings.dpadUpdateInterval)
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    upDownJob?.cancel()
                }
            }
            true
        }
    }

    private fun calculateDpadStepZ(): Float {
        val dpadSpeedZ = esp32.settings.dpadSpeedZ
        val dpadInterval = esp32.settings.dpadUpdateInterval
        return dpadSpeedZ * dpadInterval / 1000f
    }

    override fun onDestroyView() {
        clearCanvas()
        super.onDestroyView()
        _binding = null
    }

    private fun setCurrentColor(color: Int) {
        currentBrush = color
        path = Path()
    }
}
