package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = CrescentGreenPrimaryDark,
    onPrimary = CrescentGreenOnPrimaryDark,
    primaryContainer = CrescentGreenContainerDark,
    onPrimaryContainer = CrescentGreenOnContainerDark,
    secondary = CrescentGoldSecondaryDark,
    onSecondary = CrescentGoldOnSecondaryDark,
    secondaryContainer = CrescentGoldContainerDark,
    onSecondaryContainer = CrescentGoldOnContainerDark,
    background = CrescentBackgroundDark,
    onBackground = CrescentOnBackgroundDark,
    surface = CrescentSurfaceDark,
    onSurface = CrescentOnSurfaceDark,
    surfaceVariant = CrescentSurfaceVariantDark,
    onSurfaceVariant = CrescentOnSurfaceVariantDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = CrescentGreenPrimary,
    onPrimary = CrescentGreenOnPrimary,
    primaryContainer = CrescentGreenContainer,
    onPrimaryContainer = CrescentGreenOnContainer,
    secondary = CrescentGoldSecondary,
    onSecondary = CrescentGoldOnSecondary,
    secondaryContainer = CrescentGoldContainer,
    onSecondaryContainer = CrescentGoldOnContainer,
    tertiary = CrescentTertiary,
    onTertiary = CrescentOnTertiary,
    tertiaryContainer = CrescentTertiaryContainer,
    onTertiaryContainer = CrescentOnTertiaryContainer,
    background = CrescentBackgroundLight,
    onBackground = CrescentOnBackgroundLight,
    surface = CrescentSurfaceLight,
    onSurface = CrescentOnSurfaceLight,
    surfaceVariant = CrescentSurfaceVariantLight,
    onSurfaceVariant = CrescentOnSurfaceVariantLight
  )

@Composable
fun CrescentTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep brand aesthetic consistent by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
