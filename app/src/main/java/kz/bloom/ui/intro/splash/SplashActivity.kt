package kz.bloom.ui.intro.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import kz.bloom.ui.intro.splash.content.SplashContent
import kz.bloom.ui.main.MainActivity


class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashContent(
                onAnimationFinish = {
                    // После окончания анимации переходим на главный экран
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()  // Закрываем SplashActivity
                }
            )
        }
    }
}