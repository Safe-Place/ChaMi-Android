package com.mbahgojol.chami.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.ui.main.MainActivity
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.CreateUsers
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private var getFile: File? = null

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSignup.setOnClickListener {
            signup()
        }

        binding.tvMoveLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // divisi spinner
        val adapterDiv = ArrayAdapter.createFromResource(this,
            R.array.divisi_list, R.layout.spinner_item)
        adapterDiv.setDropDownViewResource(R.layout.spinner_list)
        binding.spinnerDivisi.adapter = adapterDiv

        // posisi spinner
        val adapterPosisi = ArrayAdapter.createFromResource(this,
            R.array.posisi_list, R.layout.spinner_item)
        adapterPosisi.setDropDownViewResource(R.layout.spinner_list)
        binding.spinnerDivisi.adapter = adapterPosisi
    }

    private fun signup() {
        val image = listOf(
            "https://i.pinimg.com/736x/84/6c/72/846c720bbb1ec76af83d290f1b8cc97c.jpg",
            "https://i.pinimg.com/474x/93/1a/8d/931a8d683d3377eb8bf216438959ec1d.jpg",
            "https://i.pinimg.com/564x/18/b9/ff/18b9ffb2a8a791d50213a9d595c4dd52.jpg",
            "https://i.pinimg.com/474x/86/ea/e3/86eae3d8abc2362ad6262916cb950640.jpg",
            "https://i.pinimg.com/474x/33/d7/70/33d770443af698d1dc410a32482fa264.jpg",
            "https://i.pinimg.com/564x/12/6f/3c/126f3c861b4abbb9eb891c21f4fd8740.jpg",
            "https://i.pinimg.com/736x/2f/ec/a4/2feca4c9330929232091f910dbff7f87.jpg",
            "https://i.pinimg.com/originals/f9/ba/90/f9ba90e3eba7af18a0ca139844ed08d5.jpg",
            "https://i.pinimg.com/originals/3f/c3/d2/3fc3d2a90e45cc51b9c2f2ec67992050.png",
            "https://i.pinimg.com/originals/eb/7f/a7/eb7fa775f1ee3ca4f8beeaff5dc9d468.jpg",

            )
//
//        val username = "ghozi"
//        val users = CreateUsers(
//            true,
//            "Agent Divisi Digital Center",
//            image.random(),
//            username = username
//        )
//        service.searchUser(username)
//            .get()
//            .addOnSuccessListener {
//                if (it != null && it.documents.isNotEmpty()) {
//                    val user = it.documents[0].toObject<Users>()
//                    sharedPref.userId = user?.user_id ?: ""
//
//                    Intent(this, MainActivity::class.java).apply {
//                        putExtra("user_id", user?.user_id)
//                        startActivity(this)
//                    }
//
////                    binding.progress.isVisible = false
//                } else {
//                    service.addUser(users) { id ->
//                        sharedPref.userId = id
////                        binding.progress.isVisible = false
//                        Intent(this, MainActivity::class.java).apply {
//                            putExtra("user_id", id)
//                            startActivity(this)
//                        }
//                    }
//                }
//            }

        val signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)

        val namaPegawai = binding.nama.text.toString()
        val idPegawai = binding.idPegawai.text.toString()
        val emailPegawai = binding.email.text.toString()
//        val divPegawai = binding.divisi.text.toString()
        val divPegawai = binding.spinnerDivisi2.selectedItem.toString()
        val posisiPegawai = binding.spinnerPosisi2.selectedItem.toString()
        val passwordPegawai = binding.password.text.toString()
        val konfirmPassword = binding.konfirmPassword.text.toString()

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
//            divPegawai.isEmpty() -> {
//                binding.divisi.error = "Masukkan divisi"
//            }
//            posisiPegawai.isEmpty() -> {
//                binding.posisi.error = "Masukkan posisi"
//            }
            passwordPegawai.isEmpty() -> {
                binding.password.error = "Masukkan password"
            }
            konfirmPassword.isEmpty() -> {
                binding.konfirmPassword.error = "Tidak boleh kosong"
            }
            konfirmPassword != passwordPegawai -> {
                binding.konfirmPassword.error = "Password tidak sama"
            }
            else -> {
////                if(getFile!=null){
////                    val file = reduceFileImage(getFile as File)
////
////                    val divisi = divPegawai.toRequestBody("text/plain".toMediaType())
////                    val email = emailPegawai.toRequestBody("text/plain".toMediaType())
////                    val id = idPegawai.toRequestBody("text/plain".toMediaType())
////                    val name = namaPegawai.toRequestBody("text/plain".toMediaType())
////                    val password = passwordPegawai.toRequestBody("text/plain".toMediaType())
////                    val posisi = posisiPegawai.toRequestBody("text/plain".toMediaType())
////
////                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
////                    val image: MultipartBody.Part = MultipartBody.Part.createFormData(
////                        "photo",
////                        file.name,
////                        requestImageFile
////                    )
                signupViewModel.signup(
                    idPegawai,
                    namaPegawai,
                    passwordPegawai,
                    konfirmPassword,
                    emailPegawai,
                    posisiPegawai,
                    divPegawai,
                    this@SignupActivity
                )

                signupViewModel.isLoading.observe(this@SignupActivity) {
                    showLoading(it)
                }

                signupViewModel.user.observe(this@SignupActivity) { user ->
                    LoginPref(this@SignupActivity).apply {
                        setNama(user.name)
                        setId(user.id_pegawai)
                        setEmail(user.email)
                        setDivisi(user.divisi)
                        setPosisi(user.posisi)
                    }

//                    val isLogin = LoginPref(this@SignupActivity)
//                    isLogin.setSession(true)

                    val username = user.name
                    val users = CreateUsers(
                        true,
                        user.divisi, //Agent Divisi Digital Center
                        user.posisi,
                        image.random(),
                        username = username
                    )

                    service.searchUser(user.id_pegawai)
                        .get()
                        .addOnSuccessListener {
                            if (it != null && it.documents.isNotEmpty()) {
                                Toast.makeText(this@SignupActivity, "Akun sudah ada", Toast.LENGTH_LONG).show()

//                    binding.progress.isVisible = false
                            } else {
                                service.addUser(users, user.id_pegawai) { id ->
                                    sharedPref.userId = id
                                    LoginPref(this@SignupActivity).setSession(true)
//                        binding.progress.isVisible = false
                                    Intent(this, MainActivity::class.java).apply {
                                        putExtra("user_id", id)
                                        startActivity(this)
                                    }
                                }
                            }
                        }

//                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
//
////                     ini nyoba aja, nanti hapus
////                    val avatar = resources.obtainTypedArray(R.array.avatar)
////                    val user = User(namaPegawai,idPegawai,emailPegawai,divPegawai,posisiPegawai,passwordPegawai,avatar.getResourceId(0,-1))
////
////                    val isLogin = LoginPref(this@SignupActivity)
////                    isLogin.setSession(true)
////
////
////                    val moveWithObjectIntent = Intent(this@SignupActivity, MainActivity::class.java)
////                    moveWithObjectIntent.putExtra(MainActivity.EXTRA_USER, user)
////                    startActivity(moveWithObjectIntent)
////                    finish()
////                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }




    companion object {
        const val TAG = "SignupActivity"
    }
}