package com.example.grocerystore.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.example.grocerystore.R

val interFamily = FontFamily(
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_black, FontWeight.Black),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_extrabold, FontWeight.ExtraBold),
    Font(R.font.inter_extralight, FontWeight.ExtraLight),
    Font(R.font.inter_light, FontWeight.Light),
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_thin, FontWeight.Thin)
)

val darkColorScheme = darkColorScheme(
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White,
    primary = Color(0xFF0A61C4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0A61C4),
    onPrimaryContainer = Color.White,
    inversePrimary = Color.White,
    secondary = Color(0xFF0A61C4),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF0A61C4),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF0A61C4),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF0A61C4),
    onTertiaryContainer = Color.White,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    outline = Color(0xFF0A61C4),
    outlineVariant = Color(0xFF0A61C4),
    scrim = Color.Black.copy(alpha = 0.8f)
)

val lightColorScheme = lightColorScheme(
    primary = Color(0xFF0A61C4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0A61C4),
    onPrimaryContainer = Color.White,
    inversePrimary = Color.White,
    secondary = Color(0xFF0A61C4),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF0A61C4),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF0A61C4),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF0A61C4),
    onTertiaryContainer = Color.White,
    background = Color.White,
    onBackground = Color(0xFF2F3137),
    surface = Color(0xFF0A61C4),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF0A61C4),
    onSurfaceVariant = Color.White,
    surfaceTint = Color(0xFF0A61C4),
    inverseSurface = Color.White,
    inverseOnSurface = Color(0xFF2F3137),
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.White,
    outline = Color(0xFF0A61C4),
    outlineVariant = Color(0xFF0A61C4),
    scrim = Color.Black.copy(alpha = 0.5f)
)

@Composable
fun GroceryStoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}