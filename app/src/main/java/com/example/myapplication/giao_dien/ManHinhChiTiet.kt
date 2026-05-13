package com.example.myapplication.giao_dien

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.du_lieu.BienTheSanPham
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import java.util.Locale
import com.example.myapplication.viewmodel.GioHangViewModel
import com.example.myapplication.viewmodel.NguoiDungViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChiTiet(
    sanPhamId: Int,
    gioHangViewModel: GioHangViewModel,
    nguoiDungViewModel: NguoiDungViewModel,
    onBack: () -> Unit
) {
    var sanPham by remember { mutableStateOf<SanPham?>(null) }
    var bienThes by remember { mutableStateOf<List<BienTheSanPham>>(emptyList()) }
    var dangTai by remember { mutableStateOf(true) }
    
    var selectedSize by remember { mutableStateOf<String?>(null) }
    var selectedColor by remember { mutableStateOf<String?>(null) }
    var isFavorite by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val nguoiDung = nguoiDungViewModel.nguoiDungHienTai.value

    var showCartDialog by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFavoriteConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sanPhamId, nguoiDung) {
        try {
            sanPham = KhachHangRetrofit.layGiaoDienApi.layChiTietSanPham(sanPhamId)
            bienThes = KhachHangRetrofit.layGiaoDienApi.layBienTheSanPham(sanPhamId)
            
            if (nguoiDung != null) {
                val res = KhachHangRetrofit.layGiaoDienApi.kiemTraYeuThich(nguoiDung.id, sanPhamId)
                isFavorite = res["yeu_thich"] == true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dangTai = false
        }
    }

    // Dialog xác nhận yêu thích
    if (showFavoriteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showFavoriteConfirmDialog = false },
            title = { Text(if (isFavorite) "Bỏ yêu thích?" else "Thêm vào yêu thích?", fontWeight = FontWeight.Bold) },
            text = { Text(if (isFavorite) "Bạn muốn xóa sản phẩm này khỏi danh sách yêu thích?" else "Bạn muốn thêm sản phẩm này vào danh sách yêu thích để nhận thông báo ưu đãi và xem lại sau?") },
            confirmButton = {
                Button(
                    onClick = {
                        showFavoriteConfirmDialog = false
                        scope.launch {
                            try {
                                val action = if (isFavorite) "xoa" else "them"
                                val res = KhachHangRetrofit.layGiaoDienApi.capNhatYeuThich(nguoiDung!!.id, sanPhamId, action)
                                if (res["status"].toString() == "success") {
                                    isFavorite = !isFavorite
                                    Toast.makeText(context, if (isFavorite) "Đã thêm vào yêu thích" else "Đã xóa yêu thích", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                ) { Text("Đồng ý") }
            },
            dismissButton = { TextButton(onClick = { showFavoriteConfirmDialog = false }) { Text("Hủy") } }
        )
    }

    if (showCheckoutDialog) {
        var hoTen by remember { mutableStateOf(nguoiDung?.hoTen ?: "") }
        var sdt by remember { mutableStateOf(nguoiDung?.soDienThoai ?: "") }
        var diachi by remember { mutableStateOf(nguoiDung?.diaChi ?: "") }

        AlertDialog(
            onDismissRequest = { showCheckoutDialog = false },
            title = { Text("Thông tin đặt hàng", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Sản phẩm: ${sanPham?.tenSanPham}", fontSize = 14.sp)
                    Text("Phân loại: $selectedSize - $selectedColor", fontSize = 12.sp, color = Color.Gray)
                    OutlinedTextField(value = hoTen, onValueChange = { hoTen = it }, label = { Text("Họ tên người nhận") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = sdt, onValueChange = { sdt = it }, label = { Text("Số điện thoại") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                    OutlinedTextField(value = diachi, onValueChange = { diachi = it }, label = { Text("Địa chỉ giao hàng chi tiết") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (hoTen.isBlank() || sdt.isBlank() || diachi.isBlank()) {
                            Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        showCheckoutDialog = false
                        scope.launch {
                            try {
                                val giaSauGiam = sanPham!!.gia * (1 - sanPham!!.giamGia / 100.0)
                                val res = KhachHangRetrofit.layGiaoDienApi.muaNgay(nguoiDung!!.id, sanPhamId, selectedSize!!, selectedColor!!, 1, giaSauGiam, hoTen, sdt, diachi, "cod")
                                if (res["status"].toString() == "success") showSuccessDialog = true
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                    shape = RoundedCornerShape(25.dp)
                ) { Text("Xác nhận đặt hàng") }
            },
            dismissButton = { TextButton(onClick = { showCheckoutDialog = false }) { Text("Hủy") } }
        )
    }

    if (showSuccessDialog) {
        Dialog(onDismissRequest = { showSuccessDialog = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp).background(
                        brush = Brush.verticalGradient(listOf(Color(0xFFFFEEF5), Color.White))
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = PinkPrimary.copy(alpha = 0.1f)) {}
                        Icon(Icons.Default.CheckCircle, null, tint = PinkPrimary, modifier = Modifier.size(75.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("THÀNH CÔNG!", fontWeight = FontWeight.Black, fontSize = 24.sp, color = PinkPrimary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Đơn hàng của bạn đã được ghi nhận.", textAlign = TextAlign.Center, color = Color.Gray, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { showSuccessDialog = false; onBack() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("VỀ TRANG CHỦ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            sanPham?.let { sp ->
                Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 15.dp, color = Color.White) {
                    Row(modifier = Modifier.padding(16.dp).navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            if (nguoiDung == null) {
                                Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                                return@IconButton
                            }
                            showFavoriteConfirmDialog = true
                        }, modifier = Modifier.size(52.dp).background(Color(0xFFF8F8F8), CircleShape)) {
                            Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = PinkPrimary)
                        }
                        IconButton(onClick = { showCartDialog = true }, modifier = Modifier.size(52.dp).background(Color(0xFFF8F8F8), CircleShape)) {
                            Icon(Icons.Outlined.ShoppingCart, null, tint = PinkPrimary)
                        }
                        Button(
                            onClick = { 
                                if (nguoiDung == null) Toast.makeText(context, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()
                                else if (selectedSize == null || selectedColor == null) Toast.makeText(context, "Chọn Size và Màu sắc", Toast.LENGTH_SHORT).show()
                                else showCheckoutDialog = true 
                            },
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                            shape = RoundedCornerShape(26.dp)
                        ) { Text("MUA NGAY", fontWeight = FontWeight.Black, fontSize = 16.sp) }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (dangTai) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PinkPrimary) }
        } else {
            sanPham?.let { sp ->
                val images = listOfNotNull(sp.hinhanh1, sp.hinhanh2, sp.hinhanh3)
                val pagerState = rememberPagerState(pageCount = { images.size })
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(innerPadding).verticalScroll(rememberScrollState()).fillMaxSize().background(Color.White)) {
                        // Header Image Pager
                        Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
                            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                                Image(
                                    painter = rememberAsyncImagePainter("http://10.0.2.2/shop_quan_ao/uploads/${images[page]}"),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            // Dot indicator
                            Row(
                                modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(images.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) PinkPrimary else Color.White.copy(alpha = 0.5f)
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(sp.thuongHieu?.uppercase() ?: "FASHION", color = PinkPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                                    Text(" 4.9 (120 Đã bán)", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(sp.tenSanPham, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            val giaSauGiam = sp.gia * (1 - sp.giamGia / 100.0)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("${String.format(Locale.getDefault(), "%,.0f", giaSauGiam)}đ", fontSize = 30.sp, color = PinkPrimary, fontWeight = FontWeight.Black)
                                if (sp.giamGia > 0) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("${String.format(Locale.getDefault(), "%,.0f", sp.gia)}đ", fontSize = 16.sp, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(color = Color.Red, shape = RoundedCornerShape(4.dp)) {
                                        Text("-${sp.giamGia}%", color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))

                            SectionTitle("Chọn kích cỡ")
                            val sizes = bienThes.mapNotNull { it.size }.distinct().filter { it.isNotBlank() }
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(sizes) { size -> SelectableChip(size, selectedSize == size) { selectedSize = size } }
                            }

                            SectionTitle("Chọn màu sắc")
                            val colors = bienThes.mapNotNull { it.mauSac }.distinct().filter { it.isNotBlank() }
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(colors) { color -> SelectableChip(color, selectedColor == color) { selectedColor = color } }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), thickness = 0.5.dp, color = Color(0xFFEEEEEE))

                            SectionTitle("Mô tả sản phẩm")
                            Text(sp.moTa ?: "Thông tin đang cập nhật...", color = Color.DarkGray, lineHeight = 24.sp, fontSize = 15.sp)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            // Trust Badges row
                            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp)).padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                                FeatureBadge(Icons.Default.Verified, "Chính hãng")
                                FeatureBadge(Icons.Default.LocalShipping, "Giao nhanh")
                                FeatureBadge(Icons.Default.Autorenew, "7 ngày đổi trả")
                            }

                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    // Back Button
                    IconButton(onClick = onBack, modifier = Modifier.padding(16.dp).padding(top = 8.dp).align(Alignment.TopStart).background(Color.White.copy(alpha = 0.9f), CircleShape).shadow(4.dp, CircleShape)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black)
                    }
                }
            }
        }
    }

    if (showCartDialog) {
        AlertDialog(
            onDismissRequest = { showCartDialog = false },
            title = { Text("Thêm vào giỏ hàng", fontWeight = FontWeight.Bold) },
            text = { Text("Bạn muốn thêm sản phẩm này (Size $selectedSize, Màu $selectedColor) vào giỏ hàng của mình?") },
            confirmButton = {
                Button(onClick = {
                    if (selectedSize == null || selectedColor == null) {
                        Toast.makeText(context, "Vui lòng chọn đầy đủ Size/Màu", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    showCartDialog = false
                    if (nguoiDung == null) return@Button
                    scope.launch {
                        try {
                            val res = KhachHangRetrofit.layGiaoDienApi.themVaoGioHang(action = "add", nguoiDungId = nguoiDung.id.toString(), sanPhamId = sanPham!!.id.toString(), soLuong = "1", size = selectedSize!!, mauSac = selectedColor!!)
                            if (res["status"].toString() == "success") {
                                gioHangViewModel.layGioHang(nguoiDung.id)
                                Toast.makeText(context, "Đã thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)) { Text("Đồng ý") }
            },
            dismissButton = { TextButton(onClick = { showCartDialog = false }) { Text("Hủy") } }
        )
    }
}

@Composable
fun FeatureBadge(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = PinkPrimary, modifier = Modifier.size(24.dp))
        Text(label, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 12.dp))
}

@Composable
fun SelectableChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() }.widthIn(min = 65.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) PinkPrimary else Color(0xFFF5F5F5),
        border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), textAlign = TextAlign.Center, color = if (isSelected) Color.White else Color.Black, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}
