package com.example.tugasbesar_ltfii_k1_07

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ESP32(private val context: Context) {

    private var serverAddress = "192.168.4.1"
    inner class ControlSettings() {
        var canvasPaperWidth = 200
        var canvasPaperHeight = 200
        var canvasPosThreshold = 10
        var canvasTickSpeed = 10
        var canvasSpeedZ = 10
        var canvasStepZ = 10

        var joystickStrengthThreshold = 10
        var joystickSpeedXY = 10
        var joystickSpeedZ = 10
        var joystickStepXY = 10
        var joystickStepZ = 10

        var digitalSpeedXY = 10
        var digitalSpeedZ = 10
        var digitalStepXY = 10
        var digitalStepZ = 10

        fun set(settings: String) {
            val settingsList = settings.split(",")
            canvasPaperWidth = settingsList[0].toInt()
            canvasPaperHeight = settingsList[1].toInt()
            canvasPosThreshold = settingsList[2].toInt()
            canvasTickSpeed = settingsList[3].toInt()
            canvasSpeedZ = settingsList[4].toInt()
            canvasStepZ = settingsList[5].toInt()

            joystickStrengthThreshold = settingsList[6].toInt()
            joystickSpeedXY = settingsList[7].toInt()
            joystickSpeedZ = settingsList[8].toInt()
            joystickStepXY = settingsList[9].toInt()
            joystickStepZ = settingsList[10].toInt()

            digitalSpeedXY = settingsList[11].toInt()
            digitalSpeedZ = settingsList[12].toInt()
            digitalStepXY = settingsList[13].toInt()
            digitalStepZ = settingsList[14].toInt()
        }

        fun send() {
            val settings = " $canvasPaperWidth,$canvasPaperHeight,$canvasPosThreshold,$canvasTickSpeed,$canvasSpeedZ,$canvasStepZ,$joystickStrengthThreshold,$joystickSpeedXY,$joystickSpeedZ,$joystickStepXY,$joystickStepZ,$digitalSpeedXY,$digitalSpeedZ,$digitalStepXY,$digitalStepZ"
            sendMessage(settings)
        }
    }

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
}