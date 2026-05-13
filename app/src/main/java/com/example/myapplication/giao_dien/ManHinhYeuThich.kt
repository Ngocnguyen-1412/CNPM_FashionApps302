package com.example.myapplication.giao_dien

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.mang.KhachHangRetrofit
import com.example.myapplication.ui.theme.PinkPrimary
import com.example.myapplication.viewmodel.NguoiDungViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhYeuThich(
    nguoiDungViewModel: NguoiDungViewModel,
    onBack: () -> Unit,
    onSanPhamClick: (Int) -> Unit
) {
    val nguoiDung = nguoiDungViewModel.nguoiDungHienTai.value
    var danhSachYeuThich by remember { mutableStateOf<List<SanPham>>(emptyList()) }
    var dangTai by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (nguoiDung != null) {
            try {
                danhSachYeuThich = KhachHangRetrofit.layGiaoDienApi.layDanhSachYeuThich(nguoiDung.id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                dangTai = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sản phẩm yêu thích", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF8F8F8))) {
            if (dangTai) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = PinkPrimary)
            } else if (danhSachYeuThich.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Favorite, null, modifier = Modifier.size(100.dp), tint = Color(0xFFFFC0CB))
                    Text("Chưa có sản phẩm yêu thích", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(danhSachYeuThich) { sp ->
                        ItemSanPham(sp = sp, onClick = { onSanPhamClick(sp.id) })
                    }
                }
            }
        }
    }
}
