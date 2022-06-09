package com.mbahgojol.chami.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityMainBinding
import com.mbahgojol.chami.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import com.mbahgojol.chami.dummyData.User
import com.mbahgojol.chami.signup.SignupActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.root.hideKeyboard()
        setContentView(binding.root)
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

//        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
    }

//    private fun getDataIntent(){
//        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_view, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu.findItem(R.id.search)
        val searchView = menuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint_user)

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                navController.navigate(R.id.searchFragment)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                navController.popBackStack()
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    companion object{
        const val EXTRA_USER = "extra_user"
    }
}