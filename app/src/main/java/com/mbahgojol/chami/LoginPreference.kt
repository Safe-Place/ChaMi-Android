package com.mbahgojol.chami

import android.content.Context

class LoginPref(context : Context) {

    private val pref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSession(isLogin: Boolean){
        val editor = pref.edit()
        editor.putBoolean(SESSION, isLogin)
        editor.apply()
    }

    fun getSession() : Boolean{
        return pref.getBoolean(SESSION, false)
    }

    fun logout(){
        val editor = pref.edit()
        editor.clear().apply()
    }

    companion object{
        private const val PREFS_NAME = "session_pref"
        private const val SESSION = "session"
    }
}