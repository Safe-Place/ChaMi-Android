package com.mbahgojol.chami.ui.main.challanges

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityDetailParticipantBinding
import com.mbahgojol.chami.dummyData.Challenge
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Produk

class DetailParticipantActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailParticipantBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailParticipantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Participant>(EXTRA_PARTICIPANT) as Participant

        binding.tvNamaPeserta.text = data.nama
        binding.tvFileSub.text = data.namaFile
        binding.ivPeserta.load(data.avatar){
            transformations(CircleCropTransformation())
        }
        binding.tvPesanPribadi.text = data.pesan


    }
    companion object{
        const val EXTRA_PARTICIPANT = "extra participant"
    }
}