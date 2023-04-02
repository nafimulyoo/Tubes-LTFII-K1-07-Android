package com.example.bluetooth_template_ltf

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.ui.AppBarConfiguration
import com.example.bluetooth_template_ltf.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch




class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val joystickMoveSubject = PublishSubject.create<Pair<Int, Int>>()
    private var prevAngle: Int = 0
    private var prevStrength: Int = 0
    private var moveVertical = "0"

     // Replace with the IP address of your ESP32 access point
     // The same port number used in the ESP32 code



    private fun sendMessageToESP32(message: String) {
        private val serverPort = 80
        val serverAddress = "192.168.4.1"
        val espUrl = URL("http://$serverAddress/?message=$message")
        GlobalScope.launch(Dispatchers.IO) {
            val connection = espUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Request successful
                val response = connection.inputStream.bufferedReader().readText()
                println("Response from ESP32: $response")
            } else {
                // Request failed
                println("Request failed with error code $responseCode")
            }
            connection.disconnect()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBluetooth()

        binding.btnConnect.setOnClickListener {
//                connectBluetooth(binding.inputBluetooth.text.toString())
//            connectBluetooth("BT")
//            sendBluetoothMessage("TEST")
            sendMessageToESP32("TEST")

        }

        binding.buttonCanvas.setOnClickListener {
            Intent(this, CanvasActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.joystickView.setOnMoveListener { angle, strength ->
            if(angle != prevAngle || strength != prevStrength) {
                joystickMoveSubject.onNext(angle to strength)
                prevAngle = angle;
                prevStrength = strength;
            }



        joystickMoveSubject
            .debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (angle, strength) ->
                binding.angleText.text = "Angle: $angle"
                binding.magnitudeText.text = "Magnitude: $strength"
                Log.d("TEST", "angle $angle strength $strength")
//                sendBluetoothMessage("$angle;$strength")
            }

//        binding.upButton.setOnClickListener {
//            moveVertical = "1"
//        }

//        binding.downButton.setOnClickListener {
//            moveVertical = "-1"
//        }

        // Ini buat check bluetooth connection,
        // lebih bagusnya kalau pake observables daripada
        // pake timer kek gini. But I value my time :D
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if(connected && !binding.joystickView.isEnabled) {
                    binding.joystickView.isEnabled = true
//                    binding.upButton.isEnabled = true
//                    binding.downButton.isEnabled = true
//                    binding.btnCamera.isEnabled = true

                    if (mode == "MANUAL") {
                        sendBluetoothMessage("MANUAL $angle;$strength;$moveVertical")
                    } else if (mode == "AUTO") {
                        sendBluetoothMessage("AUTO $angle;$strength;$moveVertical")
                    }

                } else if (!connected) {
                    binding.joystickView.isEnabled = false
//                    binding.upButton.isEnabled = false
//                    binding.downButton.isEnabled = false
//                    binding.btnCamera.isEnabled = false
                }
                mainHandler.postDelayed(this, 100)
            }
        })


    }
    }

    override fun onMessageSent(message: String) {

    }

    override fun onMessageReceived(message: String) {

    }

}