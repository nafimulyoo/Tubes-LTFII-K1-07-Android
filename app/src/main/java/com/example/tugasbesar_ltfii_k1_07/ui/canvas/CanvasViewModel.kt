package com.example.tugasbesar_ltfii_k1_07.ui.canvas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CanvasViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is canvas Fragment"
    }
    val text: LiveData<String> = _text
}