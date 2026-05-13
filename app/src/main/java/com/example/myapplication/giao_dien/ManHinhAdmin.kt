package com.example.myapplication.giao_dien

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.du_lieu.*
import com.example.myapplication.viewmodel.AdminViewModel
import java.util.Locale

// ─── Màu sắc Admin theme ────────────────────────────────────────────────────
private val AdminBg       = Color(0xFFF4F6FA)
private val AdminPrimary  = Color(0xFF1A1A2E)
private val AdminAccent   = Color(0xFF4F8EF7)
private val AdminGreen    = Color(0xFF27AE60)
private val AdminOrange   = Color(0xFFF39C12)
private val AdminRed      = Color(0xFFE74C3C)
private val AdminCard     = Color.White

// ════════════════════════════════════════════════════════════════════════════
// MÀN HÌNH ADMIN CHÍNH
// ════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhAdmin(
    onLogout: () -> Unit,
    adminViewModel: AdminViewModel = viewModel()
) {
    val thongKe        by adminViewModel.thongKe
    val tatCaDonHang   by adminViewModel.tatCaDonHang
    val tatCaNguoiDung by adminViewModel.tatCaNguoiDung
    val tatCaSanPham   by adminViewModel.tatCaSanPham
    val tatCaDanhMuc   by adminViewModel.tatCaDanhMuc
    val tatCaBanner    by adminViewModel.tatCaBanner
    val isLoading      by adminViewModel.isLoading
    val toastMsg       by adminViewModel.toastMessage

    var currentTab by remember { mutableStateOf(0) }

    // Toast
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            snackbarHostState.showSnackbar(it)
            adminViewModel.clearToast()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "ADMIN DASHBOARD",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            if (isLoading) "Đang tải dữ liệu..." else "N3h2 Fashion — Hệ thống quản trị",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                },
                actions = {
                    IconButton(onClick = { adminViewModel.taiTatCa() }) {
                        Icon(Icons.Default.Refresh, "Làm mới", tint = Color.White)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Đăng xuất", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminPrimary)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = AdminPrimary, tonalElevation = 0.dp) {
                val tabs = listOf(
                    Triple("Tổng quan", Icons.Default.Dashboard, 0),
                    Triple("Đơn hàng", Icons.Default.ListAlt, 1),
                    Triple("Sản phẩm", Icons.Default.Inventory2, 2),
                    Triple("Danh mục", Icons.Default.Category, 3),
                    Triple("Người dùng", Icons.Default.People, 4)
                )
                tabs.forEach { (label, icon, idx) ->
                    NavigationBarItem(
                        selected = currentTab == idx,
                        onClick = { currentTab = idx },
                        icon = { Icon(icon, null, modifier = Modifier.size(20.dp)) },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AdminAccent,
                            selectedTextColor = AdminAccent,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            unselectedTextColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = Color.White.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        },
        containerColor = AdminBg
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AnimatedContent(targetState = currentTab, label = "tab") { tab ->
                when (tab) {
                    0 -> TabTongQuan(thongKe, tatCaDonHang, adminViewModel)
                    1 -> TabDonHang(tatCaDonHang, adminViewModel)
                    2 -> TabSanPham(tatCaSanPham, tatCaDanhMuc, adminViewModel)
                    3 -> TabDanhMuc(tatCaDanhMuc, tatCaBanner, adminViewModel)
                    4 -> TabNguoiDung(tatCaNguoiDung, adminViewModel)
                }
            }
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
                    color = AdminAccent,
                    trackColor = AdminPrimary.copy(alpha = 0.2f)
                )
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
// TAB 0 — TỔNG QUAN
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TabTongQuan(
    thongKe: ThongKeAdmin?,
    donHangs: List<DonHang>,
    vm: AdminViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // KPI cards
        item {
            Text("Tổng quan hôm nay", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AdminPrimary)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard("Doanh thu", formatVnd(thongKe?.tongDoanhThu ?: 0.0), Icons.Default.AccountBalanceWallet, AdminAccent, Modifier.weight(1f))
                KpiCard("Đơn hàng", "${thongKe?.tongDonHang ?: 0}", Icons.Default.ShoppingCart, AdminGreen, Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                KpiCard("Sản phẩm", "${thongKe?.tongSanPham ?: 0}", Icons.Default.Inventory2, AdminOrange, Modifier.weight(1f))
                KpiCard("Khách hàng", "${thongKe?.tongNguoiDung ?: 0}", Icons.Default.PeopleAlt, Color(0xFF9B59B6), Modifier.weight(1f))
            }
        }

        // Biểu đồ tròn — doanh thu 7 ngày
        item {
            AdminCard(title = "Doanh thu 7 ngày gần nhất — ${formatVnd(thongKe?.tongDoanhThu7Ngay ?: 0.0)}") {
                BieuDoTronDoanhThu7Ngay(thongKe?.duLieuBieuDoTron ?: emptyList())
            }
        }

        // Biểu đồ cột — số lượng đơn theo trạng thái
        item {
            AdminCard(title = "Số lượng đơn hàng theo trạng thái") {
                BieuDoCotComponent(thongKe?.duLieuBieuDoCot ?: emptyList())
            }
        }

        // Đơn hàng mới
        item {
            Text("Đơn hàng mới nhất", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = AdminPrimary)
        }
        items(thongKe?.donHangMoi ?: emptyList()) { dh ->
            DonHangCard(dh) { vm.capNhatTrangThaiDonHang(dh.id, it) }
        }
    }
}

@Composable
fun KpiCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = AdminCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 18.sp, color = AdminPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AdminCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AdminCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AdminPrimary)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun BieuDoTronDoanhThu7Ngay(data: List<BieuDoTron>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.BarChart, null, tint = Color.LightGray, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(8.dp))
                Text("Chưa có đơn hoàn thành trong 7 ngày", color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        }
        return
    }
    val colors = listOf(
        Color(0xFF4F8EF7), Color(0xFF27AE60), Color(0xFFF39C12),
        Color(0xFFE74C3C), Color(0xFF9B59B6), Color(0xFF1ABC9C), Color(0xFFE67E22)
    )
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        // Donut chart
        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = -90f
            data.forEachIndexed { i, item ->
                val sweep = (item.phanTram / 100f) * 360f
                drawArc(color = colors[i % colors.size], startAngle = startAngle, sweepAngle = sweep, useCenter = true)
                startAngle += sweep
            }
            // Lỗ giữa
            drawCircle(color = Color.White, radius = size.minDimension / 3f)
        }
        Spacer(Modifier.width(16.dp))
        // Legend
        Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
            data.forEachIndexed { i, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(colors[i % colors.size], RoundedCornerShape(2.dp)))
                    Spacer(Modifier.width(6.dp))
                    Column {
                        Text(item.ten, fontSize = 11.sp, color = AdminPrimary, fontWeight = FontWeight.SemiBold)
                        Text("${formatVnd(item.soLuong.toDouble())} (${String.format("%.0f", item.phanTram)}%)", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun BieuDoCotComponent(data: List<BieuDoCot>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
            Text("Chưa có dữ liệu", color = Color.Gray, fontSize = 13.sp)
        }
        return
    }
    val maxVal = data.maxOfOrNull { it.giaTri } ?: 1f
    val labelNgan = mapOf(
        "choduyet"     to "Chờ",
        "daxacnhan"    to "XN",
        "danggiaohang" to "Giao",
        "hoanthanh"    to "Xong",
        "dahuy"        to "Hủy"
    )
    Row(
        modifier = Modifier.fillMaxWidth().height(130.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { item ->
            val barColor = when (item.nhan) {
                "choduyet"     -> AdminOrange
                "daxacnhan"    -> AdminAccent
                "danggiaohang" -> Color(0xFF1ABC9C)
                "hoanthanh"    -> AdminGreen
                "dahuy"        -> AdminRed
                else           -> AdminAccent
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text("${item.giaTri.toInt()}", fontSize = 9.sp, color = Color.Gray)
                Spacer(Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .fillMaxHeight(fraction = (item.giaTri / maxVal).coerceAtLeast(0.04f))
                        .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                        .background(barColor)
                )
                Spacer(Modifier.height(4.dp))
                Text(labelNgan[item.nhan] ?: item.nhan, fontSize = 9.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun BieuDoTronComponent(data: List<BieuDoTron>) {
    if (data.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
            Text("Chưa có dữ liệu", color = Color.Gray, fontSize = 13.sp)
        }
        return
    }
    val colors = listOf(AdminAccent, AdminGreen, AdminOrange, AdminRed, Color(0xFF9B59B6))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(110.dp)) {
            var startAngle = -90f
            data.forEachIndexed { i, item ->
                val sweep = (item.phanTram / 100f) * 360f
                drawArc(color = colors[i % colors.size], startAngle = startAngle, sweepAngle = sweep, useCenter = true)
                startAngle += sweep
            }
            drawCircle(color = Color.White, radius = size.minDimension / 3.5f)
        }
        Spacer(Modifier.width(20.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            data.forEachIndexed { i, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(colors[i % colors.size], CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("${trangThaiLabel(item.ten)}: ${item.soLuong.toInt()} (${String.format("%.0f", item.phanTram)}%)", fontSize = 12.sp, color = AdminPrimary)
                }
            }
        }
    }
}

// ════════════════════════════════════════════════════════════════════════════
// TAB 1 — ĐƠN HÀNG
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TabDonHang(donHangs: List<DonHang>, vm: AdminViewModel) {
    var filterTrangThai by remember { mutableStateOf("tat_ca") }
    var selectedDonHang by remember { mutableStateOf<DonHang?>(null) }
    val chiTiet by vm.chiTietDonHang

    val filters = listOf(
        "tat_ca"       to "Tất cả",
        "choduyet"     to "Chờ duyệt",
        "daxacnhan"    to "Đã xác nhận",
        "danggiaohang" to "Đang giao",
        "hoanthanh"    to "Hoàn thành",
        "dahuy"        to "Đã hủy"
    )
    val filtered = if (filterTrangThai == "tat_ca") donHangs else donHangs.filter { it.trangThai == filterTrangThai }

    if (selectedDonHang != null) {
        ChiTietDonHangDialog(
            donHang = selectedDonHang!!,
            chiTiet = chiTiet,
            onDismiss = { selectedDonHang = null },
            onCapNhat = { trangThai ->
                vm.capNhatTrangThaiDonHang(selectedDonHang!!.id, trangThai)
                selectedDonHang = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter chips — dùng LazyRow để không bị tràn
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { (key, label) ->
                FilterChip(
                    selected = filterTrangThai == key,
                    onClick = { filterTrangThai = key },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AdminAccent,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        Text(
            "${filtered.size} đơn hàng",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp, color = Color.Gray
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(filtered) { dh ->
                DonHangCard(
                    donHang = dh,
                    onClick = {
                        selectedDonHang = dh
                        vm.layChiTietDonHang(dh.id)
                    },
                    onCapNhat = { vm.capNhatTrangThaiDonHang(dh.id, it) }
                )
            }
        }
    }
}

@Composable
fun DonHangCard(donHang: DonHang, onClick: (() -> Unit)? = null, onCapNhat: (String) -> Unit) {
    val (bgColor, textColor) = trangThaiColor(donHang.trangThai)
    Card(
        modifier = Modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(containerColor = AdminCard),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("#${donHang.id}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = AdminPrimary)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(bgColor).padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(trangThaiLabel(donHang.trangThai), fontSize = 11.sp, color = textColor, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(donHang.hoTen ?: "Khách hàng", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(donHang.soDienThoai ?: "-", fontSize = 12.sp, color = Color.Gray)
            Text(donHang.diaChi ?: "-", fontSize = 12.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(formatVnd(donHang.tongTien), fontWeight = FontWeight.Black, fontSize = 16.sp, color = AdminAccent)
                Text((donHang.phuongThucThanhToan ?: "COD").uppercase(), fontSize = 11.sp, color = Color.Gray)
            }
            // Action buttons
            val nextStatus = nextTrangThai(donHang.trangThai)
            if (nextStatus != null) {
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = { onCapNhat(nextStatus.first) },
                    colors = ButtonDefaults.buttonColors(containerColor = nextStatus.second),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(nextStatus.third, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(nextStatus.fourth, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ChiTietDonHangDialog(
    donHang: DonHang,
    chiTiet: List<ChiTietDonHangItem>,
    onDismiss: () -> Unit,
    onCapNhat: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text("Chi tiết đơn #${donHang.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    InfoRow("Khách hàng", donHang.hoTen ?: "-")
                    InfoRow("SĐT", donHang.soDienThoai ?: "-")
                    InfoRow("Địa chỉ", donHang.diaChi ?: "-")
                    InfoRow("Thanh toán", (donHang.phuongThucThanhToan ?: "COD").uppercase())
                    InfoRow("Trạng thái", trangThaiLabel(donHang.trangThai))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Sản phẩm đặt mua", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                if (chiTiet.isEmpty()) {
                    item { Text("Đang tải...", color = Color.Gray, fontSize = 12.sp) }
                } else {
                    items(chiTiet) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = "http://10.0.2.2/shop_quan_ao/uploads/${item.hinhAnh}",
                                contentDescription = null,
                                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF0F0F0))
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.tenSanPham ?: "-", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 2)
                                Text("x${item.soLuong} — ${formatVnd(item.gia)}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
                item {
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Tổng cộng", fontWeight = FontWeight.Bold)
                        Text(formatVnd(donHang.tongTien), fontWeight = FontWeight.Black, color = AdminAccent)
                    }
                }
            }
        },
        confirmButton = {
            val next = nextTrangThai(donHang.trangThai)
            if (next != null) {
                Button(
                    onClick = { onCapNhat(next.first) },
                    colors = ButtonDefaults.buttonColors(containerColor = next.second)
                ) { Text(next.fourth) }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Đóng") }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text("$label: ", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.width(90.dp))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
    }
}

// ════════════════════════════════════════════════════════════════════════════
// TAB 2 — SẢN PHẨM
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TabSanPham(sanPhams: List<SanPhamAdmin>, danhMucs: List<DanhMuc>, vm: AdminViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editItem by remember { mutableStateOf<SanPhamAdmin?>(null) }
    var searchText by remember { mutableStateOf("") }

    val filtered = sanPhams.filter { it.tenSanPham.contains(searchText, ignoreCase = true) }

    if (showDialog || editItem != null) {
        DialogSanPham(
            item = editItem,
            danhMucs = danhMucs,
            onDismiss = { showDialog = false; editItem = null },
            onConfirm = { ten, gia, moTa, soLuong, giamGia, dmId, thuongHieu, chatLieu, a1, a2, a3 ->
                if (editItem != null) {
                    vm.suaSanPham(editItem!!.id, ten, gia, moTa, soLuong, giamGia, dmId, thuongHieu, chatLieu, editItem!!.trangThai)
                } else {
                    vm.themSanPham(ten, gia, moTa, soLuong, giamGia, dmId, thuongHieu, chatLieu, a1, a2, a3)
                }
                showDialog = false; editItem = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Tìm sản phẩm...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = AdminAccent,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }
        Text("${filtered.size} sản phẩm", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 12.sp, color = Color.Gray)
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(filtered) { sp ->
                SanPhamAdminCard(
                    sp = sp,
                    onEdit = { editItem = sp },
                    onToggle = {
                        vm.suaSanPham(sp.id, sp.tenSanPham, sp.gia, sp.moTa ?: "", sp.soLuong, sp.giamGia,
                            sp.danhMucId ?: 0, sp.thuongHieu ?: "", sp.chatLieu ?: "", if (sp.trangThai == 1) 0 else 1)
                    },
                    onDelete = { vm.xoaSanPham(sp.id) }
                )
            }
        }
    }
}

@Composable
fun SanPhamAdminCard(sp: SanPhamAdmin, onEdit: () -> Unit, onToggle: () -> Unit, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Xác nhận ẩn sản phẩm") },
            text = { Text("Sản phẩm \"${sp.tenSanPham}\" sẽ bị ẩn khỏi cửa hàng.") },
            confirmButton = { Button(onClick = { onDelete(); showConfirm = false }, colors = ButtonDefaults.buttonColors(containerColor = AdminRed)) { Text("Ẩn") } },
            dismissButton = { TextButton(onClick = { showConfirm = false }) { Text("Hủy") } }
        )
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = if (sp.trangThai == 1) AdminCard else Color(0xFFFAFAFA)),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(if (sp.trangThai == 1) 2.dp else 0.dp),
        border = if (sp.trangThai == 0) CardDefaults.outlinedCardBorder() else null
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "http://10.0.2.2/shop_quan_ao/uploads/${sp.hinhanh1}",
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF0F0F0))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(sp.tenSanPham, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                    if (sp.trangThai == 0) {
                        Box(Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFEEEEEE)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("Ẩn", fontSize = 10.sp, color = Color.Gray)
                        }
                    }
                }
                Text(sp.tenDanhMuc ?: "Chưa phân loại", fontSize = 11.sp, color = Color.Gray)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(formatVnd(sp.gia), fontWeight = FontWeight.Black, fontSize = 14.sp, color = AdminAccent)
                    if (sp.giamGia > 0) {
                        Box(Modifier.clip(RoundedCornerShape(4.dp)).background(AdminRed.copy(alpha = 0.1f)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                            Text("-${sp.giamGia}%", fontSize = 10.sp, color = AdminRed, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text("Kho: ${sp.soLuong} | Xem: ${sp.luotXem}", fontSize = 11.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, null, tint = AdminAccent, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = { showConfirm = true }, modifier = Modifier.size(32.dp)) {
                    Icon(if (sp.trangThai == 1) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSanPham(
    item: SanPhamAdmin?,
    danhMucs: List<DanhMuc>,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, String, Int, Int, Int, String, String, String, String, String) -> Unit
) {
    var ten by remember { mutableStateOf(item?.tenSanPham ?: "") }
    var gia by remember { mutableStateOf(item?.gia?.toString() ?: "") }
    var moTa by remember { mutableStateOf(item?.moTa ?: "") }
    var soLuong by remember { mutableStateOf(item?.soLuong?.toString() ?: "") }
    var giamGia by remember { mutableStateOf(item?.giamGia?.toString() ?: "0") }
    var thuongHieu by remember { mutableStateOf(item?.thuongHieu ?: "") }
    var chatLieu by remember { mutableStateOf(item?.chatLieu ?: "") }
    var anh1 by remember { mutableStateOf(item?.hinhanh1 ?: "") }
    var anh2 by remember { mutableStateOf(item?.hinhanh2 ?: "") }
    var anh3 by remember { mutableStateOf(item?.hinhanh3 ?: "") }
    var selectedDmId by remember { mutableStateOf(item?.danhMucId ?: (danhMucs.firstOrNull()?.id ?: 0)) }
    var dmExpanded by remember { mutableStateOf(false) }
    val selectedDmName = danhMucs.find { it.id == selectedDmId }?.tenDanhMuc ?: "Chọn danh mục"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Thêm sản phẩm" else "Sửa sản phẩm", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { AdminTextField(ten, { ten = it }, "Tên sản phẩm *") }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdminTextField(gia, { gia = it }, "Giá (đ) *", KeyboardType.Number, Modifier.weight(1f))
                        AdminTextField(giamGia, { giamGia = it }, "Giảm (%)", KeyboardType.Number, Modifier.weight(1f))
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdminTextField(soLuong, { soLuong = it }, "Số lượng *", KeyboardType.Number, Modifier.weight(1f))
                        AdminTextField(thuongHieu, { thuongHieu = it }, "Thương hiệu", modifier = Modifier.weight(1f))
                    }
                }
                item { AdminTextField(chatLieu, { chatLieu = it }, "Chất liệu") }
                item { AdminTextField(moTa, { moTa = it }, "Mô tả", singleLine = false) }
                item {
                    // Dropdown danh mục
                    ExposedDropdownMenuBox(expanded = dmExpanded, onExpandedChange = { dmExpanded = it }) {
                        OutlinedTextField(
                            value = selectedDmName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Danh mục") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dmExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(expanded = dmExpanded, onDismissRequest = { dmExpanded = false }) {
                            danhMucs.forEach { dm ->
                                DropdownMenuItem(
                                    text = { Text(dm.tenDanhMuc) },
                                    onClick = { selectedDmId = dm.id; dmExpanded = false }
                                )
                            }
                        }
                    }
                }
                item { AdminTextField(anh1, { anh1 = it }, "Tên file ảnh 1 (vd: ao1.jpg)") }
                item { AdminTextField(anh2, { anh2 = it }, "Tên file ảnh 2") }
                item { AdminTextField(anh3, { anh3 = it }, "Tên file ảnh 3") }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(ten, gia.toDoubleOrNull() ?: 0.0, moTa, soLuong.toIntOrNull() ?: 0,
                        giamGia.toIntOrNull() ?: 0, selectedDmId, thuongHieu, chatLieu, anh1, anh2, anh3)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AdminAccent),
                enabled = ten.isNotBlank() && gia.isNotBlank()
            ) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } },
        shape = RoundedCornerShape(20.dp)
    )
}

// ════════════════════════════════════════════════════════════════════════════
// TAB 3 — DANH MỤC & BANNER
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TabDanhMuc(danhMucs: List<DanhMuc>, banners: List<Banner>, vm: AdminViewModel) {
    var subTab by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = subTab,
            containerColor = AdminCard,
            contentColor = AdminAccent
        ) {
            Tab(selected = subTab == 0, onClick = { subTab = 0 }, text = { Text("Danh mục") })
            Tab(selected = subTab == 1, onClick = { subTab = 1 }, text = { Text("Banner") })
        }
        when (subTab) {
            0 -> DanhMucList(danhMucs, vm)
            1 -> BannerList(banners, vm)
        }
    }
}

@Composable
fun DanhMucList(danhMucs: List<DanhMuc>, vm: AdminViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editItem by remember { mutableStateOf<DanhMuc?>(null) }

    if (showDialog || editItem != null) {
        DialogDanhMuc(
            item = editItem,
            onDismiss = { showDialog = false; editItem = null },
            onConfirm = { ten, anh ->
                if (editItem != null) vm.suaDanhMuc(editItem!!.id, ten, anh)
                else vm.themDanhMuc(ten, anh)
                showDialog = false; editItem = null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(danhMucs) { dm ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AdminCard),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "http://10.0.2.2/shop_quan_ao/uploads/${dm.hinhAnh}",
                            contentDescription = null,
                            modifier = Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFF0F0F0))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dm.tenDanhMuc, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("ID: ${dm.id}", fontSize = 11.sp, color = Color.Gray)
                        }
                        IconButton(onClick = { editItem = dm }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Edit, null, tint = AdminAccent, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { vm.xoaDanhMuc(dm.id) }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Delete, null, tint = AdminRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = AdminAccent,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) { Icon(Icons.Default.Add, null, tint = Color.White) }
    }
}

@Composable
fun DialogDanhMuc(item: DanhMuc?, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var ten by remember { mutableStateOf(item?.tenDanhMuc ?: "") }
    var anh by remember { mutableStateOf(item?.hinhAnh ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Thêm danh mục" else "Sửa danh mục", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminTextField(ten, { ten = it }, "Tên danh mục *")
                AdminTextField(anh, { anh = it }, "Tên file ảnh (vd: ao.png)")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(ten, anh) }, colors = ButtonDefaults.buttonColors(containerColor = AdminAccent), enabled = ten.isNotBlank()) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun BannerList(banners: List<Banner>, vm: AdminViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editItem by remember { mutableStateOf<Banner?>(null) }

    if (showDialog || editItem != null) {
        DialogBanner(
            item = editItem,
            onDismiss = { showDialog = false; editItem = null },
            onConfirm = { tieude, anh, trangThai ->
                if (editItem != null) vm.suaBanner(editItem!!.id, tieude, anh, trangThai)
                else vm.themBanner(tieude, anh, trangThai)
                showDialog = false; editItem = null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(banners) { b ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AdminCard),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = "http://10.0.2.2/shop_quan_ao/uploads/${b.hinhAnh}",
                            contentDescription = null,
                            modifier = Modifier.size(72.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF0F0F0))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(b.tieuDe, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 2)
                            Spacer(Modifier.height(4.dp))
                            Box(
                                Modifier.clip(RoundedCornerShape(4.dp))
                                    .background(if (b.trangThai == 1) AdminGreen.copy(alpha = 0.1f) else Color(0xFFEEEEEE))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(if (b.trangThai == 1) "Đang hiển thị" else "Đã ẩn", fontSize = 11.sp, color = if (b.trangThai == 1) AdminGreen else Color.Gray)
                            }
                        }
                        IconButton(onClick = { editItem = b }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Edit, null, tint = AdminAccent, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { vm.xoaBanner(b.id) }, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Delete, null, tint = AdminRed, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(72.dp)) }
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = AdminAccent,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
        ) { Icon(Icons.Default.Add, null, tint = Color.White) }
    }
}

@Composable
fun DialogBanner(item: Banner?, onDismiss: () -> Unit, onConfirm: (String, String, Int) -> Unit) {
    var tieude by remember { mutableStateOf(item?.tieuDe ?: "") }
    var anh by remember { mutableStateOf(item?.hinhAnh ?: "") }
    var active by remember { mutableStateOf((item?.trangThai ?: 1) == 1) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Thêm banner" else "Sửa banner", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminTextField(tieude, { tieude = it }, "Tiêu đề banner *")
                AdminTextField(anh, { anh = it }, "Tên file ảnh (vd: banner1.jpg)")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (active) AdminGreen.copy(alpha = 0.08f) else Color(0xFFF5F5F5))
                        .clickable { active = !active }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Checkbox(
                        checked = active,
                        onCheckedChange = { active = it },
                        colors = CheckboxDefaults.colors(checkedColor = AdminGreen)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (active) "Đang hiển thị trên app" else "Đã ẩn",
                        fontSize = 13.sp,
                        color = if (active) AdminGreen else Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(tieude, anh, if (active) 1 else 0) }, colors = ButtonDefaults.buttonColors(containerColor = AdminAccent), enabled = tieude.isNotBlank()) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } },
        shape = RoundedCornerShape(20.dp)
    )
}

