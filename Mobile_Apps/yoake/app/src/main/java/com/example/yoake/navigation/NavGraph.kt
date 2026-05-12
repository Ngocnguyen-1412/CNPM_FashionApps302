package com.example.yoake.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.yoake.ui.screens.*
import com.example.yoake.ui.viewmodel.*

// ─── Screen routes ────────────────────────────────────────────────────────────

sealed class Screen(val route: String) {
    object Home          : Screen("home")
    object OrderHistory  : Screen("order_history")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Profile : Screen("profile")
}

// ─── NavGraph ─────────────────────────────────────────────────────────────────

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Home.route,
        enterTransition  = { slideInHorizontally(tween(320)) { it / 4 } + fadeIn(tween(320)) },
        exitTransition   = { slideOutHorizontally(tween(320)) { -it / 4 } + fadeOut(tween(320)) },
        popEnterTransition  = { slideInHorizontally(tween(320)) { -it / 4 } + fadeIn(tween(320)) },
        popExitTransition   = { slideOutHorizontally(tween(320)) { it / 4 } + fadeOut(tween(320)) }
    ) {

        // ── Home ──────────────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            val vm: HomeViewModel = viewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState        = uiState,
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }

        // ── Order history ─────────────────────────────────────────────────────
        composable(Screen.OrderHistory.route) {
            val vm: OrderHistoryViewModel = viewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            OrderHistoryScreen(
                uiState     = uiState,
                onBackClick = { navController.popBackStack() },
                onLoadMore  = vm::loadMore
            )
        }

        // ── Product detail ────────────────────────────────────────────────────
        composable(
            route     = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val vm: ProductDetailViewModel = viewModel()

            // Load product when this destination is first composed
            LaunchedEffect(productId) { vm.loadProduct(productId) }

            val uiState by vm.uiState.collectAsStateWithLifecycle()

            // Show snackbar / toast when added to cart
            LaunchedEffect(uiState.addedToCart) {
                if (uiState.addedToCart) vm.resetAddedToCart()
            }

            ProductDetailScreen(
                productId  = productId,
                uiState    = uiState,
                onBackClick = { navController.popBackStack() },
                onAddToCart = { color, size -> vm.addToCart(productId, color, size) }
            )
        }

        // ── Profile ───────────────────────────────────────────────────────────
        composable(Screen.Profile.route) {
            val vm: ProfileViewModel = viewModel()
            val uiState by vm.uiState.collectAsStateWithLifecycle()
            ProfileScreen(
                uiState              = uiState,
                onOrderHistoryClick  = { navController.navigate(Screen.OrderHistory.route) }
            )
        }
    }
}
