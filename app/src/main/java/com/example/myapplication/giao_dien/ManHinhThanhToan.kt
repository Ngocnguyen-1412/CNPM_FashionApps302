package com.example.myapplication.giao_dien

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import com.example.myapplication.viewmodel.GioHangViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhThanhToan(
    userId: Int,
    gioHangViewModel: GioHangViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var diaChi by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var dangTai by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Dialog xác nhận đặt hàng cuối cùng
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận đặt hàng", fontWeight = FontWeight.Bold) },
            text = { 
                Column {
                    Text("Kiểm tra lại thông tin đơn hàng của bạn:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tổng thanh toán: ${String.format(Locale.getDefault(), "%,.0f", gioHangViewModel.tinhTongTien())} VNĐ", 
                        color = PinkPrimary, fontWeight = FontWeight.Bold)
                    Text("Địa chỉ: $diaChi", fontSize = 13.sp, color = Color.Gray)
                    Text("SĐT: $soDienThoai", fontSize = 13.sp, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        dangTai = true
                        scope.launch {
                            try {
                                val orderData = mapOf(
                                    "nguoidung_id" to userId,
                                    "tongtien" to gioHangViewModel.tinhTongTien(),
                                    "diachi" to diaChi,
                                    "sodienthoai" to soDienThoai,
                                    "chitiet" to gioHangViewModel.danhSachGioHang.map {
                                        mapOf("sanpham_id" to it.sanPham.id, "soluong" to it.soLuong, "gia" to it.sanPham.gia, "size" to it.size, "mausac" to it.mauSac)
                                    }
                                )
                                val response = KhachHangRetrofit.layGiaoDienApi.thanhToan(orderData)
                                if (response["status"].toString() == "success") {
                                    gioHangViewModel.xoaHet()
                                    onSuccess()
                                } else {
                                    Toast.makeText(context, "Lỗi: ${response["message"]}", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Lỗi kết nối server", Toast.LENGTH_SHORT).show()
                            } finally {
                                dangTai = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                ) { Text("Đồng ý đặt hàng") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Kiểm tra lại") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thanh toán", fontWeight = FontWeight.Bold) },
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Thông tin giao hàng", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PinkPrimary)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = soDienThoai,
                        onValueChange = { soDienThoai = it },
                        label = { Text("Số điện thoại nhận hàng") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = diaChi,
                        onValueChange = { diaChi = it },
                        label = { Text("Địa chỉ nhận hàng chi tiết") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9FB)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tổng thanh toán:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(
                        "${String.format(Locale.getDefault(), "%,.0f", gioHangViewModel.tinhTongTien())} VNĐ",
                        fontSize = 20.sp,
                        color = PinkPrimary,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (diaChi.isBlank() || soDienThoai.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    showConfirmDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                shape = RoundedCornerShape(28.dp),
                enabled = !dangTai
            ) {
                if (dangTai) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("XÁC NHẬN ĐẶT HÀNG", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            }
        }
    }
}
