package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.giao_dien.*
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.SanPhamViewModel
import com.example.myapplication.viewmodel.GioHangViewModel
import com.example.myapplication.viewmodel.NguoiDungViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val sanPhamViewModel: SanPhamViewModel = viewModel()
                val gioHangViewModel: GioHangViewModel = viewModel()
                val nguoiDungViewModel: NguoiDungViewModel = viewModel()
                val context = LocalContext.current

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        val mainScreens = listOf("trang_chu", "ho_so")
                        if (currentRoute in mainScreens) {
                            NavigationBar(
                                containerColor = Color.White,
                                tonalElevation = 8.dp
                            ) {
                                NavigationBarItem(
                                    selected = currentRoute == "trang_chu",
                                    onClick = {
                                        if (currentRoute != "trang_chu") {
                                            navController.navigate("trang_chu") {
                                                popUpTo("trang_chu") { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                                    label = { Text("Trang chủ") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "ho_so",
                                    onClick = {
                                        if (currentRoute != "ho_so") {
                                            navController.navigate("ho_so") {
                                                popUpTo("trang_chu") { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Tài khoản") },
                                    label = { Text("Tôi") }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "man_hinh_chao",
                        modifier = Modifier.padding(paddingValues),
                        enterTransition = { fadeIn(animationSpec = tween(300)) },
                        exitTransition = { fadeOut(animationSpec = tween(300)) }
                    ) {
                        composable("man_hinh_chao") {
                            ManHinhChao(onNavigateToNext = {
                                navController.navigate("dang_nhap") {
                                    popUpTo("man_hinh_chao") { inclusive = true }
                                }
                            })
                        }
                        composable("dang_nhap") {
                            ManHinhDangNhap(
                                onDangNhapThanhCong = { user ->
                                    nguoiDungViewModel.thietLapNguoiDung(user)
                                    if (user.vaiTro == "admin") {
                                        navController.navigate("admin_panel") {
                                            popUpTo("dang_nhap") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("trang_chu") {
                                            popUpTo("dang_nhap") { inclusive = true }
                                        }
                                    }
                                },
                                onChuyenSangDangKy = { navController.navigate("dang_ky") }
                            )
                        }
                        composable("dang_ky") {
                            ManHinhDangKy(onDangKyThanhCong = { navController.popBackStack() }, onBackToLogin = { navController.popBackStack() })
                        }
                        composable("admin_panel") {
                            ManHinhAdmin(onLogout = {
                                nguoiDungViewModel.dangXuat()
                                navController.navigate("dang_nhap") { popUpTo(0) { inclusive = true } }
                            })
                        }
                        composable("trang_chu") {
                            ManHinhChinh(
                                viewModel = sanPhamViewModel,
                                onSanPhamClick = { id -> navController.navigate("chi_tiet/$id") },
                                onCartClick = { navController.navigate("gio_hang") }
                            )
                        }
                        composable("ho_so") {
                            ManHinhHoSo(
                                viewModel = nguoiDungViewModel,
                                onLogout = {
                                    nguoiDungViewModel.dangXuat()
                                    navController.navigate("dang_nhap") { popUpTo(0) { inclusive = true } }
                                },
                                onNavigateToOrders = { trangThai -> navController.navigate("don_hang/$trangThai") },
                                onEditProfile = { navController.navigate("sua_ho_so") },
                                onNavigateToFavorites = { navController.navigate("yeu_thich") },
                                onNavigateToAddress = { navController.navigate("dia_chi") },
                                onNavigateToHelp = { navController.navigate("ho_tro") }
                            )
                        }
                        composable("sua_ho_so") { ManHinhSuaHoSo(viewModel = nguoiDungViewModel, onBack = { navController.popBackStack() }) }
                        composable("yeu_thich") { 
                            ManHinhYeuThich(
                                nguoiDungViewModel = nguoiDungViewModel, 
                                onBack = { navController.popBackStack() },
                                onSanPhamClick = { id -> navController.navigate("chi_tiet/$id") }
                            ) 
                        }
                        composable("dia_chi") { 
                            ManHinhDiaChi(
                                nguoiDungViewModel = nguoiDungViewModel, 
                                onBack = { navController.popBackStack() }
                            ) 
                        }
                        composable("ho_tro") { ManHinhHoTro(onBack = { navController.popBackStack() }) }
                        
                        composable(
                            route = "don_hang/{trangThai}",
                            arguments = listOf(navArgument("trangThai") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val trangThai = backStackEntry.arguments?.getString("trangThai") ?: ""
                            ManHinhDonHang(
                                nguoiDungId = nguoiDungViewModel.nguoiDungHienTai.value?.id ?: 0,
                                trangThai = trangThai, 
                                onBack = { navController.popBackStack() },
                                onChiTietDonHang = { id -> navController.navigate("chi_tiet_don_hang/$id") }
                            )
                        }

                        composable(
                            route = "chi_tiet_don_hang/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            ManHinhChiTietDonHang(maDonHang = id, onBack = { navController.popBackStack() })
                        }

                        composable(
                            route = "chi_tiet/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: 0
                            ManHinhChiTiet(
                                sanPhamId = id, 
                                gioHangViewModel = gioHangViewModel, 
                                nguoiDungViewModel = nguoiDungViewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }
                        
                        composable("gio_hang") {
                            ManHinhGioHang(
                                userId = nguoiDungViewModel.nguoiDungHienTai.value?.id ?: 0,
                                viewModel = gioHangViewModel, 
                                onBack = { navController.popBackStack() }, 
                                onThanhToan = { navController.navigate("thanh_toan") }
                            )
                        }

                        composable("thanh_toan") {
                            ManHinhThanhToan(
                                userId = nguoiDungViewModel.nguoiDungHienTai.value?.id ?: 0,
                                gioHangViewModel = gioHangViewModel,
                                onBack = { navController.popBackStack() },
                                onSuccess = {
                                    navController.navigate("thanh_cong") {
                                        popUpTo("gio_hang") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("thanh_cong") {
                            ManHinhThanhCong(
                                onTiepTucMuaSam = {
                                    navController.navigate("trang_chu") {
                                        popUpTo("trang_chu") { inclusive = true }
                                    }
                                },
                                onXemDonHang = {
                                    navController.navigate("don_hang/cho_xac_nhan") {
                                        popUpTo("trang_chu")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
