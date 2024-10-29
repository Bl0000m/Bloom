package kz.bloom.ui.intro.splash.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashContent(modifier: Modifier = Modifier, onLogoShown:() -> Unit) {
    onLogoShown()
}