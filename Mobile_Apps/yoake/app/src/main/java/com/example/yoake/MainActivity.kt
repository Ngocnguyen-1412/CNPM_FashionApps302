package com.example.yoake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.yoake.navigation.NavGraph
import com.example.yoake.navigation.Screen
import com.example.yoake.ui.components.YoakeBottomNavBar
import com.example.yoake.ui.theme.YoakeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YoakeTheme {
                YoakeApp()
            }
        }
    }
}


@Composable
fun YoakeApp() {
    val navController = rememberNavController()


    val currentBackEntry by navController.currentBackStackEntryAsState()
    val currentRoute     = currentBackEntry?.destination?.route
    val showBottomBar    = currentRoute != Screen.ProductDetail.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                YoakeBottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  NavGraph reference — kept in navigation/NavGraph.kt
//  All screens are in ui/screens/
//  All shared UI components are in ui/components/
//  Design tokens are in ui/theme/
//
//  To integrate your API / database:
//    1. Add a ViewModel per screen (e.g. HomeViewModel, OrderHistoryViewModel).
//    2. Inject your repository into the ViewModel with Hilt (@HiltViewModel).
//    3. Pass the ViewModel's uiState down into each Screen composable.
//    4. Replace the default empty UiState values in each Screen's parameter list.
// ─────────────────────────────────────────────────────────────────────────────
