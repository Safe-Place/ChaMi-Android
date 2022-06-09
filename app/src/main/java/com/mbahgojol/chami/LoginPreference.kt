package com.mbahgojol.chami

import android.content.Context

class LoginPref(context : Context) {

    private val prefIsLogin = context.getSharedPreferences(PREFS_ISLOGIN, Context.MODE_PRIVATE)
    private val prefIdPegawai = context.getSharedPreferences(PREFS_ID, Context.MODE_PRIVATE)
    private val prefNamaPegawai = context.getSharedPreferences(PREFS_NAMA, Context.MODE_PRIVATE)
    private val prefEmailPegawai = context.getSharedPreferences(PREFS_EMAIL, Context.MODE_PRIVATE)
    private val prefDivPegawai = context.getSharedPreferences(PREFS_DIV, Context.MODE_PRIVATE)
    private val prefPosisiPegawai = context.getSharedPreferences(PREFS_POSISI, Context.MODE_PRIVATE)

    fun setSession(isLogin: Boolean){
        val editor = prefIsLogin.edit()
        editor.putBoolean(SESSION, isLogin)
        editor.apply()
    }

    fun getSession() : Boolean{
        return prefIsLogin.getBoolean(SESSION, false)
    }

    fun logout(){
//        val editor = prefIsLogin.edit()
//        editor.clear().apply()
        prefIsLogin.edit().clear().apply()
        prefEmailPegawai.edit().clear().apply()
        prefPosisiPegawai.edit().clear().apply() //divisi sama nama blm di clear
    }

    fun setId(idPegawai: String?){
        val editor = prefIdPegawai.edit()
        editor.putString(ID, idPegawai)
        editor.apply()
    }

    fun getId() : String?{
        return prefIdPegawai.getString(ID, null)
    }

    fun setNama(namaPegawai: String?){
        val editor = prefNamaPegawai.edit()
        editor.putString(ID, namaPegawai)
        editor.apply()
    }

    fun getNama() : String?{
        return prefNamaPegawai.getString(NAMA, null)
    }

    fun setEmail(emailPegawai: String?){
        val editor = prefEmailPegawai.edit()
        editor.putString(ID, emailPegawai)
        editor.apply()
    }

    fun getEmail() : String?{
        return prefEmailPegawai.getString(EMAIL, null)
    }

    fun setDivisi(divPegawai: String?){
        val editor = prefIdPegawai.edit()
        editor.putString(DIV, divPegawai)
        editor.apply()
    }

    fun getDivisi() : String?{
        return prefDivPegawai.getString(DIV, null)
    }

    fun setPosisi(posisiPegawai: String?){
        val editor = prefPosisiPegawai.edit()
        editor.putString(POSISI, posisiPegawai)
        editor.apply()
    }

    fun getPosisi() : String?{
        return prefPosisiPegawai.getString(POSISI, null)
    }

    companion object{
        private const val PREFS_ISLOGIN = "isLogin_pref"
        private const val SESSION = "session"
        private const val PREFS_ID = "id_pref"
        private const val ID = "id_pegawai"
        private const val PREFS_NAMA = "name_pref"
        private const val NAMA = "nama_pegawai"
        private const val PREFS_DIV = "div_pref"
        private const val DIV = "div_pegawai"
        private const val PREFS_POSISI = "posisi_pref"
        private const val POSISI = "posisi_pegawai"
        private const val PREFS_EMAIL = "email_pref"
        private const val EMAIL = "email_pegawai"
    }
}