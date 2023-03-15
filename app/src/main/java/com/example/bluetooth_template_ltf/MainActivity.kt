package com.example.bluetooth_template_ltf

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.bluetooth_template_ltf.databinding.ActivityMainBinding
import com.example.bluetooth_template_ltf.helperBT.BTActivityWrapper
import com.harrysoft.androidbluetoothserial.BluetoothManager
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface
import com.jakewharton.rxbinding3.view.drags
import com.jakewharton.rxbinding3.view.layoutChanges
import com.jakewharton.rxbinding3.view.systemUiVisibilityChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import com.jakewharton.rxbinding3.widget.changes


class MainActivity : BTActivityWrapper() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var lastMessageSentTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initBluetooth()

        binding.btnConnect.setOnClickListener {
                connectBluetooth(binding.inputBluetooth.text.toString())
        }

        binding.slider.changes()
            .debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value ->
                var angleValue = (value.toDouble()/100)*180
                var angleString = String.format("%.1f", angleValue)
                if (connected) {
                    sendBluetoothMessage(angleString)
                    binding.angleText.text = "Angle: $angleString"
                } else {
                    alert("Tidak terkoneksi dengan bluetooth device")
                }
            }

        // Ini buat check bluetooth connection,
        // lebih bagusnya kalau pake observables daripada
        // pake timer kek gini. But I value my time :D
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if(connected && !binding.slider.isEnabled) {
                    binding.slider.isEnabled = true
                } else if (!connected) {
                    binding.slider.isEnabled = false
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