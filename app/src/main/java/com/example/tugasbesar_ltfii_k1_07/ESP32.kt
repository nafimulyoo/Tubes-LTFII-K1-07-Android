package com.example.tugasbesar_ltfii_k1_07

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL

data class Settings(
    // Canvas settings
    var canvasScalingFactor: Float = 1f,

    // Joystick Settings
    var joystickUpdateInterval: Long = 200L, // Milliseconds
    var joystickThreshold: Float = 0.1f,
    var joystickSpeedXY: Float = 1f, // Unit per second
    var joystickSpeedZ: Float = 1f, // Unit per second

    // D-Pad Settings
    var dpadUpdateInterval: Long = 600L, // Milliseconds
    var dpadSpeedXY: Float = 2f, // Unit per second
    var dpadSpeedZ: Float = 1f, // Unit per second
)

class ESP32(private val context: Context) {

    val settings = Settings()

    private var serverAddress = "192.168.4.1"

    fun setServerAddress(address: String) {
        // Validate address using a regex pattern
        val ipPattern = Regex("""^((25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)$""")

        if (address == "") {
            Toast.makeText(context, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            return
        }
        if (address == serverAddress) {
            Toast.makeText(context, "IP address is already set", Toast.LENGTH_SHORT).show()
            return
        }
        if (!ipPattern.matches(address)) {
            Toast.makeText(context, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            return
        }

        serverAddress = address
        Toast.makeText(context, "IP address set to $serverAddress", Toast.LENGTH_SHORT).show()
    }

    fun sendMessage(message: String) {
        val espUrl = URL("http://$serverAddress/?message=$message")
        GlobalScope.launch(Dispatchers.IO) {
            try {
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
            } catch (e: ConnectException) {
                // Show Toast message indicating that the connection has failed
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to connect to ESP32 at $serverAddress", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun saveSettings() {
        val sharedPreferences = context.getSharedPreferences("ESP32Settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("canvasScalingFactor", settings.canvasScalingFactor)

            putLong("joystickUpdateInterval", settings.joystickUpdateInterval)
            putFloat("joystickThreshold", settings.joystickThreshold)
            putFloat("joystickSensitivity", settings.joystickSpeedXY)
            putFloat("joystickSpeedZ", settings.joystickSpeedZ)

            putLong("dpadUpdateInterval", settings.dpadUpdateInterval)
            putFloat("dpadStepXY", settings.dpadSpeedXY)
            putFloat("dpadStepZ", settings.dpadSpeedZ)
            apply()
        }
    }

    fun loadSettings() {
        val sharedPreferences = context.getSharedPreferences("ESP32Settings", Context.MODE_PRIVATE)
        settings.canvasScalingFactor = sharedPreferences.getFloat("canvasScalingFactor", settings.canvasScalingFactor)

        settings.joystickUpdateInterval = sharedPreferences.getLong("joystickInterval", settings.joystickUpdateInterval)
        settings.joystickThreshold = sharedPreferences.getFloat("joystickThreshold", settings.joystickThreshold)
        settings.joystickSpeedXY = sharedPreferences.getFloat("joystickSensitivity", settings.joystickSpeedXY)
        settings.joystickSpeedZ = sharedPreferences.getFloat("joystickSpeedZ", settings.joystickSpeedZ)

        settings.dpadUpdateInterval = sharedPreferences.getLong("dpadInterval", settings.dpadUpdateInterval)
        settings.dpadSpeedXY = sharedPreferences.getFloat("dpadStepXY", settings.dpadSpeedXY)
        settings.dpadSpeedZ = sharedPreferences.getFloat("dpadStepZ", settings.dpadSpeedZ)
    }

    init {
        loadSettings()
    }
}