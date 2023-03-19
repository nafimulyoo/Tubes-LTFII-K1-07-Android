package com.example.bluetooth_template_ltf

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.ui.AppBarConfiguration
import com.example.bluetooth_template_ltf.databinding.ActivityMainBinding
import com.example.bluetooth_template_ltf.helperBT.BTActivityWrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainActivity : BTActivityWrapper() {
    private lateinit var binding: ActivityMainBinding
    private val joystickMoveSubject = PublishSubject.create<Pair<Int, Int>>()
    private var prevAngle: Int = 0
    private var prevStrength: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBluetooth()

        binding.btnConnect.setOnClickListener {
                connectBluetooth(binding.inputBluetooth.text.toString())
        }

        binding.joystickView.setOnMoveListener { angle, strength ->
            if(angle != prevAngle || strength != prevStrength) {
                joystickMoveSubject.onNext(angle to strength)
                prevAngle = angle;
                prevStrength = strength;
            }
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

        // Ini buat check bluetooth connection,
        // lebih bagusnya kalau pake observables daripada
        // pake timer kek gini. But I value my time :D
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if(connected && !binding.joystickView.isEnabled) {
                    binding.joystickView.isEnabled = true
                } else if (!connected) {
                    binding.joystickView.isEnabled = false
                }
                mainHandler.postDelayed(this, 200)
            }
        })
    }

    override fun onMessageSent(message: String) {

    }

    override fun onMessageReceived(message: String) {
    }
}