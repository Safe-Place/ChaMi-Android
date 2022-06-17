package com.mbahgojol.chami.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mbahgojol.chami.ui.main.chat.personal.converse.setStatusBarGradiant

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(this)
    }
}