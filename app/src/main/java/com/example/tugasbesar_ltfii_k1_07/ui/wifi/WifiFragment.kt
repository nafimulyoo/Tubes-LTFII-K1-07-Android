package com.example.tugasbesar_ltfii_k1_07.ui.wifi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugasbesar_ltfii_k1_07.MainActivity.Companion.esp32
import com.example.tugasbesar_ltfii_k1_07.databinding.FragmentWifiBinding

class WifiFragment : Fragment() {

    private var _binding: FragmentWifiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWifiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val connectWifiButton: Button = binding.connectWifiButton
        connectWifiButton.setOnClickListener {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        }

        // Submit IP Address button
        val submitButton: Button = binding.submitButton
        submitButton.setOnClickListener {
            val inputServerAddress = binding.serverAddressInput.text.toString()
            esp32.setServerAddress(inputServerAddress)
        }

        // Test Connection button
        val testConnectionButton: Button = binding.testConnectionButton
        testConnectionButton.setOnClickListener {
            esp32.testConnection()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

