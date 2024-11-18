package kz.bloom.ui.intro.splash

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kz.bloom.ui.intro.splash.content.SplashContent
import kz.bloom.ui.main.MainActivity
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class SplashActivity : ComponentActivity(), KoinComponent {

    private val sharedPreferences by inject<SharedPreferencesSetting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAccessTokenExpired(sharedPreferences.accessToken) && isRefreshTokenExpired(
                sharedPreferences.refreshToken
            )
        ) {
            clearTokens(sharedPreferences)
        } else if (isAccessTokenExpired(sharedPreferences.accessToken)) {
            sharedPreferences.accessToken = null
        }
        enableEdgeToEdge()
        setContent {
            SplashContent(
                onLogoShown = {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                }
            )
        }
    }
}

fun isAccessTokenExpired(accessToken: String?): Boolean {
    if (accessToken.isNullOrEmpty()) return true
    return try {
        val parts = accessToken.split(".")
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val expiryTime = JSONObject(payload).getLong("exp") * 1000
        System.currentTimeMillis() > expiryTime
    } catch (e: Exception) {
        true
    }
}

fun isRefreshTokenExpired(refreshToken: String?): Boolean {
    if (refreshToken.isNullOrEmpty()) return true
    return try {
        val parts = refreshToken.split(".")
        if (parts.size < 2) return true
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
        val expiryTime = JSONObject(payload).getLong("exp") * 1000
        System.currentTimeMillis() > expiryTime
    } catch (e: Exception) {
        true
    }
}

private fun clearTokens(sharedPreferencesSetting: SharedPreferencesSetting) {
    sharedPreferencesSetting.accessToken = null
    sharedPreferencesSetting.refreshToken = null
}