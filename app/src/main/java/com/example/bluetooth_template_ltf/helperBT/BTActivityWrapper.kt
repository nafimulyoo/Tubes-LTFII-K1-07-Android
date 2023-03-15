package com.example.bluetooth_template_ltf.helperBT
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.bluetooth_template_ltf.databinding.ActivityMainBinding
import com.harrysoft.androidbluetoothserial.BluetoothManager
import com.harrysoft.androidbluetoothserial.BluetoothSerialDevice
import com.harrysoft.androidbluetoothserial.SimpleBluetoothDeviceInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BTActivityWrapper : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    public var connected = false;

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_PRIVILEGED
    )

    private val PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_PRIVILEGED
    )

    fun checkPermissions() {
        val permission1 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                1
            )
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                1
            )
        }
    }

    private var bluetoothManager: BluetoothManager? = null

    fun initBluetooth() {
        bluetoothManager = BluetoothManager.getInstance()
        checkBluetooth()
    }

    fun alert(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
            .show()
    }

    fun checkBluetooth(): Boolean {
        if (bluetoothManager == null) {
            Toast.makeText(this, "Bluetooth not available.", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    fun connectBluetooth(namaDevice: String) {
        if (!checkBluetooth()) {
            return
        }

        checkPermissions()

        val pairedDevices: Collection<BluetoothDevice>? = bluetoothManager?.pairedDevicesList

        pairedDevices?.let {
            for (device in pairedDevices) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Bluetooth not available.", Toast.LENGTH_LONG)
                        .show()
                    return
                }

                if (device.name.lowercase().contains(namaDevice.lowercase())) {
                    if (deviceInterface == null) {
                        connectDevice(device.address)
                        return
                    }
                }
            }

            Toast.makeText(this, "No matching device name with paired device.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private var deviceInterface: SimpleBluetoothDeviceInterface? = null

    private fun connectDevice(mac: String) {
        if (bluetoothManager == null) {
            Toast.makeText(this, "Have you init the bluetooth service?", Toast.LENGTH_LONG)
                .show()
            return
        }

        bluetoothManager?.let {
            it.openSerialDevice(mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connectedDevice: BluetoothSerialDevice -> onConnected(connectedDevice) }) { error: Throwable ->
                    Toast.makeText(this, "Error, $error", Toast.LENGTH_LONG)
                        .show()
                    connected = false
                    onError(
                        error
                    )
                }
        }
    }

    private fun onConnected(connectedDevice: BluetoothSerialDevice) {
        deviceInterface = connectedDevice.toSimpleDeviceInterface()
        deviceInterface?.setListeners(
            { message: String -> onMessageReceived(message) },
            { message: String -> onMessageSent(message) }) { error: Throwable ->
            connected = false
            onError(
                error
            )
        }
        connected = true
        alert("Connected with ${connectedDevice.mac}")
    }

    fun sendBluetoothMessage(message: String) {
        deviceInterface?.sendMessage(message)
    }

    abstract fun onMessageSent(message: String)
    abstract fun onMessageReceived(message: String)
    private fun onError(error: Throwable) {
        alert("Error, kemungkinan bluetooth tidak menyala")
        Log.d("BLUETOOTH", "ERROR!!!! $error")
    }
}