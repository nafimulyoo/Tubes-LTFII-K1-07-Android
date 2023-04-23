package com.example.tugasbesar_ltfii_k1_07.ui.joystick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentJoystickBinding

class JoystickFragment : Fragment() {

    private var _binding: FragmentJoystickBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        TODO: Tambah protokol komunikasi, buat looper

        _binding = FragmentJoystickBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val textView: TextView = binding.textJoystick


        val joystickView: JoystickView = binding.joystickView
        joystickView.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(dx: Float, dy: Float) {
                textView.text = String.format("dx: %.2f, dy: %.2f", dx, dy)
            }
        })

        val verticalJoystickView: VerticalJoystickView = binding.verticalJoystickView
        verticalJoystickView.setOnMoveListener(object : VerticalJoystickView.OnMoveListener {
            override fun onMove(dz: Float) {
                // Handle vertical joystick movement
                // position value will be between -1 and 1
                textView.text = String.format("dz: %.2f", dz)
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
