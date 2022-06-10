package com.mbahgojol.chami.login.forgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityForgotPasswordBinding
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.login.PasswordLoginActivity
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.otpView.setOtpCompletionListener(object :OnOtpCompletionListener
        {
            override fun onOtpCompleted(otp: String?) {
                val intent = Intent(this@ForgotPasswordActivity, NewPasswordActivity::class.java)
                startActivity(intent)
            }

        })

        binding.btnBack.setOnClickListener {
            this.finish()
        }
    }
}