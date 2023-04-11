package com.example.bluetooth_template_ltf

import android.content.Intent
import android.os.Bundle
import com.example.bluetooth_template_ltf.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.subjects.PublishSubject
import com.example.bluetooth_template_ltf.ESP32.Companion.ConnectToESP32
import com.example.bluetooth_template_ltf.ESP32.Companion.sendMessageToESP32
import com.example.bluetooth_template_ltf.ESP32.Companion.serverAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val joystickMoveSubject = PublishSubject.create<Pair<Int, Int>>()
    private var prevAngle: Int = 0
    private var prevStrength: Int = 0
    private var moveVertical = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.inputWifiIP.setText(serverAddress)


        binding.buttonCanvas.setOnClickListener {
            Intent(this, CanvasActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.buttonJoystick.setOnClickListener {
            Intent(this, JoystickActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.buttonDigital.setOnClickListener {
            Intent(this, DigitalActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.buttonConnect.setOnClickListener {
            val serverAddressInput = binding.inputWifiIP.text.toString()
            ConnectToESP32(serverAddressInput)
        }

    }
}

