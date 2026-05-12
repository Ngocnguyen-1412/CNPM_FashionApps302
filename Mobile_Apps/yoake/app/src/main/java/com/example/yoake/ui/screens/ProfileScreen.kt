package com.example.yoake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.yoake.data.model.User
import com.example.yoake.ui.components.YoakeTopAppBar
import com.example.yoake.ui.theme.*

// ─── Data contract ────────────────────────────────────────────────────────────

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User?        = null
)

// ─── Profile navigation items ─────────────────────────────────────────────────

private data class ProfileNavItem(
    val icon: ImageVector,
    val label: String,
    val isDestructive: Boolean = false
)

private val profileNavItems = listOf(
    ProfileNavItem(Icons.Outlined.ReceiptLong,   "Order History"),
    ProfileNavItem(Icons.Outlined.LocationOn,    "Saved Addresses"),
    ProfileNavItem(Icons.Outlined.CreditCard,    "Payment Methods"),
    ProfileNavItem(Icons.Outlined.FavoriteBorder,"Wishlist"),
    ProfileNavItem(Icons.Outlined.Notifications, "Notifications"),
    ProfileNavItem(Icons.Outlined.Settings,      "Settings"),
)

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun ProfileScreen(
    uiState: ProfileUiState             = ProfileUiState(),
    onEditProfile: () -> Unit           = {},
    onOrderHistoryClick: () -> Unit     = {},
    onAddressesClick: () -> Unit        = {},
    onPaymentMethodsClick: () -> Unit   = {},
    onWishlistClick: () -> Unit         = {},
    onNotificationsClick: () -> Unit    = {},
    onSettingsClick: () -> Unit         = {},
    onSignOut: () -> Unit               = {},
    onMenuClick: () -> Unit             = {},
    onCartClick: () -> Unit             = {}
) {
    Scaffold(
        topBar = {
            YoakeTopAppBar(onMenuClick = onMenuClick, onCartClick = onCartClick)
        },
        containerColor = Background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 96.dp)
        ) {

            // ── Profile header ───────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainer)
                        .border(1.dp, OutlineVariant, CircleShape)
                ) {
                    if (uiState.user?.avatarUrl != null) {
                        AsyncImage(
                            model              = uiState.user.avatarUrl,
                            contentDescription = "Avatar",
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )
                    } else {
                        // Placeholder initials
                        Text(
                            text     = uiState.user?.displayName
                                ?.split(" ")
                                ?.mapNotNull { it.firstOrNull()?.toString() }
                                ?.take(2)
                                ?.joinToString("") ?: "",
                            style    = MaterialTheme.typography.displayMedium,
                            color    = OnSurfaceVariant,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Name
                Text(
                    uiState.user?.displayName ?: "",
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(Modifier.height(4.dp))

                // Email
                Text(
                    uiState.user?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))

                // Edit profile button
                OutlinedButton(
                    onClick        = onEditProfile,
                    shape          = MaterialTheme.shapes.extraSmall,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text("EDIT PROFILE", style = MaterialTheme.typography.labelSmall)
                }
            }

            HorizontalDivider(color = OutlineVariant.copy(alpha = 0.3f))

            // ── Navigation list ──────────────────────────────────────────────
            val clickHandlers = listOf(
                onOrderHistoryClick,
                onAddressesClick,
                onPaymentMethodsClick,
                onWishlistClick,
                onNotificationsClick,
                onSettingsClick
            )

            profileNavItems.forEachIndexed { index, item ->
                ProfileNavRow(
                    icon    = item.icon,
                    label   = item.label,
                    onClick = clickHandlers[index]
                )
                HorizontalDivider(color = OutlineVariant.copy(alpha = 0.3f))
            }

            Spacer(Modifier.height(32.dp))

            // ── Sign out ─────────────────────────────────────────────────────
            ProfileNavRow(
                icon        = Icons.Outlined.Logout,
                label       = "Sign Out",
                onClick     = onSignOut,
                isDestructive = true
            )
        }
    }
}

// ─── Row item ─────────────────────────────────────────────────────────────────

@Composable
private fun ProfileNavRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val contentColor = if (isDestructive) Error else OnSurface
    val iconColor    = if (isDestructive) Error else OnSurfaceVariant

    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(SurfaceContainerLowest)
            .padding(vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = iconColor,
                modifier           = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, color = contentColor)
        }
        if (!isDestructive) {
            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint               = OutlineVariant
            )
        }
    }
}
