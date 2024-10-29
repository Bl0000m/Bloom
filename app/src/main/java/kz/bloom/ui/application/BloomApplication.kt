package kz.bloom.ui.application

import android.app.Application
import kz.bloom.ui.application.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BloomApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BloomApplication)

            modules(appModule)
        }
    }
}