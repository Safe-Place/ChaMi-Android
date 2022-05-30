package com.mbahgojol.chami

import android.content.Context

class LoginPref(context : Context) {

    private val prefIsLogin = context.getSharedPreferences(PREFS_ISLOGIN, Context.MODE_PRIVATE)
    private val prefIdPegawai = context.getSharedPreferences(PREFS_ID, Context.MODE_PRIVATE)

    fun setSession(isLogin: Boolean){
        val editor = prefIsLogin.edit()
        editor.putBoolean(SESSION, isLogin)
        editor.apply()
    }

    fun getSession() : Boolean{
        return prefIsLogin.getBoolean(SESSION, false)
    }

    fun logout(){
        val editor = prefIsLogin.edit()
        editor.clear().apply()
    }

    fun setId(idPegawai: String?){
        val editor = prefIdPegawai.edit()
        editor.putString(ID, idPegawai)
        editor.apply()
    }

    fun getId() : String?{
        return prefIdPegawai.getString(ID, null)
    }

    companion object{
        private const val PREFS_ISLOGIN = "isLogin_pref"
        private const val SESSION = "session"
        private const val PREFS_ID = "id_pref"
        private const val ID = "id_pegawai"
    }
}