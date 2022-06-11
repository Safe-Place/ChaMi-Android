package com.mbahgojol.chami.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.CreateUsers
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.signup.SignupActivity
import com.mbahgojol.chami.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val id_user = getid()

        binding.tvMoveSignup.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.ivPassword.setOnClickListener {
            if (id_user != null){
                val intent = Intent(this@LoginActivity, PasswordLoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity, "Daftar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivFaceRecog.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Login dengan face belum tersedia", Toast.LENGTH_SHORT).show()
        }
        binding.ivFingerPrint.setOnClickListener {
            Toast.makeText(this@LoginActivity, "Login dengan finger print belum tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getid() : String?{
        val pref = LoginPref(this@LoginActivity)
        val id = pref.getId()

        if(id!=null) {
            binding.tvIdPegawai.text = "id $id"
            return id
        }
        else{
            binding.tvIdPegawai.text = "Belum Daftar"
            return null
        }
    }
}