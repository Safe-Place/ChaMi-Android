package com.mbahgojol.chami.ui.main.chat.personal.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ActivityDetailPersonalBinding

class DetailPersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(this)
        binding = ActivityDetailPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<Users>("user")
        if (user != null) {
            binding.apply {
                avatar.load(user.profile_url)
                tvTitle.text = user.username
                tvJabatan.text = user.jabatan
                idPegawai.text = user.user_id
            }

            binding.btnBack.setOnClickListener {
                finish()
            }
        }
    }
}