package com.mbahgojol.chami.ui.main.others

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mbahgojol.chami.LoginPref
import com.mbahgojol.chami.R
import com.mbahgojol.chami.databinding.ActivitySignupBinding
import com.mbahgojol.chami.databinding.FragmentOthersBinding
import com.mbahgojol.chami.signup.SignupActivity

class OthersFragment : Fragment() {

    private lateinit var binding : FragmentOthersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_others, container, false)
        binding = FragmentOthersBinding.inflate(layoutInflater, container, false)
        binding.tvLogout.setOnClickListener{
            logout()
        }


        return binding.root
    }

    private fun logout(){
        val isLogin = LoginPref(requireActivity())
        isLogin.logout()
        val i = Intent(requireActivity(), SignupActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }


}