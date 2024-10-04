package kz.bloom.ui.intro.splash.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kz.bloom.R
import kz.bloom.ui.intro.splash.SplashActivity

@Composable
fun SplashContent(
    onAnimationFinish: () -> Unit
) {
    // Загружаем Lottie-анимацию
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,  // Один раз проигрываем
        restartOnPlay = false
    )

    // После окончания анимации вызываем onAnimationFinish
    if (progress == 1f) {
        LaunchedEffect(Unit) {
            onAnimationFinish()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black // Цвет фона для сплэш-экрана
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Анимация Lottie
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    SplashContent(onAnimationFinish = {})
}