package com.mbahgojol.chami.ui.main.meeting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityAddMeetingBinding

class AddMeetingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddMeetingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            this.finish()
        }

        binding.btnSaveMeeting.setOnClickListener {
            Toast.makeText(this@AddMeetingActivity, "Jadwal ditambahkan", Toast.LENGTH_LONG).show()
            this.finishAndRemoveTask()
        }
    }
}