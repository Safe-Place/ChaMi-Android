package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityKeamananBinding

class KeamananActivity : AppCompatActivity() {
    private lateinit var binding : ActivityKeamananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeamananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chamiPict.setOnClickListener {
            val intent = Intent(this, ChamiPictActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener { this.finish() }
    }
}