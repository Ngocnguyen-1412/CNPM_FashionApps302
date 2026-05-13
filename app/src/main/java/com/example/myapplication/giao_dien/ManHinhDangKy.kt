package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import kotlinx.coroutines.launch
import java.net.ConnectException

@Composable
fun ManHinhDangKy(
    onDangKyThanhCong: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var hoTen by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var soDienThoai by remember { mutableStateOf("") }
    var matKhau by remember { mutableStateOf("") }
    var dangTai by remember { mutableStateOf(false) }
    var loi by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoN3h2()
        
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Đăng Ký Tài Khoản",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = hoTen,
            onValueChange = { hoTen = it },
            label = { Text("Họ tên") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PinkPrimary,
                focusedLabelColor = PinkPrimary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PinkPrimary,
                focusedLabelColor = PinkPrimary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = soDienThoai,
            onValueChange = { soDienThoai = it },
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PinkPrimary,
                focusedLabelColor = PinkPrimary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = matKhau,
            onValueChange = { matKhau = it },
            label = { Text("Mật khẩu") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PinkPrimary,
                focusedLabelColor = PinkPrimary
            )
        )
        
        loi?.let {
            Text(
                it, 
                color = Color.Red, 
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (hoTen.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
                    loi = "Vui lòng nhập đầy đủ thông tin"
                    return@Button
                }
                scope.launch {
                    dangTai = true
                    loi = null
                    try {
                        val response = KhachHangRetrofit.layGiaoDienApi.dangKy(hoTen, email, soDienThoai, matKhau)
                        if (response["status"] == "success") {
                            onDangKyThanhCong()
                        } else {
                            loi = response["message"] ?: "Lỗi từ server"
                        }
                    } catch (e: Exception) {
                        loi = when (e) {
                            is ConnectException -> "Không thể kết nối Server. Hãy kiểm tra XAMPP và địa chỉ IP (10.0.2.2)"
                            else -> "Lỗi hệ thống: ${e.localizedMessage}"
                        }
                    } finally {
                        dangTai = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
            shape = RoundedCornerShape(12.dp),
            enabled = !dangTai
        ) {
            if (dangTai) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("ĐĂNG KÝ", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Đã có tài khoản? ")
            Text(
                "Đăng nhập",
                color = PinkPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
