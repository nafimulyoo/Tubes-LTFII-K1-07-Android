package com.example.tugasbesar_ltfii_k1_07

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

data class Settings(
    // Server settings
    var serverAddress: String = "192.168.4.1",

    // Canvas settings
    var canvasScalingFactor: Float = 1f,
    var canvasTimeout: Long = 25L, // Milliseconds
    var canvasTogglePenStep: Float = 1f,

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

class ESP32 private constructor(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("ESP32Settings", Context.MODE_PRIVATE)
    val settings = Settings()

    init {
        loadSettings()
    }

    companion object {
        @Volatile private var instance: ESP32? = null

        fun getInstance(context: Context): ESP32 =
            instance ?: synchronized(this) {
                instance ?: ESP32(context).also { instance = it }
            }
    }


    fun setServerAddress(address: String) {
        // Validate address using a regex pattern
        val ipPattern = Regex("""^((25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)\.){3}(25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|\d)$""")

        if (address == "") {
            Toast.makeText(context, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            return
        }
        if (address == settings.serverAddress) {
            Toast.makeText(context, "IP address is already set", Toast.LENGTH_SHORT).show()
            return
        }
        if (!ipPattern.matches(address)) {
            Toast.makeText(context, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            return
        }

        settings.serverAddress = address
        Toast.makeText(context, "IP address set to ${settings.serverAddress}", Toast.LENGTH_SHORT).show()
    }

    fun sendMessage(message: String) {
        println("Sending message to ESP32: $message")

        val espUrl = URL("http://${settings.serverAddress}/?message=$message")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = espUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Request successful
                    val response = connection.inputStream.bufferedReader().readText()
                } else {
                    // Request failed
                    println("Request failed with error code $responseCode")
                }
                connection.disconnect()
            } catch (e: ConnectException) {
                println("Connection to ESP32 failed")
                    GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Connection to ESP32 failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SocketTimeoutException) {
                println("Connection to ESP32 timed out")
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Connection to ESP32 timed out", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun testConnection() {
        println("Sending message to ESP32: TEST")
        val espUrl = URL("http://${settings.serverAddress}/?message=TEST")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val connection = espUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Request successful
                    val response = connection.inputStream.bufferedReader().readText()

                    val toastMessage = response.substring(response.indexOf("<h1>") + 4, response.indexOf("</h1>"))
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    }
                    println(toastMessage)
                } else {
                    // Request failed
                    println("Request failed with error code $responseCode")
                }
                connection.disconnect()
            } catch (e: ConnectException) {
                println("Connection to ESP32 failed")
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Connection to ESP32 failed", Toast.LENGTH_SHORT).show()
                }
            }
            catch (e: SocketTimeoutException) {
                println("Connection to ESP32 timed out")
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Connection to ESP32 timed out", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun saveSettings() {
        with(sharedPreferences.edit()) {
            putString("serverAddress", settings.serverAddress)

            putFloat("canvasScalingFactor", settings.canvasScalingFactor)
            putLong("canvasTimeout", settings.canvasTimeout)
            putFloat("canvasTogglePenStep", settings.canvasTogglePenStep)

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
        settings.canvasScalingFactor = sharedPreferences.getFloat("canvasScalingFactor", settings.canvasScalingFactor)
        settings.canvasTimeout = sharedPreferences.getLong("canvasTimeout", settings.canvasTimeout)

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