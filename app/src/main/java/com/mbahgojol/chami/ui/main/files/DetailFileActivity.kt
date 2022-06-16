package com.mbahgojol.chami.ui.main.files

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mbahgojol.chami.data.model.Files
import com.mbahgojol.chami.databinding.ActivityDetailFileBinding
import timber.log.Timber

class DetailFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val model = intent.getParcelableExtra<Files>("data")

        binding.apply {
            val settings = webview.settings
            settings.javaScriptEnabled = true
            settings.setSupportZoom(false)

            model?.file_url?.let {
                val url = "https://drive.google.com/viewerng/viewer?embedded=true&url=$it"
                Timber.e(url)
                webview.loadUrl(it)
            }
        }
    }
}