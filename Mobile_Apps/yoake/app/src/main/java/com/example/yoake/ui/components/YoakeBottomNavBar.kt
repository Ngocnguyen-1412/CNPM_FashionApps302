package com.example.yoake.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.yoake.navigation.Screen
import com.example.yoake.ui.theme.OnSurface
import com.example.yoake.ui.theme.Secondary
import com.example.yoake.ui.theme.Surface

// ─── Bottom tab definition ───────────────────────────────────────────────────
private data class BottomTab(
    val screen: Screen,
    val label: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit,
    val contentDescription: String
)

private val tabs = listOf(
    BottomTab(
        screen              = Screen.Home,
        label               = "Home",
        selectedIcon        = { Icon(Icons.Filled.Home, null) },
        unselectedIcon      = { Icon(Icons.Outlined.Home, null) },
        contentDescription  = "Home"
    ),
    BottomTab(
        screen              = Screen.Home,   // swap for a real Search screen when added
        label               = "Search",
        selectedIcon        = { Icon(Icons.Outlined.Search, null) },
        unselectedIcon      = { Icon(Icons.Outlined.Search, null) },
        contentDescription  = "Search"
    ),
    BottomTab(
        screen              = Screen.Home,   // swap for a Wishlist screen when added
        label               = "Wishlist",
        selectedIcon        = { Icon(Icons.Outlined.Favorite, null) },
        unselectedIcon      = { Icon(Icons.Outlined.FavoriteBorder, null) },
        contentDescription  = "Wishlist"
    ),
    BottomTab(
        screen              = Screen.Profile,
        label               = "Profile",
        selectedIcon        = { Icon(Icons.Filled.Person, null) },
        unselectedIcon      = { Icon(Icons.Outlined.Person, null) },
        contentDescription  = "Profile"
    )
)

@Composable
fun YoakeBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier         = Modifier.height(72.dp),
        containerColor   = Surface.copy(alpha = 0.92f),
        tonalElevation   = 0.dp
    ) {
        tabs.forEach { tab ->
            val selected = currentDestination?.hierarchy
                ?.any { it.route == tab.screen.route } == true

            NavigationBarItem(
                selected = selected,
                onClick  = {
                    navController.navigate(tab.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState    = true
                    }
                },
                icon  = { if (selected) tab.selectedIcon() else tab.unselectedIcon() },
                label = {
                    Text(
                        tab.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = OnSurface,
                    unselectedIconColor = Secondary,
                    selectedTextColor   = OnSurface,
                    unselectedTextColor = Secondary,
                    indicatorColor      = Surface
                )
            )
        }
    }
}
