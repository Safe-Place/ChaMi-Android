package com.mbahgojol.chami.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mbahgojol.chami.api.ApiConfig
import com.mbahgojol.chami.response.DataLogin
import com.mbahgojol.chami.response.DataUser
import com.mbahgojol.chami.response.LoginResponse
import com.mbahgojol.chami.response.SignupResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class LoginViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _user = MutableLiveData<DataLogin>()
    val user: LiveData<DataLogin> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(id_pegawai: String, password: String, context: Context){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).loginUser(id_pegawai,password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = responseBody?.message
                    _user.value = responseBody?.data
                } else {
                    val jsonError = response.errorBody()?.string()?.let{ JSONObject(it) }
                    val responseStatus = jsonError?.getString("message")
                    Toast.makeText(
                        context,
                        responseStatus,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Timber.e("onFailure: " + t.message)
            }
        })
    }
}