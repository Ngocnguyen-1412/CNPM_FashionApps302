package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.du_lieu.NguoiDung
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

@Composable
fun ManHinhDangNhap(
    onDangNhapThanhCong: (NguoiDung) -> Unit,
    onChuyenSangDangKy: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var matKhau by remember { mutableStateOf("") }
    var dangTai by remember { mutableStateOf(false) }
    var loi by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoN3h2()
        
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Đăng Nhập",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(24.dp))

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
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                scope.launch {
                    dangTai = true
                    loi = null
                    try {
                        val user = KhachHangRetrofit.layGiaoDienApi.dangNhap(email, matKhau)
                        onDangNhapThanhCong(user)
                    } catch (e: HttpException) {
                        loi = when (e.code()) {
                            401  -> "Sai mật khẩu"
                            404  -> "Email không tồn tại"
                            else -> "Lỗi server (${e.code()})"
                        }
                    } catch (e: ConnectException) {
                        loi = "Không thể kết nối server. Kiểm tra XAMPP đã bật chưa?"
                    } catch (e: SocketTimeoutException) {
                        loi = "Kết nối quá thời gian. Thử lại sau."
                    } catch (e: Exception) {
                        loi = "Đăng nhập thất bại. Vui lòng thử lại."
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
                Text("ĐĂNG NHẬP", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("Chưa có tài khoản? ")
            Text(
                "Đăng ký ngay",
                color = PinkPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onChuyenSangDangKy() }
            )
        }
    }
}
