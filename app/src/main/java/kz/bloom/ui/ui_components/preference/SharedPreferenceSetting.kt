package kz.bloom.ui.ui_components.preference

import android.content.Context

class SharedPreferencesSetting(private val applicationContext: Context) : BaseSharedPreferences() {

    private val securityPreference = SecurityPreference(applicationContext)

    private val sharedPreferencesCanBeRemoved = applicationContext.getSharedPreferences(
        SESSION_APP_CONFIG_CAN_BE_REMOVED,
        Context.MODE_PRIVATE
    )

    private val sharedPreferencesSaved = applicationContext.getSharedPreferences(
        SESSION_APP_CONFIG_SAVED,
        Context.MODE_PRIVATE
    )

    fun isAuth() = !accessToken.isNullOrBlank()

    fun isGuest() = accessToken.isNullOrBlank()

    fun clearAuth() {
        //clearDataOnLogout
        securityPreference.clearDataOnLogout()
    }

    var accessToken
        get() = securityPreference.getTokenAccess()
        set(value) = securityPreference.setTokenAccess(value)

    var pushToken
        get() = securityPreference.getPushToken()
        set(value) = securityPreference.setPushToken(value)

    var pincode
        get() = securityPreference.getPincode()
        set(value) = securityPreference.setPincode(value)

    var refreshToken: String?
        get() = securityPreference.getTokenRefresh()
        set(value) = securityPreference.setTokenRefresh(value)
}