package kz.bloom.ui.ui_components.preference

import android.content.Context
import kz.bloom.ui.ui_components.preference.security.AESCrypt

class SecurityPreference(applicationContext: Context) : BaseSharedPreferences() {

    private val encryptPref = applicationContext.getSharedPreferences(
        SESSION_APP_CONFIG_CRYPTED,
        Context.MODE_PRIVATE
    )

    fun setTokenAccess(tokenAccess: String?) {
        this._tokenAccess = tokenAccess
        val encrypted = encrypt(ACCESS_TOKEN, tokenAccess)
        putString(ACCESS_TOKEN, encrypted, encryptPref)
    }

    fun getTokenAccess(): String? {
        if (_tokenAccess == null) {
            _tokenAccess = decrypt(
                ACCESS_TOKEN,
                encryptPref.getString(ACCESS_TOKEN, null)
            )
        }

        return _tokenAccess
    }

    fun setTokenRefresh(tokenRefresh: String?) {
        this._tokenRefresh = tokenRefresh
        val encrypted = encrypt(REFRESH_TOKEN_ID, tokenRefresh)
        putString(REFRESH_TOKEN_ID, encrypted, encryptPref)
    }

    fun getTokenRefresh(): String? {
        if (_tokenRefresh == null) {
            _tokenRefresh = decrypt(
                REFRESH_TOKEN_ID,
                encryptPref.getString(REFRESH_TOKEN_ID, null)
            )
        }

        return _tokenRefresh
    }

    fun setPushToken(pushToken: String?) {
        this._pushToken = pushToken
        val encrypted = encrypt(PUSH_TOKEN, pushToken)
        putString(PUSH_TOKEN, encrypted, encryptPref)
    }

    fun getPushToken(): String? {
        if (_pushToken == null) {
            _pushToken = decrypt(
                PUSH_TOKEN,
                encryptPref.getString(PUSH_TOKEN, null)
            )
        }

        return _pushToken
    }

    fun setPincode(pincode: String?) {
        this._pincode = pincode
        val encrypted = encrypt(PINCODE, pincode)
        putString(PINCODE, encrypted, encryptPref)
    }

    fun getPincode(): String? {
        if (_pincode == null) {
            _pincode = decrypt(
                PINCODE,
                encryptPref.getString(PINCODE, null)
            )
        }

        return _pincode
    }

    private fun decrypt(alias: String, cipherText: String?) = AESCrypt.decrypt(alias, cipherText)

    private fun encrypt(alias: String, initialText: String?) = AESCrypt.encrypt(alias, initialText)

    fun clearDataOnLogout() {
        _tokenAccess = null
        deleteField(encryptPref, ACCESS_TOKEN)
    }

    fun clearPincode() {
        _pincode = null
        deleteField(encryptPref, PINCODE)
    }

    fun clearAllSecurityData() {
        encryptPref.edit().clear().apply()
    }

}