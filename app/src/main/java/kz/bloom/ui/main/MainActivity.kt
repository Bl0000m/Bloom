package kz.bloom.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import kz.bloom.ui.main.VM.ImageListViewModel
import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.content.MainContent
import kz.bloom.ui.main.data.MainRepository
import kz.bloom.ui.theme.BloomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MainContent(vm = ImageListViewModel(repository = MainRepository()))
                }
            }
        }
    }
}