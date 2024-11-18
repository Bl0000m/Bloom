package kz.bloom.ui.application.module

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

import kotlinx.serialization.json.Json


import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.api.AuthApiClient
import kz.bloom.ui.main.data.MainRepository

import okhttp3.logging.HttpLoggingInterceptor

import org.koin.core.qualifier.named
import org.koin.dsl.module

import kotlinx.coroutines.Dispatchers
import kz.bloom.ui.auth.api.entity.ErrorResponse
import kz.bloom.ui.ui_components.preference.SecurityPreference
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
//import kz.bloom.ui.ui_components.preference.TokenAuthenticator
import okhttp3.Authenticator
import okio.IOException
import org.koin.android.ext.koin.androidContext
import kotlin.coroutines.CoroutineContext

class ApiException(val error: ErrorResponse) : IOException(error.message)

private val json = Json {
    isLenient = true
    prettyPrint = false
    encodeDefaults = true
    ignoreUnknownKeys = true
    useArrayPolymorphism = false
    allowStructuredMapKeys = true
    allowSpecialFloatingPointValues = true
}

val appModule = module {
    single { MainRepository() }
    single<CoroutineContext>(qualifier = named("Main")) { Dispatchers.Main.immediate }
    single<CoroutineContext>(qualifier = named("IO")) { Dispatchers.IO }
    single<StoreFactory> { DefaultStoreFactory() }

    //single<Authenticator> { TokenAuthenticator() }

    single {
        HttpClient(OkHttp) {
            defaultRequest {
                url("http://api.bloooom.kz:8443")
            }
            install(io.ktor.client.plugins.HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 15000
            }
            install(plugin = ContentNegotiation.Plugin) {
                json(json = json)
            }
            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val errorBody = response.bodyAsText()
                        val errorResponse = try {
                            json.decodeFromString<ErrorResponse>(errorBody)
                        } catch (e: Exception) {
                            throw IOException("Failed to parse error response", e)
                        }
                        throw ApiException(errorResponse)
                    }
                }
            }
            engine {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                config {
                    retryOnConnectionFailure(true)
                    addInterceptor(loggingInterceptor)
                        //authenticator(authenticator = get())
                }
            }
        }
    }

    single { SharedPreferencesSetting(androidContext()) }
    single { SecurityPreference(androidContext()) }

    single<AuthApi> { AuthApiClient(client = get()) }
}