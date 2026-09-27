package com.bitchat.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bitchat.android.R
import com.bitchat.android.ui.theme.BitchatFontFamily
import com.bitchat.android.ui.theme.LocalBitchatPalette

/**
 * PeerAvatar — círculo con la inicial del peer, badges de estado y anillo de transporte.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * REDESIGN (ui/beautify branch) — qué cambió y por qué:
 * ────────────────────────────────────────────────────────────────────────────
 * 1. Fondo del círculo: pasó de un fill uniforme (`color.copy(alpha = 0.16f)`)
 *    a un gradiente radial (`0.24f` en el centro → `0.10f` en el borde). El
 *    fill uniforme se veía plano contra el fondo oscuro; el gradiente aporta
 *    profundidad sin cambiar el tamaño ni la posición del avatar.
 * 2. Borde sutil de 1dp con `color.copy(alpha = 0.35f)`: separa el círculo del
 *    fondo de la lista. Antes el avatar "flotaba" sin anclaje visual.
 * 3. NUEVO parámetro opcional `transportColor: Color? = null`. Si el caller lo
 *    pasa, se dibuja un anillo de 2dp alrededor del círculo con ese color. El
 *    anillo comunica visualmente por qué transporte está conectado el peer
 *    (verde mesh, púrpura nostr, ámbar relay, cian wifi-aware). Es opcional
 *    para no romper los callers existentes que no lo necesiten.
 *
 * Sin cambios:
 *  - Estructura de badges (TopEnd, TopStart, BottomEnd).
 *  - Lógica de favoritos (`isFavorite`, `theyFavoritedUs`).
 *  - Verificación (`isVerified`) con `colorScheme.primary` (que ahora es azul
 *    tras el cambio de tema, coherente con el resto de la UI).
 *  - Tamaños base (42dp contenedor, 38dp círculo, 18dp badge).
 *  - Fuente Geist Mono (`BitchatFontFamily`).
 *  - El color del peer sigue viniendo del caller (byte-identical con iOS).
 * ────────────────────────────────────────────────────────────────────────────
 */

internal val PeerAvatarBadgeSize = 18.dp
private val PeerAvatarStarSize = 16.dp
private val PeerAvatarVerifiedSize = 16.dp

/** Diámetro del círculo interior que contiene la inicial. */
private val PeerAvatarInnerSize = 38.dp

/** Grosor del anillo de transporte cuando se pasa `transportColor`. */
private val PeerAvatarTransportRingWidth = 2.dp

@Composable
internal fun PeerAvatar(
    name: String,
    color: Color,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    theyFavoritedUs: Boolean = false,
    isVerified: Boolean = false,
    /**
     * Color del anillo de transporte. Si es `null`, no se dibuja anillo.
     *
     * El caller puede pasar `palette.transportMesh`, `palette.transportNostr`,
     * `palette.transportRelay`, `palette.transportWifiAware`, o cualquier color
     * que represente el transporte activo del peer en el contexto actual.
     *
     * Es opcional para no romper los callers existentes (listas de conversación,
     * sheets, etc.) que no necesitan distinguir transporte.
     */
    transportColor: Color? = null,
    badge: (@Composable () -> Unit)? = null
) {
    val palette = LocalBitchatPalette.current
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier.size(42.dp),
        contentAlignment = Alignment.Center
    ) {
        // ─────────────────────────────────────────────────────────────────
        // Anillo de transporte (opcional). Se dibuja DEBAJO del círculo
        // interior para que el borde del círculo quede por encima y no se
        // solapen. El anillo es un Box vacío con borde circular de 2dp.
        // ─────────────────────────────────────────────────────────────────
        if (transportColor != null) {
            Box(
                modifier = Modifier
                    .size(PeerAvatarInnerSize)
                    .border(
                        width = PeerAvatarTransportRingWidth,
                        color = transportColor.copy(alpha = 0.85f),
                        shape = CircleShape
                    )
            )
        }

        // ─────────────────────────────────────────────────────────────────
        // Círculo interior con gradiente radial + borde sutil.
        // El gradiente va de alpha 0.24 (centro) a 0.10 (borde) para dar
        // profundidad. El borde de 1dp separa el avatar del fondo de la lista.
        // ─────────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(PeerAvatarInnerSize)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.24f),
                            color.copy(alpha = 0.10f)
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.35f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.trim().firstOrNull()?.uppercase() ?: "#",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = BitchatFontFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = color
            )
        }

        if (badge != null) {
            Surface(
                modifier = Modifier
                    .size(PeerAvatarBadgeSize)
                    .align(Alignment.BottomEnd),
                shape = CircleShape,
                color = colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    badge()
                }
            }
        }

        if (isFavorite || theyFavoritedUs) {
            Surface(
                modifier = Modifier
                    .size(PeerAvatarStarSize)
                    .align(Alignment.TopEnd),
                shape = CircleShape,
                color = colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(
                            if (isFavorite) {
                                R.drawable.ic_spec_star_filled
                            } else {
                                R.drawable.ic_spec_star
                            }
                        ),
                        contentDescription = stringResource(
                            if (isFavorite) {
                                R.string.cd_favorite
                            } else {
                                R.string.cd_favorited_you
                            }
                        ),
                        modifier = Modifier.size(10.dp),
                        tint = palette.accentOrange
                    )
                }
            }
        }

        if (isVerified) {
            Surface(
                modifier = Modifier
                    .size(PeerAvatarVerifiedSize)
                    .align(Alignment.TopStart),
                shape = CircleShape,
                color = colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = stringResource(
                            R.string.fingerprint_verified_label
                        ),
                        modifier = Modifier.size(12.dp),
                        tint = colorScheme.primary
                    )
                }
            }
        }
    }
}