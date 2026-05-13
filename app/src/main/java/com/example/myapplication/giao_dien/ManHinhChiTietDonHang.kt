package com.example.myapplication.giao_dien

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.du_lieu.DonHang
import com.example.myapplication.ui.theme.PinkPrimary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChiTietDonHang(
    maDonHang: String,
    onBack: () -> Unit
) {
    // Trong thực tế sẽ gọi API lấy chi tiết đơn hàng dựa trên maDonHang
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết đơn hàng #$maDonHang", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
                .padding(16.dp)
        ) {
            // Thông tin vận chuyển
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Thông tin giao hàng", fontWeight = FontWeight.Bold, color = PinkPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Người nhận: Bùi Kim Hoàng", fontSize = 14.sp)
                    Text("SĐT: 0987xxxxxx", fontSize = 14.sp)
                    Text("Địa chỉ: 123 Đường ABC, Quận X, TP. HCM", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sản phẩm", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Danh sách sản phẩm trong đơn (Ví dụ)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(listOf(1)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Tên sản phẩm", fontWeight = FontWeight.Bold)
                                Text("Phân loại: L, Hồng", fontSize = 12.sp, color = Color.Gray)
                                Text("x1", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            HorizontalDivider()
            Row(
                modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tổng thanh toán", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("225,000 đ", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PinkPrimary)
            }
        }
    }
}
