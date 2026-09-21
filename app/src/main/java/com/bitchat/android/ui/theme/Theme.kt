package com.bitchat.android.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

// Standard UI semantics live in Material so stock components and custom Bitchat composables
// share one source of truth. LocalBitchatPalette below only supplies app-specific extra colors.
//
// Redesign notes:
// - Primary is now iOS blue (#007AFF) across both themes; the previous green is reserved
//   for mesh-active states via BitchatPalette.transportMesh.
// - Dark background lifted from #000000 to #0A0F0A so surface/surfaceVariant read as
//   distinct elevation levels instead of collapsing into one flat black.
internal val DarkBitchatColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF082E54),
    onPrimaryContainer = Color(0xFFC2E0FF),
    secondary = Color(0xFF32D74B),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF163D1D),
    onSecondaryContainer = Color(0xFFB8F5C1),
    tertiary = DarkBitchatPalette.accentOrange,
    onTertiary = Color.Black,
    background = Color(0xFF0A0F0A),
    onBackground = Color(0xFFF5F5F5),
    surface = Color(0xFF111A11),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF1C261C),
    onSurfaceVariant = Color(0xFF9AA69A),
    outline = Color(0xFF2A3A2A),
    outlineVariant = Color(0xFF1C271C),
    error = Color(0xFFFF453A),
    onError = Color.Black
)

internal val LightBitchatColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E9FF),
    onPrimaryContainer = Color(0xFF002C5C),
    secondary = Color(0xFF248A3D),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5F1D8),
    onSecondaryContainer = Color(0xFF0A3212),
    tertiary = LightBitchatPalette.accentOrange,
    onTertiary = Color.Black,
    // Warm white instead of pure #FFFFFF: less glare on long sessions.
    background = Color(0xFFFAFBF9),
    onBackground = Color(0xFF131A13),
    surface = Color(0xFFF2F6F2),
    onSurface = Color(0xFF131A13),
    surfaceVariant = Color(0xFFE7EDE7),
    onSurfaceVariant = Color(0xFF4C574C),
    outline = Color(0xFFCBD6CB),
    outlineVariant = Color(0xFFDEE6DE),
    error = Color(0xFFD70015),
    onError = Color.White
)

/**
 * Formas unificadas de la app. Antes se usaba [Shapes] por defecto de Material 3, que es
 * más conservador; estos valores alinean las cards y sheets con el radio 16dp que ya
 * usaba [ChatVisualTokens.BubbleCornerRadius].
 */
internal val BitchatShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun BitchatTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    // App-level override from ThemePreferenceManager
    val themePref by ThemePreferenceManager.themeFlow.collectAsState(initial = ThemePreference.System)
    val shouldUseDark = when (darkTheme) {
        true -> true
        false -> false
        null -> when (themePref) {
            ThemePreference.Dark -> true
            ThemePreference.Light -> false
            ThemePreference.System -> isSystemInDarkTheme()
        }
    }

    val colorScheme = if (shouldUseDark) DarkBitchatColorScheme else LightBitchatColorScheme
    val palette = if (shouldUseDark) DarkBitchatPalette else LightBitchatPalette

    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    if (!shouldUseDark) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = if (!shouldUseDark) {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else 0
            }
            window.navigationBarColor = colorScheme.background.toArgb()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }
    }

    CompositionLocalProvider(LocalBitchatPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = BitchatShapes,
            content = content
        )
    }
}