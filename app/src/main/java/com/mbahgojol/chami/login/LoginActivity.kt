package com.mbahgojol.chami.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.databinding.ActivitySignupBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvMoveSignup.setOnClickListener{
            val intent = Intent(this@LoginActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}