package com.healthhubcm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.healthhubcm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuyer.setOnClickListener {
            startActivity(Intent(this, BuyerActivity::class.java))
        }

        binding.btnSeller.setOnClickListener {
            startActivity(Intent(this, SellerActivity::class.java))
        }
    }
}