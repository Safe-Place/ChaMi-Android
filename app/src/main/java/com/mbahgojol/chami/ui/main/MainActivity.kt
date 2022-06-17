package com.mbahgojol.chami.ui.main

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityMainBinding
import com.mbahgojol.chami.signup.SignupActivity
import com.mbahgojol.chami.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = activity.window
            val background = ContextCompat.getDrawable(activity, R.drawable.toolbar_color)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            window.statusBarColor = ContextCompat.getColor(activity, android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.root.hideKeyboard()
        setStatusBarGradiant(this)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.elevation = 0f

        val loginPref = LoginPref(this@MainActivity)
        val isLogin = loginPref.getSession()
        if (!isLogin) {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        binding.apply {
            bottomNavView.setupWithNavController(navController)
        }

        var chatPersonal = false
        var chatGroup = false

        fun showBullets() {
            binding.bottomNavView.getOrCreateBadge(R.id.chatFragment).isVisible =
                chatPersonal or chatGroup
        }

        navController.getBackStackEntry(R.id.chatFragment)
            .savedStateHandle.getLiveData<Boolean>("haveCount")
            .observe(this) {
                chatPersonal = it
                showBullets()
            }

        navController.getBackStackEntry(R.id.chatFragment)
            .savedStateHandle.getLiveData<Boolean>("haveCountGroup")
            .observe(this) {
                chatGroup = it
                showBullets()
            }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}