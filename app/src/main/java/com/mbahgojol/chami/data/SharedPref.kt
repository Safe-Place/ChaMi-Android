package com.mbahgojol.chami.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context: Context) {
    private val pref = context.getSharedPreferences("chami", Context.MODE_PRIVATE)
    private val userIdKey = "userIdKey"
    private val userNameKey = "userNameKey"

    var userId: String
        set(value) {
            pref.edit()
                .putString(userIdKey, value)
                .apply()
        }
        get() = pref.getString(userIdKey, "").toString()

    var userName: String
        set(value) {
            pref.edit()
                .putString(userNameKey, value)
                .apply()
        }
        get() = pref.getString(userNameKey, "").toString()
}