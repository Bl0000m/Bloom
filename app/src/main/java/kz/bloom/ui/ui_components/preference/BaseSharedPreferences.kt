package kz.bloom.ui.ui_components.preference

import android.content.SharedPreferences

abstract class BaseSharedPreferences {

    protected val SESSION_APP_CONFIG_CAN_BE_REMOVED = "SESSION_APP_CONFIG_CAN_BE_REMOVED"
    protected val SESSION_APP_CONFIG_SAVED = "SESSION_APP_CONFIG_SAVED"
    protected val SESSION_APP_CONFIG_JSON = "shared_pref_file"


    protected val SESSION_APP_CONFIG_CRYPTED = "SESSION_APP_CONFIG_CRYPTED"
    protected val ACCESS_TOKEN = "ACCESS_TOKEN_S"
    protected val REFRESH_TOKEN_ID = "REFRESH_TOKEN_ID_S"
    protected val PINCODE = "PINCODE_S"
    protected val PUSH_TOKEN = "FIREBASE_TOKEN_S"

    protected var _pincode: String? = null
    protected var _tokenAccess: String? = null
    protected var _tokenRefresh: String? = null
    protected var _pushToken: String? = null

    protected fun putBoolean(key: String, value: Boolean, sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    protected fun putInt(key: String, value: Int?, sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        value?.let { editor.putInt(key, it) }
        editor.apply()
    }

    protected fun putLong(key: String, value: Long?, sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        value?.let { editor.putLong(key, it) }
        editor.apply()
    }

    protected fun putString(key: String, value: String?, sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun deleteField(sharedPreferences: SharedPreferences, key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}