package com.mbahgojol.chami.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.CreateUsers
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.login.LoginActivity
import com.mbahgojol.chami.ui.main.MainActivity
import com.mbahgojol.chami.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref
    private var token: String = ""
    private val signupViewModel by viewModels<SignupViewModel>()

    private var posisi_user: String? = "Agent"
    private var divisi_user: String? = "Kosong"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSignup.setOnClickListener {
            signup()
        }

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

        val avatar = image.random()

        signupViewModel.user.observe(this@SignupActivity) { user ->
            LoginPref(this@SignupActivity).apply {
                setNama(user.name)
                setId(user.id_pegawai)
                setEmail(user.email)
                setDivisi(user.divisi)
                setPosisi(user.posisi)
                setAvatar(avatar)
            }


            val username = user.name
            val users = CreateUsers(
                true,
                user.divisi, //Agent Divisi Digital Center
                user.posisi,
                avatar,
                username = username,
                user_id = user.id_pegawai,
                token = token
            )

            service.searchUsers(user.id_pegawai)
                .get()
                .addOnSuccessListener {
                    if (it != null && it.documents.isNotEmpty()) {
                        Toast.makeText(
                            this@SignupActivity,
                            "Akun sudah ada",
                            Toast.LENGTH_LONG
                        ).show()

                        showLoading(false)
                    } else {
                        service.addUsers(users).addOnSuccessListener {
                            sharedPref.userId = user.id_pegawai
                            sharedPref.userName = binding.nama.text.toString()
                            LoginPref(this@SignupActivity).setSession(true)
                            showLoading(false)
                            Intent(this, MainActivity::class.java).apply {
                                putExtra("user_id", sharedPref.userId)
                                this.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(this)
                            }
                        }
                    }
                }
        }

        binding.tvMoveLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("TOKEN => ", it)
            token = it
        }

        // divisi spinner
        val listDivisi = resources.getStringArray(R.array.divisi_list)
        val adapterDiv = ArrayAdapter(this, R.layout.spinner_item, listDivisi)
        binding.spinnerDivisi.adapter = adapterDiv

        binding.spinnerDivisi.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                divisi_user = listDivisi[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        // posisi spinner
        val listPosisi = resources.getStringArray(R.array.posisi_list)
        val adapter = ArrayAdapter(this, R.layout.spinner_item, listPosisi)
        binding.spinnerPosisi.adapter = adapter

        binding.spinnerPosisi.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                posisi_user = listPosisi[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        signupViewModel.isLoading.observe(this@SignupActivity) {
            showLoading(it)
        }
    }

    private fun signup() {


        val namaPegawai = binding.nama.text.toString()
        val idPegawai = binding.idPegawai.text.toString()
        val emailPegawai = binding.email.text.toString()
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
                signupViewModel.signup(
                    idPegawai,
                    namaPegawai,
                    passwordPegawai,
                    konfirmPassword,
                    emailPegawai,
                    posisi_user!!,
                    divisi_user!!,
                    this@SignupActivity
                )
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