// ════════════════════════════════════════════════════════════════════════════
// TAB 4 — NGƯỜI DÙNG
// ════════════════════════════════════════════════════════════════════════════
@Composable
fun TabNguoiDung(nguoiDungs: List<NguoiDung>, vm: AdminViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var editItem by remember { mutableStateOf<NguoiDung?>(null) }
    var searchText by remember { mutableStateOf("") }
    var showConfirmDelete by remember { mutableStateOf<NguoiDung?>(null) }

    val filtered = nguoiDungs.filter {
        it.hoTen.contains(searchText, ignoreCase = true) || it.email.contains(searchText, ignoreCase = true)
    }

    if (showDialog || editItem != null) {
        DialogNguoiDung(
            user = editItem,
            onDismiss = { showDialog = false; editItem = null },
            onConfirm = { hoten, email, sdt, mk, vaitro ->
                if (editItem != null) vm.suaNguoiDung(editItem!!.id, hoten, email, sdt, vaitro)
                else vm.themNguoiDung(hoten, email, sdt, mk, vaitro)
                showDialog = false; editItem = null
            }
        )
    }

    showConfirmDelete?.let { user ->
        AlertDialog(
            onDismissRequest = { showConfirmDelete = null },
            title = { Text("Xác nhận xóa") },
            text = { Text("Xóa người dùng \"${user.hoTen}\"? Hành động này không thể hoàn tác.") },
            confirmButton = {
                Button(onClick = { vm.xoaNguoiDung(user.id); showConfirmDelete = null }, colors = ButtonDefaults.buttonColors(containerColor = AdminRed)) { Text("Xóa") }
            },
            dismissButton = { TextButton(onClick = { showConfirmDelete = null }) { Text("Hủy") } }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Tìm theo tên, email...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = AdminAccent,
                modifier = Modifier.size(48.dp)
            ) { Icon(Icons.Default.PersonAdd, null, tint = Color.White) }
        }

        // Thống kê nhanh
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val admins = nguoiDungs.count { it.vaiTro == "admin" }
            val customers = nguoiDungs.count { it.vaiTro != "admin" }
            MiniStatChip("Admin: $admins", AdminRed)
            MiniStatChip("Khách: $customers", AdminGreen)
            MiniStatChip("Tổng: ${nguoiDungs.size}", AdminAccent)
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(filtered) { user ->
                NguoiDungCard(
                    user = user,
                    onEdit = { editItem = user },
                    onDelete = { showConfirmDelete = user }
                )
            }
        }
    }
}

