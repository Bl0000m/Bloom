package kz.bloom.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kz.bloom.ui.main.VM.ImageListViewModel
import kz.bloom.ui.main.content.MainContent
import kz.bloom.ui.main.content.SplashMainContentAnimation
import kz.bloom.ui.main.data.MainRepository
import kz.bloom.ui.theme.BloomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    var isAnimationFinished by remember { mutableStateOf(false) }
                    SplashMainContentAnimation(modifier = Modifier.fillMaxSize(), onAnimationFinish = { isAnimationFinished = true} )
                    if (isAnimationFinished) {
                        MainContent(vm = ImageListViewModel(repository = MainRepository()))
                    }
                }
            }
        }
    }
}