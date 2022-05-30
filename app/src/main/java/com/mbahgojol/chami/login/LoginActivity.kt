package com.mbahgojol.chami.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getid()

        binding.tvMoveSignup.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.ivPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, PasswordLoginActivity::class.java)
            startActivity(intent)
        }
        binding.ivFaceRecog.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Login dengan face belum tersedia", Toast.LENGTH_SHORT).show()
        }
        binding.ivFingerPrint.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Login dengan finger print belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getid(){
        val pref = LoginPref(this@LoginActivity)
        val id = pref.getId()

        if(id!=null) {
            binding.tvIdPegawai.text = "id $id"
        }
        else{
            binding.tvIdPegawai.text = "Belum Daftar"
        }
    }
}