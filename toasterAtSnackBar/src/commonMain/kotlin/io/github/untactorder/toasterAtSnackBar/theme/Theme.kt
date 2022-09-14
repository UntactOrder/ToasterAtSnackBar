package io.github.untactorder.toasterAtSnackBar.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

val DarkColorScheme = darkColorScheme(
    primary = SignatureColorsDark.red,
    onPrimary = SignatureColorsDark.text,
    secondary = SignatureColorsDark.yellow,
    onSecondary = SignatureColorsDark.text,
    tertiary = SignatureColorsDark.purple,
    onTertiary = SignatureColorsDark.text,
    background = SignatureColorsDark.background,
    onBackground = SignatureColorsDark.background,
    surface = SignatureColorsDark.background,
    onSurface = SignatureColorsDark.background
)

val LightColorScheme = lightColorScheme(
    primary = SignatureColorsLight.red,
    onPrimary = SignatureColorsLight.text,
    secondary = SignatureColorsLight.yellow,
    onSecondary = SignatureColorsLight.text,
    tertiary = SignatureColorsLight.purple,
    onTertiary = SignatureColorsLight.text,
    background = SignatureColorsLight.background,
    onBackground = SignatureColorsLight.background,
    surface = SignatureColorsLight.background,
    onSurface = SignatureColorsLight.background
)

@Composable
fun ColorScheme.getSignature(darkTheme: Boolean = isSystemInDarkTheme()): SignatureColors {
    return if (darkTheme) SignatureColorsDark else SignatureColorsLight
}

val UntactOrderRoundRadius = 24.dp
val UntactOrderElevation = 7.dp

@Composable
fun chooseColorScheme(darkTheme: Boolean = isSystemInDarkTheme()): ColorScheme {
    return if (darkTheme) DarkColorScheme else LightColorScheme
}
