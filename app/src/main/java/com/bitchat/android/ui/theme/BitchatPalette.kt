package com.bitchat.android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Bitchat-specific color tokens that do not have a faithful Material 3 semantic role.
 *
 * Standard backgrounds, surfaces, text, outlines, primary/secondary accents, and errors belong
 * to [androidx.compose.material3.MaterialTheme.colorScheme]. Keeping only the extra app semantics
 * here lets Material components inherit correct defaults without losing Bitchat's identity.
 */
@Immutable
data class BitchatPalette(
    // MARK: - Form controls
    /**
     * Resting border for text inputs. Deliberately a neutral grey rather than a tinted
     * Material outline: the composer is the one surface the user stares at while typing.
     */
    val inputOutline: Color,
    /** Border for a focused text input. A step brighter, still neutral. */
    val inputOutlineFocused: Color,
    /** Fill for text inputs. Untinted, so it does not compound with the tinted scrim. */
    val inputSurface: Color,
    /** Fill for a focused text input. A barely perceptible lift. */
    val inputSurfaceFocused: Color,
    /** Resting disc behind the composer's action glyphs. Neutral grey. */
    val inputButton: Color,

    // MARK: - Extra semantics
    /** Timestamps, placeholders, section labels, disabled states. */
    val textTertiary: Color,
    /** Self, mentions targeting you, unread DMs. */
    val accentOrange: Color,
    /** Nostr reachability. */
    val accentPurple: Color,

    // MARK: - Transport identity
    /**
     * Mesh local vía Bluetooth LE. Es el transporte "hero" de la app: verde se lee como
     * "conectado / luz verde". Se alinea con el verde de iOS (#32D74B) para paridad.
     */
    val transportMesh: Color,
    /** Nostr (internet global). Púrpura iOS. */
    val transportNostr: Color,
    /** Wi-Fi Aware (mesh de alta banda en dispositivos compatibles). Cian iOS. */
    val transportWifiAware: Color,
    /** Peer alcanzable solo por relay multi-hop. Amarillo iOS como advertencia suave. */
    val transportRelay: Color,

    // MARK: - Message delivery states
    /** Mensaje en cola, aún no confirmado por la red. */
    val messageSending: Color,
    /** Mensaje aceptado localmente y enviado al transporte. */
    val messageSent: Color,
    /** Confirmación de entrega end-to-end recibida. */
    val messageDelivered: Color,
    /** Fallo definitivo tras reintentos. */
    val messageFailed: Color,

    // MARK: - Surface elevation
    /**
     * Superficie por encima del `surface` base de Material (sheets, cards flotantes,
     * popovers). Antes no existía y todo caía al `surfaceVariant`.
     */
    val surfaceElevated: Color,
    /** Superficie por debajo del `surface` base (wells, insets hundidos). */
    val surfaceSunken: Color,

    // MARK: - Deterministic peer colors
    /**
     * Saturation/value applied after deriving a peer's stable hue. Swap this when adding a
     * new theme — see [PeerColorStyle] for contrast guidelines.
     */
    val peerColors: PeerColorStyle,
)

val DarkBitchatPalette = BitchatPalette(
    // Form controls — sin cambios
    inputOutline = Color(0xFF333635),
    inputOutlineFocused = Color(0xFF5A605D),
    inputSurface = Color(0xFF0B0B0B),
    inputSurfaceFocused = Color(0xFF151515),
    inputButton = Color(0xFF1E1E1E),

    // Extra semantics — sin cambios
    textTertiary = Color(0xFF6B776B),
    accentOrange = Color(0xFFFF9F0A),
    accentPurple = Color(0xFFBF5AF2),

    // Transport identity — verde mesh, azul ya no es transport (es el primary global)
    transportMesh = Color(0xFF32D74B),
    transportNostr = Color(0xFFBF5AF2),
    transportWifiAware = Color(0xFF64D2FF),
    transportRelay = Color(0xFFFFD60A),

    // Message delivery states
    messageSending = Color(0xFF8E8E93),
    messageSent = Color(0xFF0A84FF),
    messageDelivered = Color(0xFF32D74B),
    messageFailed = Color(0xFFFF453A),

    // Surface elevation
    surfaceElevated = Color(0xFF1A231A),
    surfaceSunken = Color(0xFF070A07),

    peerColors = PeerColorStyle.Dark,
)

val LightBitchatPalette = BitchatPalette(
    // Form controls — sin cambios
    inputOutline = Color(0xFFCFD3D1),
    inputOutlineFocused = Color(0xFF8E9490),
    inputSurface = Color(0xFFFAFAFA),
    inputSurfaceFocused = Color(0xFFF2F2F2),
    inputButton = Color(0xFFE8E8E8),

    // Extra semantics — sin cambios
    textTertiary = Color(0xFF757F75),
    accentOrange = Color(0xFFFF9500),
    accentPurple = Color(0xFFAF52DE),

    // Transport identity
    transportMesh = Color(0xFF248A3D),
    transportNostr = Color(0xFF9B3FCC),
    transportWifiAware = Color(0xFF007AFF),
    transportRelay = Color(0xFFB58900),

    // Message delivery states
    messageSending = Color(0xFF8E8E93),
    messageSent = Color(0xFF007AFF),
    messageDelivered = Color(0xFF248A3D),
    messageFailed = Color(0xFFD70015),

    // Surface elevation
    surfaceElevated = Color(0xFFFFFFFF),
    surfaceSunken = Color(0xFFEAEFEA),

    peerColors = PeerColorStyle.Light,
)

val LocalBitchatPalette = staticCompositionLocalOf { DarkBitchatPalette }

/**
 * Motion tokens. The redesign leans on short, snappy transitions: long durations read as
 * sluggish on a chat surface where the user is scanning quickly.
 */
object BitchatMotion {
    /** Icon tints, text colors, small fills. */
    const val QUICK_MS = 120

    /** Tab indicators, pill growth, chip reveals. */
    const val STANDARD_MS = 180

    /** Sheet-level fades and scroll-driven top bars. */
    const val EMPHASIZED_MS = 240
}