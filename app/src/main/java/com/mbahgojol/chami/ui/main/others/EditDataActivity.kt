package com.mbahgojol.chami.ui.main.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import coil.load
import coil.transform.CircleCropTransformation
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivityEditDataBinding
import com.mbahgojol.chami.databinding.ActivityLoginBinding

class EditDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setProfile()
    }

    private fun setProfile(){
        val pref = LoginPref(this)

        val nama = pref.getNama()
        val email = pref.getEmail()
        val divisi = pref.getDivisi()
        val id = pref.getId()
        val avatar = pref.getAvatar()

//        if(nama!=null){
//            binding.nama.text = nama.toString().toEditable()
//        }
//        if(email!=null){
//            binding.email.text = email.toString().toEditable()
//        }
//        if(divisi!=null){
//            binding.divisi.text = divisi.toString().toEditable()
//        }
//        if(id!=null){
//            binding.idPegawai.text = id.toString().toEditable()
//        }

        binding.nama.text = nama.toString().toEditable()
        binding.email.text = email.toString().toEditable()
        binding.divisi.text = divisi.toString().toEditable()
        binding.idPegawai.text = id.toString().toEditable()
        binding.avatar.load(avatar) {
            transformations(CircleCropTransformation())
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}