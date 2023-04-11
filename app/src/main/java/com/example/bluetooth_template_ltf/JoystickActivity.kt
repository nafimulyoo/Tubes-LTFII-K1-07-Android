package com.example.bluetooth_template_ltf

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth_template_ltf.databinding.ActivityJoystickBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import com.example.bluetooth_template_ltf.ESP32.Companion.sendMessageToESP32
import java.util.concurrent.TimeUnit



class JoystickActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJoystickBinding
    private val joystickMoveSubject = PublishSubject.create<Pair<Int, Int>>()
    private var prevAngle: Int = 0
    private var prevStrength: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joystick)
        binding = ActivityJoystickBinding.inflate(layoutInflater)

        binding.joystickView.setEnabled(true)


        println(binding.joystickView.isEnabled())


        binding.joystickView.setOnMoveListener { angle, strength ->
            if(angle != prevAngle || strength != prevStrength) {
                joystickMoveSubject.onNext(angle to strength)
                prevAngle = angle;
                prevStrength = strength;
                Log.d("TEST", "angle $angle strength $strength")
            }
        }

        joystickMoveSubject.debounce(100, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ (angle, strength) ->
                binding.angleText.text = "Angle: $angle"
                binding.magnitudeText.text = "Magnitude: $strength"
                Log.d("TEST", "angle $angle strength $strength")
                sendMessageToESP32("angle $angle strength $strength")

            }
    }


}