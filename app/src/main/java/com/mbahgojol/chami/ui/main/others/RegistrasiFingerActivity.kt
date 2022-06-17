package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityRegistrasiFingerBinding
import com.mbahgojol.chami.utils.BaseActivity

class RegistrasiFingerActivity : BaseActivity() {
    private lateinit var binding : ActivityRegistrasiFingerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiFingerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            this.finishAndRemoveTask()
        }
    }
}