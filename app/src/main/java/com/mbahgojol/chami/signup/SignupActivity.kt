package com.mbahgojol.chami.signup

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.MainActivity
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.login.LoginActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.uploadPhoto.setOnClickListener{startGallery()}

        binding.btnSignup.setOnClickListener {
            signup()
        }

        binding.tvMoveLogin.setOnClickListener{
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signup(){
         val signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        val namaPegawai = binding.nama.text.toString()
        val idPegawai = binding.idPegawai.text.toString()
        val emailPegawai = binding.email.text.toString()
        val divPegawai = binding.divisi.text.toString()
        val posisiPegawai = binding.posisi.text.toString()
        val passwordPegawai = binding.password.text.toString()

        when {
            namaPegawai.isEmpty() -> {
                binding.nama.error = "Masukkan nama"
            }
            idPegawai.isEmpty() -> {
                binding.idPegawai.error = "Masukkan Id pegawai"
            }
            emailPegawai.isEmpty() -> {
                binding.email.error = "Masukkan email"
            }
            divPegawai.isEmpty() -> {
                binding.divisi.error = "Masukkan divisi"
            }
            posisiPegawai.isEmpty() -> {
                binding.posisi.error = "Masukkan posisi"
            }
            passwordPegawai.isEmpty() -> {
                binding.password.error = "Masukkan password"
            }
            else -> {
                if(getFile!=null){
                    val file = reduceFileImage(getFile as File)

                    val divisi = divPegawai.toRequestBody("text/plain".toMediaType())
                    val email = emailPegawai.toRequestBody("text/plain".toMediaType())
                    val id = idPegawai.toRequestBody("text/plain".toMediaType())
                    val name = namaPegawai.toRequestBody("text/plain".toMediaType())
                    val password = passwordPegawai.toRequestBody("text/plain".toMediaType())
                    val posisi = posisiPegawai.toRequestBody("text/plain".toMediaType())

                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    signupViewModel.signup(divisi,email,id,image,name,password,posisi, this@SignupActivity)

                    signupViewModel.isLoading.observe(this@SignupActivity) {
                        showLoading(it)
                    }

                    val isLogin = LoginPref(this@SignupActivity)
                    isLogin.setSession(true)

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@SignupActivity)
            getFile = myFile
            binding.uploadPhoto.setImageURI(selectedImg)
        }
    }

    companion object{
        const val TAG = "SignupActivity"
    }
}