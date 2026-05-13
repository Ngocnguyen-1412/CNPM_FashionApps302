package com.example.myapplication.giao_dien

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.myapplication.viewmodel.NguoiDungViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhDiaChi(
    nguoiDungViewModel: NguoiDungViewModel,
    onBack: () -> Unit
) {
    val nguoiDung = nguoiDungViewModel.nguoiDungHienTai.value
    var showDialog by remember { mutableStateOf(false) }
    var diaChiMoi by remember { mutableStateOf(nguoiDung?.diaChi ?: "") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cập nhật địa chỉ nhận hàng") },
            text = {
                OutlinedTextField(
                    value = diaChiMoi,
                    onValueChange = { diaChiMoi = it },
                    label = { Text("Địa chỉ chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nguoiDung != null) {
                            scope.launch {
                                try {
                                    val res = KhachHangRetrofit.layGiaoDienApi.capNhatDiaChi(nguoiDung.id, diaChiMoi)
                                    // Sửa lỗi so sánh bằng cách dùng toString()
                                    if (res["status"].toString() == "success") {
                                        nguoiDungViewModel.thietLapNguoiDung(nguoiDung.copy(diaChi = diaChiMoi))
                                        showDialog = false
                                        Toast.makeText(context, "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                ) { Text("Lưu lại") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Hủy") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Địa chỉ nhận hàng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                containerColor = PinkPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Edit, null)
                Text(" Thay đổi địa chỉ")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF8F8F8)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (nguoiDung?.diaChi.isNullOrBlank()) Arrangement.Center else Arrangement.Top
        ) {
            if (nguoiDung?.diaChi.isNullOrBlank()) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
                Text("Bạn chưa lưu địa chỉ nào", color = Color.Gray, fontSize = 16.sp)
            } else {
                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = PinkPrimary, modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Địa chỉ hiện tại", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                            Text(nguoiDung?.diaChi ?: "", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
