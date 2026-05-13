package com.example.myapplication.giao_dien

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.ui.theme.PinkPrimary
import com.example.myapplication.viewmodel.GioHangViewModel
import com.example.myapplication.viewmodel.ItemGioHang
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhGioHang(
    userId: Int,
    viewModel: GioHangViewModel,
    onBack: () -> Unit,
    onThanhToan: () -> Unit
) {
    val items = viewModel.danhSachGioHang
    val dangTai = viewModel.dangTai.value

    LaunchedEffect(userId) {
        viewModel.layGioHang(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giỏ hàng của bạn", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkPrimary)
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng tiền:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "${String.format(Locale.getDefault(), "%,.0f", viewModel.tinhTongTien())} VNĐ",
                                fontSize = 18.sp,
                                color = PinkPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onThanhToan,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("TIẾN HÀNH THANH TOÁN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF5F5F5))) {
            if (dangTai && items.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PinkPrimary)
            } else if (items.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Giỏ hàng trống", color = Color.Gray, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)) {
                        Text("Mua sắm ngay")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        ItemGioHangCard(
                            item = item, 
                            onRemove = { 
                                viewModel.xoaKhoiGio(userId, item.sanPham.id, item.size, item.mauSac) 
                            },
                            onIncrease = {
                                viewModel.capNhatSoLuong(userId, item.sanPham.id, item.soLuong + 1, item.size, item.mauSac)
                            },
                            onDecrease = {
                                viewModel.capNhatSoLuong(userId, item.sanPham.id, item.soLuong - 1, item.size, item.mauSac)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemGioHangCard(
    item: ItemGioHang, 
    onRemove: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter("http://10.0.2.2/shop_quan_ao/uploads/${item.sanPham.hinhanh1}"),
                contentDescription = null,
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item.sanPham.tenSanPham, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.weight(1f))
                    IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
                Text("${String.format(Locale.getDefault(), "%,.0f", item.sanPham.gia)} VNĐ", color = PinkPrimary, fontWeight = FontWeight.SemiBold)
                Text("Phân loại: ${item.size}, ${item.mauSac}", fontSize = 12.sp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF5F5F5),
                        onClick = onDecrease
                    ) {
                        Icon(Icons.Default.Remove, null, modifier = Modifier.padding(4.dp), tint = Color.Black)
                    }
                    
                    Text(
                        item.soLuong.toString(), 
                        modifier = Modifier.padding(horizontal = 12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    
                    Surface(
                        modifier = Modifier.size(28.dp),
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF5F5F5),
                        onClick = onIncrease
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.padding(4.dp), tint = Color.Black)
                    }
                }
            }
        }
    }
}
