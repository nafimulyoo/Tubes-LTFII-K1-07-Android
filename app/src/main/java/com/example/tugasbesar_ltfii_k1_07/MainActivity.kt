package com.example.tugasbesar_ltfii_k1_07

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tugasbesar_ltfii_k1_07.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var esp32: ESP32
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        esp32 = ESP32.getInstance(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_canvas, R.id.navigation_joystick, R.id.navigation_digital, R.id.navigation_settings
            )
        )

        binding.fabNavigationWifi.setOnClickListener {
            navController.navigate(R.id.navigation_wifi)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}