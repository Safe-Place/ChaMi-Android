package com.mbahgojol.chami.ui.main.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityDetailAprovalBinding
import com.mbahgojol.chami.databinding.ActivityDetailParticipantBinding
import com.mbahgojol.chami.dummyData.Participant
import com.mbahgojol.chami.dummyData.Validasi
import com.mbahgojol.chami.utils.BaseActivity

class DetailAprovalActivity : BaseActivity() {
    private lateinit var binding : ActivityDetailAprovalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAprovalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Validasi>(EXTRA_VALIDASI) as Validasi

        binding.tvName.text = data.nama
        binding.tvJabatan.text = data.divisi
        binding.avatar.load(data.avatar){
            transformations(CircleCropTransformation())
        }
        binding.tvDeskripsi.text = data.deskripsi
        binding.tvIdPegawai.text = data.id

    }
    companion object{
        const val EXTRA_VALIDASI= "extra validasi"
    }
}