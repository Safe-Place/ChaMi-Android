package com.mbahgojol.chami.signup

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mbahgojol.chami.api.ApiConfig
import com.mbahgojol.chami.response.DataUser
import com.mbahgojol.chami.response.SignupResponse
import com.mbahgojol.chami.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : BaseViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _user = MutableLiveData<DataUser>()
    val user: LiveData<DataUser> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signup(
        id_pegawai: String,
        name: String,
        password: String,
        email: String,
        posisi: String,
        divisi: String,
        context: Context
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(context)
            .signupUser(id_pegawai, name, password, email, posisi, divisi)
        client.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = responseBody?.message
                    Toast.makeText(
                        context,
                        _message.value,
                        Toast.LENGTH_SHORT
                    ).show()
                    _user.value = responseBody?.data
                } else {
                    val jsonError = response.errorBody()?.string()?.let { JSONObject(it) }
                    val responseStatus = jsonError?.getString("message")
                    Toast.makeText(
                        context,
                        responseStatus,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}