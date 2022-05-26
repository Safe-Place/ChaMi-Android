package com.mbahgojol.chami.ui.main.chat.personal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.HistoryChatModel
import com.mbahgojol.chami.databinding.ActivityDetailPersonalChatBinding
import com.mbahgojol.chami.di.FirestoreService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailPersonalChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPersonalChatBinding

    @Inject
    lateinit var firestoreModule: FirestoreService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val model = intent.getParcelableExtra<HistoryChatModel>("data")
        if (model != null) {
            binding.apply {
                tvName.text = model.username
                tvJabatan.text = model.jabatan
                avatar.load(model.profile_url) {
                    transformations(CircleCropTransformation())
                }

                if (model.isonline) {
                    binding.isOnline.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color.green)
                } else {
                    binding.isOnline.backgroundTintList =
                        ContextCompat.getColorStateList(binding.root.context, R.color.grey)
                }
            }
        }
    }
}