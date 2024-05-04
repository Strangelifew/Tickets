package com.example.tickets.ui.tickets

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tickets.R
import com.example.tickets.databinding.ActivityTicketsBinding

class TicketsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}