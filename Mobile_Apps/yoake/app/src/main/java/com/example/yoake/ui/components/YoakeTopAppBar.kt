package com.example.yoake.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yoake.ui.theme.OnSurface
import com.example.yoake.ui.theme.Surface

/**
 * Two variants:
 *  - [YoakeTopAppBar]      — hamburger + brand + cart  (Home, Order History, Profile)
 *  - [YoakeDetailTopAppBar]— back arrow + brand + cart  (Product Detail)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoakeTopAppBar(
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    cartItemCount: Int = 0
) {
    TopAppBar(
        modifier = Modifier.height(64.dp),
        colors   = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, contentDescription = "Menu", tint = OnSurface)
            }
        },
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text  = "YOAKE",
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = "Cart", tint = OnSurface)
                }
                if (cartItemCount > 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset((-6).dp, 6.dp)
                    ) { Text("$cartItemCount") }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoakeDetailTopAppBar(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    cartItemCount: Int = 0
) {
    TopAppBar(
        modifier = Modifier.height(64.dp),
        colors   = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = OnSurface
                )
            }
        },
        title = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text  = "YOAKE",
                    style = MaterialTheme.typography.displayMedium
                )
            }
        },
        actions = {
            Box {
                IconButton(onClick = onCartClick) {
                    Icon(Icons.Outlined.ShoppingBag, contentDescription = "Cart", tint = OnSurface)
                }
                if (cartItemCount > 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset((-6).dp, 6.dp)
                    ) { Text("$cartItemCount") }
                }
            }
        }
    )
}
