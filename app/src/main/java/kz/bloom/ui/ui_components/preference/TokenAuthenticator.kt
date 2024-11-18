package kz.bloom.ui.ui_components.preference

import kotlinx.coroutines.runBlocking
import kz.bloom.ui.auth.api.AuthApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//class TokenAuthenticator : Authenticator, KoinComponent {
//
//    private val sharedPreferences by inject<SharedPreferencesSetting>()
//    private val authApi by inject<AuthApi>()
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//        val refreshToken = sharedPreferences.refreshToken ?: return null
//
//        val newAccessToken = runBlocking {
//            try {
//                val authResponse = authApi.refreshAccessToken(refreshToken)
//                sharedPreferences.accessToken = authResponse.accessToken
//            } catch (e: Exception) {
//                null
//            }
//        }
//
//        return newAccessToken?.let {
//            response.request.newBuilder()
//                .header("Authorization", "Bearer $it")
//                .build()
//        }
//    }
//}