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
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.colorList
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.currentBrush
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.pathList
import com.example.tugasbesar_ltfii_k1_07.ui.canvas.CanvasView.Companion.isDrawing
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import com.example.tugasbesar_ltfii_k1_07.R

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

//        TODO: Cek koordinat

        val paintButton = binding.paintButton
        val pointButton = binding.pointButton
        val clearButton = binding.clearButton
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

//        TODO: Logic
        binding.downButton.setOnClickListener {
//            esp32.sendMessage("CANVAS MOVE_Z 0 0 -${esp32.settings.canvasPaperHeight}")
        }

        binding.upButton.setOnClickListener {
//            esp32.sendMessage("CANVAS MOVE_Z 0 0 ${esp32.settings.canvasPaperHeight}")

        }

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
