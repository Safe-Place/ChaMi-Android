package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityUbahSandiBinding
import com.mbahgojol.chami.ui.main.MainActivity
import com.mbahgojol.chami.utils.BaseActivity

class UbahSandiActivity : BaseActivity() {
    private lateinit var binding : ActivityUbahSandiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahSandiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            Toast.makeText(this@UbahSandiActivity, "Sandi Diperbarui", Toast.LENGTH_LONG).show()
            val intent = Intent(this, KeamananActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            this.finishAndRemoveTask()
        }
    }
}