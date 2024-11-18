package kz.bloom.ui.application

import android.app.Application
import android.util.Base64
import androidx.lifecycle.LifecycleObserver
import kz.bloom.ui.application.module.appModule
import kz.bloom.ui.ui_components.preference.SecurityPreference
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.json.JSONObject
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

class BloomApplication : Application(), KoinComponent, LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BloomApplication)
            modules(appModule)
        }
    }
}