package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.data.SharedPref
import com.mbahgojol.chami.data.model.Users
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.databinding.FragmentOthersBinding
import com.mbahgojol.chami.di.FirestoreService
import com.mbahgojol.chami.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

class OthersFragment : Fragment() {

    private lateinit var binding : FragmentOthersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOthersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfil()

        binding.tvLogout.setOnClickListener{ logout() }
        binding.linearEditData.setOnClickListener {
            val intent = Intent(requireActivity(), EditDataActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setProfil(){
        val pref = LoginPref(requireActivity())
        binding.tvName.text = pref.getNama().toString()
        binding.tvDivisi.text = pref.getDivisi().toString()
        binding.ivAvatar.load(pref.getAvatar()) {
            transformations(CircleCropTransformation())
        }
    }

    private fun logout(){
        val isLogin = LoginPref(requireActivity())
        isLogin.logout()
        val i = Intent(requireActivity(), SignupActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }


}