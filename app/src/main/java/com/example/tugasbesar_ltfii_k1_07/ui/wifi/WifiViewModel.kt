package com.example.tugasbesar_ltfii_k1_07.ui.wifi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WifiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is wifi Fragment"
    }
    val text: LiveData<String> = _text
}