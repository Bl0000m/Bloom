package kz.bloom.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kz.bloom.R

// Set of Material typography styles to start with

private val SanFranciscoPro: FontFamily = FontFamily(
    Font(
        resId = R.font.sf_pro_text_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.sf_pro_text_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.sf_pro_text_regular,
        weight = FontWeight.Normal
    )
)

private val Inter: FontFamily = FontFamily(
    Font(
        resId = R.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.inter_regular,
        weight = FontWeight.Normal
    )
)

val InterTypography = Typography(
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp
    )
)

val Typography = Typography(
    // Body text styles
    bodyLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // Title text styles
    titleLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // Headline styles
    headlineLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // Caption and button text styles
    labelLarge = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SanFranciscoPro,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
)