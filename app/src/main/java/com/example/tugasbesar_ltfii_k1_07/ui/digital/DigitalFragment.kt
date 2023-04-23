package com.example.tugasbesar_ltfii_k1_07.ui.digital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tugasbesar_ltfii_k1_07.R
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentDigitalBinding

class DigitalFragment : Fragment() {

    private var _binding: FragmentDigitalBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        //        TODO: Tambah protokol komunikasi, buat looper

        val buttonLeft = view.findViewById<ImageButton>(R.id.button_left)
        val buttonRight = view.findViewById<ImageButton>(R.id.button_right)
        val buttonBackward = view.findViewById<ImageButton>(R.id.button_backward)
        val buttonForward = view.findViewById<ImageButton>(R.id.button_forward)
        val buttonUp = view.findViewById<ImageButton>(R.id.button_up)
        val buttonDown = view.findViewById<ImageButton>(R.id.button_down)


        buttonUp.setOnClickListener {
            // Handle up button click
        }

        buttonDown.setOnClickListener {
            // Handle down button click
        }

        buttonLeft.setOnClickListener {
            // Handle left button click
        }

        buttonRight.setOnClickListener {
            // Handle right button click
        }

        buttonForward.setOnClickListener {
            // Handle left button click
        }

        buttonBackward.setOnClickListener {
            // Handle right button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
