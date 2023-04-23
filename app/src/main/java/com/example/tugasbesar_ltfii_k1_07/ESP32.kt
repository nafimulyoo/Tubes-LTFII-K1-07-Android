package com.example.tugasbesar_ltfii_k1_07

import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class ESP32 {
////    Canvas Settings
//    val canvasPaperWidth = 200
//    val canvasPaperHeight = 200
//    val canvasPosThreshold = 10
//    val canvasTickSpeed = 10
//    val canvasSpeedZ = 10
//    val canvasStepZ = 10
//
////   Joystick Settings
//    val joystickStrengthThreshold = 10
//    val joystickSpeedXY = 10
//    val joystickSpeedZ = 10
//    val joystickStepXY = 10
//    val joystickStepZ = 10
//
////    Digital Settings
//    val digitalSpeedXY = 10
//    val digitalSpeedZ = 10
//    val digitalStepXY = 10
//    val digitalStepZ = 10

    var serverPort = 80
    var serverAddress = "192.168.4.1"

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

    fun changeSettings(setting: String, value: String) {
        val espUrl = URL("http://$serverAddress/?message=$setting:$value")
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

    fun testConnection() {
        val espUrl = URL("http://$serverAddress/")
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