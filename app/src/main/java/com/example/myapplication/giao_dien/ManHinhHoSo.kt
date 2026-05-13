package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PinkPrimary
import com.example.myapplication.viewmodel.NguoiDungViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhHoSo(
    viewModel: NguoiDungViewModel,
    onLogout: () -> Unit,
    onNavigateToOrders: (String) -> Unit,
    onEditProfile: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToAddress: () -> Unit,
    onNavigateToHelp: () -> Unit
) {
    val user = viewModel.nguoiDungHienTai.value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tài khoản của tôi", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFFDFDFD))
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        modifier = Modifier.size(110.dp),
                        shape = CircleShape,
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(2.dp, PinkPrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(20.dp),
                            tint = PinkPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        user?.hoTen ?: "Bùi Kim Hoàng",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        user?.email ?: "buikimhoang@gmail.com",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onEditProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(36.dp).width(120.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Sửa hồ sơ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // My Orders Section
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Đơn hàng của tôi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { onNavigateToOrders("tat_ca") }) {
                    Text("Xem lịch sử", color = Color(0xFFFF69B4), fontSize = 13.sp)
                }
            }
            
            Card(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OrderStateItem(Icons.Default.CreditCard, "Chờ xác nhận") { onNavigateToOrders("cho_xac_nhan") }
                    OrderStateItem(Icons.Default.LocalShipping, "Đang giao") { onNavigateToOrders("dang_giao") }
                    OrderStateItem(Icons.Default.AssignmentTurnedIn, "Đã giao") { onNavigateToOrders("da_giao") }
                    OrderStateItem(Icons.Default.Stars, "Đánh giá") { onNavigateToOrders("danh_gia") }
                }
            }

            // Utilities Section
            Text(
                "Tiện ích", 
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp), 
                fontWeight = FontWeight.Bold, 
                color = Color.Gray,
                fontSize = 15.sp
            )
            
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                ProfileMenuItem(Icons.Default.Favorite, "Sản phẩm yêu thích", onNavigateToFavorites)
                ProfileMenuItem(Icons.Default.LocationOn, "Địa chỉ nhận hàng", onNavigateToAddress)
                ProfileMenuItem(Icons.Default.Help, "Trung tâm hỗ trợ", onNavigateToHelp)
            }

            // Logout
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đăng xuất", color = Color.Red, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun OrderStateItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable { onClick() }.padding(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFFFF69B4), modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 11.sp, color = Color.DarkGray)
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFFFF69B4), modifier = Modifier.size(22.dp))
        Text(
            label, 
            modifier = Modifier.weight(1f).padding(start = 16.dp), 
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
    }
}
