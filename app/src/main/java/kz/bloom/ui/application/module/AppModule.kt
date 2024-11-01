package kz.bloom.ui.application.module

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest


import kz.bloom.ui.auth.api.AuthApi
import kz.bloom.ui.auth.api.AuthApiClient
import kz.bloom.ui.main.data.MainRepository

import okhttp3.logging.HttpLoggingInterceptor

import org.koin.core.qualifier.named
import org.koin.dsl.module

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

val appModule = module {
    single { MainRepository() }
    single<CoroutineContext>(qualifier = named("Main")) { Dispatchers.Main.immediate }
    single<CoroutineContext>(qualifier = named("IO")) { Dispatchers.IO }
    single<StoreFactory> { DefaultStoreFactory() }

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
            engine {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                config {
                    retryOnConnectionFailure(true)
                    addInterceptor(loggingInterceptor)
                }
            }
        }
    }

    single<AuthApi> { AuthApiClient(client = get()) }
}