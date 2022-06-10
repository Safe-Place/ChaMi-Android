package com.mbahgojol.chami.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.CreateUsers
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.databinding.ActivityLoginBinding
import com.mbahgojol.chami.signup.SignupActivity
import com.mbahgojol.chami.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        getid()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("TOKEN => ", it)
            token = it
        }

        binding.tvUsingLogin.setOnClickListener {
            if (sharedPref.userName != "") {
                service.searchUser(sharedPref.userName)
                    .get()
                    .addOnSuccessListener {
                        if (it != null && it.documents.isNotEmpty()) {
                            val user = it.documents[0].toObject<Users>()
                            sharedPref.userId = user?.user_id ?: ""
                            sharedPref.userName = user?.username ?: ""
                            service.updateToken(sharedPref.userId, token)

                            Intent(this, MainActivity::class.java).apply {
                                putExtra("user_id", user?.user_id)
                                startActivity(this)
                            }

                        }
                    }
            } else {
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

                val nameList = mutableListOf(
                    "Ghozi Mahdi",
                    "Annisa Sabila",
                    "Faren Saputra",
                    "Paramita Citra",
                    "Eky Nugraha Suaib",
                    "Amhad Junnaidi",
                    "Aismi Aliah"
                )

                val agenList = mutableListOf(
                    "Agent Divisi Digital Center",
                    "Lead Divisi Digital Center",
                    "Agent Divisi Digital Center",
                    "Agent Divisi Digital Center",
                    "Agent Divisi Digital Center",
                    "Agent Divisi Digital Center",
                    "Agent Divisi Digital Center"
                )

                val username = nameList.random()
                val users = CreateUsers(
                    true,
                    agenList.random(),
                    image.random(),
                    username = username
                )
                service.searchUser(username)
                    .get()
                    .addOnSuccessListener {
                        if (it != null && it.documents.isNotEmpty()) {
                            val user = it.documents[0].toObject<Users>()
                            sharedPref.userId = user?.user_id ?: ""
                            sharedPref.userName = user?.username ?: ""
                            service.updateToken(sharedPref.userId, token)

                            Intent(this, MainActivity::class.java).apply {
                                putExtra("user_id", user?.user_id)
                                startActivity(this)
                            }

//                    binding.progress.isVisible = false
                        } else {
                            service.addUser(users) { id ->
                                sharedPref.userId = id
                                sharedPref.userName = username
                                service.updateToken(sharedPref.userId, token)

//                        binding.progress.isVisible = false
                                Intent(this, MainActivity::class.java).apply {
                                    putExtra("user_id", id)
                                    startActivity(this)
                                }
                            }
                        }
                    }
            }


        }

        binding.tvMoveSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.ivPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, PasswordLoginActivity::class.java)
            startActivity(intent)
        }
        binding.ivFaceRecog.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "Login dengan face belum tersedia",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.ivFingerPrint.setOnClickListener {
            Toast.makeText(
                this@LoginActivity,
                "Login dengan finger print belum tersedia",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getid() {
        val pref = LoginPref(this@LoginActivity)
        val id = pref.getId()

        if (id != null) {
            binding.tvIdPegawai.text = "id $id"
        } else {
            binding.tvIdPegawai.text = "Belum Daftar"
        }
    }
}