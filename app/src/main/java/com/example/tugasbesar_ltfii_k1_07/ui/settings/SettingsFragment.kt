package com.example.tugasbesar_ltfii_k1_07.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.ESP32
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var esp32: ESP32

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        esp32 = ESP32.getInstance(requireContext())

        // Initialize EditTexts with the current settings values
        binding.etCanvasScalingFactor.setText(esp32.settings.canvasScalingFactor.toString())
        binding.etCanvasTimeout.setText(esp32.settings.canvasTimeout.toString())
        binding.etCanvasTogglePenStep.setText(esp32.settings.canvasTogglePenStep.toString())
        binding.etJoystickUpdateInterval.setText(esp32.settings.joystickUpdateInterval.toString())
        binding.etJoystickThreshold.setText(esp32.settings.joystickThreshold.toString())
        binding.etJoystickSpeedXY.setText(esp32.settings.joystickSpeedXY.toString())
        binding.etJoystickSpeedZ.setText(esp32.settings.joystickSpeedZ.toString())
        binding.etDpadUpdateInterval.setText(esp32.settings.dpadUpdateInterval.toString())
        binding.etDpadSpeedXY.setText(esp32.settings.dpadSpeedXY.toString())
        binding.etDpadSpeedZ.setText(esp32.settings.dpadSpeedZ.toString())

        binding.btnSave.setOnClickListener {
            // Save updated settings values
            esp32.settings.canvasScalingFactor = binding.etCanvasScalingFactor.text.toString().toFloatOrNull() ?: esp32.settings.canvasScalingFactor
            esp32.settings.canvasTimeout = binding.etCanvasTimeout.text.toString().toLongOrNull() ?: esp32.settings.canvasTimeout
            esp32.settings.canvasTogglePenStep = binding.etCanvasTogglePenStep.text.toString().toFloatOrNull() ?: esp32.settings.canvasTogglePenStep
            esp32.settings.joystickUpdateInterval = binding.etJoystickUpdateInterval.text.toString().toLongOrNull() ?: esp32.settings.joystickUpdateInterval
            esp32.settings.joystickThreshold = binding.etJoystickThreshold.text.toString().toFloatOrNull() ?: esp32.settings.joystickThreshold
            esp32.settings.joystickSpeedXY = binding.etJoystickSpeedXY.text.toString().toFloatOrNull() ?: esp32.settings.joystickSpeedXY
            esp32.settings.joystickSpeedZ = binding.etJoystickSpeedZ.text.toString().toFloatOrNull() ?: esp32.settings.joystickSpeedZ
            esp32.settings.dpadUpdateInterval = binding.etDpadUpdateInterval.text.toString().toLongOrNull() ?: esp32.settings.dpadUpdateInterval
            esp32.settings.dpadSpeedXY = binding.etDpadSpeedXY.text.toString().toFloatOrNull() ?: esp32.settings.dpadSpeedXY
            esp32.settings.dpadSpeedZ = binding.etDpadSpeedZ.text.toString().toFloatOrNull() ?: esp32.settings.dpadSpeedZ

            esp32.saveSettings()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
