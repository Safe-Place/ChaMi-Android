package com.mbahgojol.chami.ui.main.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityDetailTukarPointBinding
import com.mbahgojol.chami.dummyData.Produk
import com.mbahgojol.chami.utils.BaseActivity

class DetailTukarPointActivity : BaseActivity() {
    private lateinit var binding : ActivityDetailTukarPointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTukarPointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Produk>(EXTRA_PRODUK) as Produk
        binding.ivFotoProduk.load(data.foto)
        binding.tvNamaProduk.text = data.nama
        binding.tvDescProduk.text = data.deskripsi
        binding.tvHargaProduk.text = data.harga.toString()

        binding.btnBack.setOnClickListener { this.finish() }
    }

    companion object {
        const val EXTRA_PRODUK = "extra_produk"
    }
}