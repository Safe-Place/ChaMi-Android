package com.mbahgojol.chami.ui.main.challanges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityTambahChallengeBinding
import com.mbahgojol.chami.utils.BaseActivity

class TambahChallengeActivity : BaseActivity() {
    private lateinit var binding : ActivityTambahChallengeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            this.finishAndRemoveTask()
        }

        binding.btnSimpan.setOnClickListener {
            Toast.makeText(this@TambahChallengeActivity, "Challenge ditambahkan", Toast.LENGTH_LONG).show()
            this.finishAndRemoveTask()
        }
    }
}