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

    private var dx = 0f
    private var dy = 0f
    private var dz = 0f
    private var dpadJob: Job? = null

    private lateinit var buttonLeft: ImageButton
    private lateinit var buttonRight: ImageButton
    private lateinit var buttonBackward: ImageButton
    private lateinit var buttonForward: ImageButton
    private lateinit var buttonUp: ImageButton
    private lateinit var buttonDown: ImageButton


    private val pressedButtons = mutableListOf<ImageButton>()

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


        buttonLeft = view.findViewById<ImageButton>(R.id.button_left)
        buttonRight = view.findViewById<ImageButton>(R.id.button_right)
        buttonBackward = view.findViewById<ImageButton>(R.id.button_backward)
        buttonForward = view.findViewById<ImageButton>(R.id.button_forward)
        buttonUp = view.findViewById<ImageButton>(R.id.button_up)
        buttonDown = view.findViewById<ImageButton>(R.id.button_down)

        setTouchListener(buttonUp)
        setTouchListener(buttonDown)
        setTouchListener(buttonLeft)
        setTouchListener(buttonRight)
        setTouchListener(buttonForward)
        setTouchListener(buttonBackward)
    }

    private fun setTouchListener(button: ImageButton) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressedButtons.add(button)
                    updateDxDyDz()
                    startDpadJob()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    pressedButtons.remove(button)
                    updateDxDyDz()

                    if (pressedButtons.isEmpty()) {
                        stopDpadJob()
                    } else {
                        // Do nothing
                    }
                }
            }
            true
        }
    }


    private fun updateDxDyDz() {
        dx = 0f
        dy = 0f
        dz = 0f

        for (button in pressedButtons) {
            when (button) {
                buttonUp -> dz += dpadStepZ
                buttonDown -> dz -= dpadStepZ
                buttonLeft -> dx -= dpadStepXY
                buttonRight -> dx += dpadStepXY
                buttonForward -> dy += dpadStepXY
                buttonBackward -> dy -= dpadStepXY
            }
        }
    }

    private fun startDpadJob() {
        dpadJob?.cancel()
        dpadJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                esp32.sendMessage("JOYSTICK $dx $dy $dz")
                delay(dpadInterval)
            }
        }
    }

    private fun stopDpadJob() {
        dpadJob?.cancel()
        dpadJob = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dpadJob?.cancel()
        _binding = null
    }
}