@Composable
fun MiniStatChip(text: String, color: Color) {
    Box(
        Modifier.clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = 0.1f)).padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(text, fontSize = 12.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun NguoiDungCard(user: NguoiDung, onEdit: () -> Unit, onDelete: () -> Unit) {
    val isAdmin = user.vaiTro == "admin"
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AdminCard),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(46.dp).clip(CircleShape).background(if (isAdmin) AdminRed else AdminAccent),
                contentAlignment = Alignment.Center
            ) {
                Text(user.hoTen.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(user.hoTen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Box(
                        Modifier.clip(RoundedCornerShape(4.dp))
                            .background(if (isAdmin) AdminRed.copy(alpha = 0.1f) else AdminGreen.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(if (isAdmin) "Admin" else "Khách", fontSize = 10.sp, color = if (isAdmin) AdminRed else AdminGreen, fontWeight = FontWeight.Bold)
                    }
                }
                Text(user.email, fontSize = 12.sp, color = Color.Gray)
                Text(user.soDienThoai ?: "Chưa có SĐT", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Edit, null, tint = AdminAccent, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Delete, null, tint = AdminRed, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun DialogNguoiDung(
    user: NguoiDung? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var hoten by remember { mutableStateOf(user?.hoTen ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var sdt by remember { mutableStateOf(user?.soDienThoai ?: "") }
    var matkhau by remember { mutableStateOf("") }
    var vaitro by remember { mutableStateOf(user?.vaiTro ?: "khachhang") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Thêm người dùng" else "Sửa người dùng", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminTextField(hoten, { hoten = it }, "Họ tên *")
                AdminTextField(email, { email = it }, "Email *", KeyboardType.Email)
                AdminTextField(sdt, { sdt = it }, "Số điện thoại", KeyboardType.Phone)
                if (user == null) {
                    AdminTextField(matkhau, { matkhau = it }, "Mật khẩu *", KeyboardType.Password)
                }
                Text("Vai trò:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = vaitro == "khachhang", onClick = { vaitro = "khachhang" }, colors = RadioButtonDefaults.colors(selectedColor = AdminAccent))
                    Text("Khách hàng", fontSize = 13.sp)
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = vaitro == "admin", onClick = { vaitro = "admin" }, colors = RadioButtonDefaults.colors(selectedColor = AdminRed))
                    Text("Admin", fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(hoten, email, sdt, matkhau, vaitro) },
                colors = ButtonDefaults.buttonColors(containerColor = AdminAccent),
                enabled = hoten.isNotBlank() && email.isNotBlank()
            ) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } },
        shape = RoundedCornerShape(20.dp)
    )
}

// ════════════════════════════════════════════════════════════════════════════
// HELPER COMPONENTS & FUNCTIONS
// ════════════════════════════════════════════════════════════════════════════

@Composable
fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier.fillMaxWidth(),
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        singleLine = singleLine,
        maxLines = if (singleLine) 1 else 4,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

fun formatVnd(amount: Double): String =
    "${String.format(Locale.getDefault(), "%,.0f", amount)}đ"

fun trangThaiLabel(trangThai: String): String = when (trangThai) {
    "choduyet"      -> "Chờ duyệt"
    "daxacnhan"     -> "Đã xác nhận"
    "danggiaohang"  -> "Đang giao hàng"
    "hoanthanh"     -> "Hoàn thành"
    "dahuy"         -> "Đã hủy"
    else            -> trangThai
}

fun trangThaiColor(trangThai: String): Pair<Color, Color> = when (trangThai) {
    "choduyet"     -> Color(0xFFFFF3E0) to Color(0xFFF57C00)
    "daxacnhan"    -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
    "danggiaohang" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
    "hoanthanh"    -> Color(0xFFE8F5E9) to AdminGreen
    "dahuy"        -> Color(0xFFFFEBEE) to AdminRed
    else           -> Color(0xFFF5F5F5) to Color.Gray
}

// Trả về (nextStatus, buttonColor, icon, label) hoặc null nếu không có bước tiếp
fun nextTrangThai(trangThai: String): Quadruple<String, Color, ImageVector, String>? = when (trangThai) {
    "choduyet"     -> Quadruple("daxacnhan",    AdminAccent,  Icons.Default.CheckCircle,    "Xác nhận đơn hàng")
    "daxacnhan"    -> Quadruple("danggiaohang", AdminGreen,   Icons.Default.LocalShipping,  "Bắt đầu giao hàng")
    "danggiaohang" -> Quadruple("hoanthanh",    AdminAccent,  Icons.Default.Done,           "Xác nhận đã giao")
    else           -> null
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
