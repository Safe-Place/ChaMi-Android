package com.mbahgojol.chami.ui.main.chat.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.messaging.FirebaseMessaging
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.CreateUsers
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ActivityCreateBinding
import com.mbahgojol.chami.data.remote.FirestoreService
import com.mbahgojol.chami.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CreateActivity : AppCompatActivity() {

    @Inject
    lateinit var service: FirestoreService

    @Inject
    lateinit var sharedPref: SharedPref
    private lateinit var binding: ActivityCreateBinding
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("TOKEN => ", it)
            token = it
        }

        /*Firebase.auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    user?.getIdToken(true)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Timber.e(it.result.token)
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }*/

        binding.btnGabung.setOnClickListener {
            binding.progress.isVisible = true

            val username = binding.etName.text.toString() ?: "Bot ChaMi"
            val users = CreateUsers(
                true,
                "Agent Divisi Digital Center",
                image.random(),
                username = username, token = token
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

                        binding.progress.isVisible = false
                    } else {
                        service.addUser(users) { id ->
                            sharedPref.userId = id
                            sharedPref.userName = username
                            binding.progress.isVisible = false
                            Intent(this, MainActivity::class.java).apply {
                                putExtra("user_id", id)
                                startActivity(this)
                            }
                        }
                    }
                }
        }
    }
}