package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.du_lieu.DonHang
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhDonHang(
    nguoiDungId: Int,
    trangThai: String,
    onBack: () -> Unit,
    onChiTietDonHang: (Int) -> Unit
) {
    var danhSachDonHang by remember { mutableStateOf<List<DonHang>>(emptyList()) }
    var dangTai by remember { mutableStateOf(true) }

    val tieuDe = when (trangThai) {
        "cho_xac_nhan" -> "Chờ xác nhận"
        "dang_giao" -> "Đang giao hàng"
        "da_giao" -> "Đã giao hàng"
        "danh_gia" -> "Đánh giá"
        else -> "Đơn hàng của tôi"
    }

    // Map route từ UI sang Enum của Database
    val statusMap = mapOf(
        "cho_xac_nhan" to "choduyet",
        "dang_giao" to "danggiaohang",
        "da_giao" to "hoanthanh",
        "danh_gia" to "hoanthanh"
    )

    LaunchedEffect(trangThai) {
        dangTai = true
        try {
            val statusToFilter = if (trangThai == "tat_ca") null else statusMap[trangThai]
            val response = KhachHangRetrofit.layGiaoDienApi.layDonHangNguoiDung(nguoiDungId, statusToFilter)
            danhSachDonHang = response
        } catch (e: Exception) {
            danhSachDonHang = emptyList()
        } finally {
            dangTai = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tieuDe, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF8F8F8))
        ) {
            if (dangTai) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PinkPrimary)
            } else if (danhSachDonHang.isEmpty()) {
                EmptyOrderView(onBack)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(danhSachDonHang) { donHang ->
                        ItemDonHang(donHang = donHang, onClick = { onChiTietDonHang(donHang.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun ItemDonHang(donHang: DonHang, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Mã đơn: #${donHang.id}", fontWeight = FontWeight.Bold, color = Color.Gray)
                
                val (statusText, statusColor) = when(donHang.trangThai) {
                    "choduyet" -> "Chờ duyệt" to PinkPrimary
                    "daxacnhan" -> "Đã xác nhận" to Color(0xFF4CAF50)
                    "danggiaohang" -> "Đang giao" to Color(0xFFFF9800)
                    "hoanthanh" -> "Hoàn thành" to Color(0xFF2196F3)
                    "dahuy" -> "Đã hủy" to Color.LightGray
                    else -> donHang.trangThai to Color.Black
                }
                
                Text(text = statusText, color = statusColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            
            Text(text = "Ngày đặt: ${donHang.ngayDat ?: "Hôm nay"}", color = Color.Gray, fontSize = 13.sp)
            Text(text = "Địa chỉ: ${donHang.diaChi}", color = Color.DarkGray, fontSize = 14.sp, maxLines = 1)

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${String.format(Locale.getDefault(), "%,.0f", donHang.tongTien)} đ",
                    fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = Color.Black
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (donHang.trangThai) {
                        "choduyet" -> {
                            OutlinedButton(onClick = {}, shape = RoundedCornerShape(20.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                                Text("Hủy đơn", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        "hoanthanh" -> {
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary), shape = RoundedCornerShape(20.dp)) {
                                Text("Mua lại", fontSize = 12.sp)
                            }
                        }
                        "danggiaohang" -> {
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(20.dp)) {
                                Text("Theo dõi", fontSize = 12.sp)
                            }
                        }
                        "dahuy" -> {
                            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary), shape = RoundedCornerShape(20.dp)) {
                                Text("Mua lại", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyOrderView(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.ShoppingBag, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Chưa có đơn hàng nào", color = Color.Gray, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Tiếp tục mua sắm")
        }
    }
}
