package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.PinkPrimary

@Composable
fun ManHinhThanhCong(
    onTiepTucMuaSam: () -> Unit,
    onXemDonHang: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Biểu tượng thành công chuyên nghiệp
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(PinkPrimary.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                color = PinkPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(16.dp).size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Đặt hàng thành công!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cảm ơn bạn đã tin tưởng và mua sắm tại cửa hàng của chúng tôi. Đơn hàng của bạn đang được xử lý.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Nút xem đơn hàng
        Button(
            onClick = onXemDonHang,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                "XEM ĐƠN HÀNG",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút tiếp tục mua sắm
        OutlinedButton(
            onClick = onTiepTucMuaSam,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, PinkPrimary),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                "TIẾP TỤC MUA SẮM",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PinkPrimary
            )
        }
    }
}
