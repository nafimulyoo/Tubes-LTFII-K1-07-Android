package com.example.tugasbesar_ltfii_k1_07.ui.digital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.R
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentDigitalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32


class DigitalFragment : Fragment() {

    private var _binding: FragmentDigitalBinding? = null
    private val binding get() = _binding!!

    // Configuration variables
    val dpadSpeedXY = esp32.settings.dpadSpeedXY
    val dpadSpeedZ = esp32.settings.dpadSpeedZ
    val dpadInterval = esp32.settings.dpadUpdateInterval

    // Calculated variables
    val dpadStepXY = dpadSpeedXY * dpadInterval / 1000f
    val dpadStepZ = dpadSpeedZ * dpadInterval / 1000f


    // Coroutine Job for managing loops
    private var dpadJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDigitalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buttonLeft = view.findViewById<ImageButton>(R.id.button_left)
        val buttonRight = view.findViewById<ImageButton>(R.id.button_right)
        val buttonBackward = view.findViewById<ImageButton>(R.id.button_backward)
        val buttonForward = view.findViewById<ImageButton>(R.id.button_forward)
        val buttonUp = view.findViewById<ImageButton>(R.id.button_up)
        val buttonDown = view.findViewById<ImageButton>(R.id.button_down)

        setTouchListener(buttonUp, 0f, 0f, dpadStepZ)
        setTouchListener(buttonDown, 0f, 0f, -dpadStepXY)
        setTouchListener(buttonLeft, 0f, -dpadStepXY, 0f)
        setTouchListener(buttonRight, 0f, dpadStepXY, 0f)
        setTouchListener(buttonForward, dpadStepXY, 0f, 0f)
        setTouchListener(buttonBackward, -dpadStepXY, 0f, 0f)
    }

    private fun setTouchListener(button: ImageButton, dx: Float, dy: Float, dz: Float) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dpadJob?.cancel()
                    dpadJob = CoroutineScope(Dispatchers.Main).launch {
                        while (true) {
                            // Replace sendMessage with your actual sendMessage function
                            esp32.sendMessage("JOYSTICK $dx $dy $dz")
                            delay(dpadInterval)
                        }
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    dpadJob?.cancel()
                }
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
