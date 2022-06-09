package com.mbahgojol.chami.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ActivityPasswordLoginBinding
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.signup.SignupViewModel
import com.mbahgojol.chami.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PasswordLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordLoginBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = LoginPref(this@PasswordLoginActivity)
        val id = pref.getId()

        binding.btnLogin.setOnClickListener {
            if (id == null){
                Toast.makeText(this@PasswordLoginActivity, "Id Pegawai belum terdaftar", Toast.LENGTH_LONG).show()
            } else login(id)
        }
    }

    fun login(id: String) {
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val password = binding.passwordEditText.text.toString()
        when {
            password.isEmpty() -> {
                binding.passwordEditText.error = "Masukkan password"
            }
            else -> {
//                loginViewModel.login(id,password, this@PasswordLoginActivity)
//                loginViewModel.isLoading.observe(this@PasswordLoginActivity) {
//                    showLoading(it)
//                }
//
//                loginViewModel.user.observe(this@PasswordLoginActivity) { user ->
//                    LoginPref(this@PasswordLoginActivity).apply {
//                        setSession(true)
//                    }

                    service.searchUser(id)
                        .get()
                        .addOnSuccessListener {
                            if (it != null && it.documents.isNotEmpty()) {
                                val user = it.documents[0].toObject<Users>()
                                sharedPref.userId = user?.user_id ?: ""

                                loginViewModel.login(id,password, this@PasswordLoginActivity)
                                loginViewModel.isLoading.observe(this@PasswordLoginActivity) {
                                    showLoading(it)
                                }

                                loginViewModel.user.observe(this@PasswordLoginActivity) { user ->
                                    LoginPref(this@PasswordLoginActivity).apply {
                                        setSession(true)
                                    }
                                    Intent(this, MainActivity::class.java).apply {
                                        putExtra("user_id", user?.id_pegawai)
                                        startActivity(this)
                                        finish()
                                    }
                                }

//                    binding.progress.isVisible = false
                            } else {
                                Toast.makeText(this@PasswordLoginActivity, "Akun tidak ditemukan", Toast.LENGTH_LONG).show()
                            }
                        }


                }
            }
        }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}