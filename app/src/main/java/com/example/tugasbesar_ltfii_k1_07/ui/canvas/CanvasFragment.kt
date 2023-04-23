package com.example.tugasbesar_ltfii_k1_07.ui.canvas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentCanvasBinding

class CanvasFragment : Fragment() {

    private var _binding: FragmentCanvasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val wifiViewModel =
            ViewModelProvider(this).get(CanvasViewModel::class.java)

        _binding = FragmentCanvasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCanvas
        wifiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}