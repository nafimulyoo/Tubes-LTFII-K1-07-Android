package com.example.tugasbesar_ltfii_k1_07.ui.digital
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DigitalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is digital Fragment"
    }
    val text: LiveData<String> = _text
}