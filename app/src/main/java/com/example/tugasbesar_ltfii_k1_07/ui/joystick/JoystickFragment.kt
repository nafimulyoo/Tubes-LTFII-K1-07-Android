package com.example.tugasbesar_ltfii_k1_07.ui.joystick

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentJoystickBinding

class JoystickFragment : Fragment() {

    private var _binding: FragmentJoystickBinding? = null
    private val binding get() = _binding!!

    private val joystickUpdateInterval = esp32.settings.joystickUpdateInterval // Interval for joystick movement to be sent
    private val joystickThreshold = esp32.settings.joystickThreshold // Threshold for joystick movement
    private val joystickSensitivity = esp32.settings.joystickSensitivity // Sensitivity for joystick movement

    private val handler = Handler(Looper.getMainLooper())
    private var joystickRunnable: Runnable? = null
    private var dx = 0f
    private var dy = 0f
    private var dz = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoystickBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textJoystick

        val joystickView: JoystickView = binding.joystickView
        joystickView.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(dx: Float, dy: Float) {
                if (Math.pow(dx.toDouble(), 2.0) + Math.pow(dy.toDouble(), 2.0) > Math.pow(joystickThreshold.toDouble(), 2.0)) {
                    this@JoystickFragment.dx = dx * joystickSensitivity
                    this@JoystickFragment.dy = dy * joystickSensitivity
                } else {
                    this@JoystickFragment.dx = 0f
                    this@JoystickFragment.dy = 0f
                }
                startJoystickRunnable()
            }
        })

        val verticalJoystickView: VerticalJoystickView = binding.verticalJoystickView
        verticalJoystickView.setOnMoveListener(object : VerticalJoystickView.OnMoveListener {
            override fun onMove(dz: Float) {
                if (Math.abs(dz) > joystickThreshold) {
                    this@JoystickFragment.dz = dz * joystickSensitivity
                } else {
                    this@JoystickFragment.dz = 0f
                }
                startJoystickRunnable()
            }
        })

        return root
    }

    private fun startJoystickRunnable() {
        if (joystickRunnable == null) {
            joystickRunnable = object : Runnable {
                override fun run() {
                    if (dx != 0f || dy != 0f || dz != 0f) {
                        esp32.sendMessage("JOYSTICK $dx $dy $dz")
                        println("JOYSTICK $dx $dy $dz")
                        handler.postDelayed(this, joystickUpdateInterval)
                    } else {
                        joystickRunnable = null
                    }
                }
            }
            handler.post(joystickRunnable!!)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}
