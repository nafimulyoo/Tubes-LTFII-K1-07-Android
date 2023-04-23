package com.example.tugasbesar_ltfii_k1_07.ui.joystick
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JoystickViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is joystick Fragment"
    }
    val text: LiveData<String> = _text
}