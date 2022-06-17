package com.mbahgojol.chami.login.forgotPassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityForgotPasswordBinding
import com.mbahgojol.chami.databinding.ActivityNewPasswordBinding
import com.mbahgojol.chami.utils.BaseActivity

class NewPasswordActivity : BaseActivity() {
    private lateinit var binding : ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            this.finish()
        }
    }
}