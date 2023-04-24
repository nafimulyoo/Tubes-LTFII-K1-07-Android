package com.example.tugasbesar_ltfii_k1_07.ui.canvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentCanvasBinding
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.colorList
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.currentBrush
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.pathList
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.isDrawing
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import com.example.tugasbesar_ltfii_k1_07.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class CanvasFragment : Fragment() {
    private var _binding: FragmentCanvasBinding? = null
    private val binding get() = _binding!!

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


        val paintButton = binding.paintButton
        val pointButton = binding.pointButton
        val clearButton = binding.clearButton


//        Up and Down Button
        var upDownJob: Job? = null
        val dpadSpeedZ = esp32.settings.dpadSpeedZ
        val dpadInterval = esp32.settings.dpadUpdateInterval
        val dpadStepZ = dpadSpeedZ * dpadInterval / 1000f

        val upButton = binding.upButton
        val downButton = binding.downButton

        pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
        isDrawing = false
        paintBrush.color = Color.TRANSPARENT
        currentColor(paintBrush.color)
        pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)

        binding.paintButton.setOnClickListener {
            isDrawing = true
            paintBrush.color = Color.BLACK
            currentColor(paintBrush.color)
            paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
            pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
        }

        binding.pointButton.setOnClickListener {
            isDrawing = false
            paintBrush.color = Color.TRANSPARENT
            currentColor(paintBrush.color)
            pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
            paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
        }

        binding.clearButton.setOnClickListener {
            pathList.clear()
            colorList.clear()
            path.reset()
            isDrawing = false
            paintBrush.color = Color.TRANSPARENT
            currentColor(paintBrush.color)
            paintButton.backgroundTintList = resources.getColorStateList(R.color.moss_200)
            pointButton.backgroundTintList = resources.getColorStateList(R.color.moss_700)
        }



        fun setTouchListener(button: FloatingActionButton, dz: Float) {
            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        upDownJob?.cancel()
                        upDownJob = CoroutineScope(Dispatchers.Main).launch {
                            while (true) {
                                // Replace sendMessage with your actual sendMessage function
                                esp32.sendMessage("CANVAS MOVE_Z 0 0 $dz")
                                println("CANVAS MOVE_Z 0 0 $dz")
                                delay(dpadInterval)
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

        setTouchListener(binding.upButton, dpadStepZ)
        setTouchListener(binding.downButton, -dpadStepZ)

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun currentColor(color: Int) {
        currentBrush = color
        path = Path()
    }
}
