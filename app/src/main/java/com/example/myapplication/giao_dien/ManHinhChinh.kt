package com.example.myapplication.giao_dien

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.du_lieu.DanhMuc
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.du_lieu.Banner
import com.example.myapplication.ui.theme.PinkPrimary
import com.example.myapplication.viewmodel.SanPhamViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManHinhChinh(
    viewModel: SanPhamViewModel,
    onSanPhamClick: (Int) -> Unit,
    onCartClick: () -> Unit
) {
    val danhMucs = viewModel.danhMucs.value
    val sanPhams = viewModel.sanPhams.value
    val banners = viewModel.banners.value
    val dangTai = viewModel.dangTai.value
    val searchText by viewModel.searchText

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LogoN3h2Small()
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Fashion & Lifestyle", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("N3h2 Premium", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Giỏ hàng", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkPrimary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    SearchBar(
                        value = searchText,
                        onValueChange = { viewModel.onSearchTextChange(it) }
                    )
                }

                if (searchText.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        BannerSpecialSection(banners)
                    }

                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Bộ Sưu Tập Hot", fontSize = 19.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    item(span = { GridItemSpan(2) }) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            item {
                                ItemDanhMucTatCa { viewModel.laySanPhamTheoDanhMuc(null) }
                            }
                            items(danhMucs) { dm ->
                                ItemDanhMuc(dm) { viewModel.laySanPhamTheoDanhMuc(dm.id) }
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(2) }) {
                    Text(
                        if (searchText.isEmpty()) "Gợi ý cho bạn" else "Kết quả cho \"$searchText\"",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                if (dangTai && sanPhams.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PinkPrimary)
                        }
                    }
                } else {
                    items(sanPhams) { sp ->
                        ItemSanPham(sp, onSanPhamClick)
                    }
                }
            }
        }
    }
}

@Composable
fun LogoN3h2Small() {
    Surface(
        modifier = Modifier.size(36.dp),
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("N3", color = PinkPrimary, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Bạn đang tìm phong cách nào?", fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PinkPrimary) },
        modifier = Modifier.fillMaxWidth().shadow(6.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = PinkPrimary,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun ItemDanhMucTatCa(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = Color(0xFFFFF0F5),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("Tất cả", color = PinkPrimary, fontWeight = FontWeight.Black, fontSize = 14.sp)
            }
        }
        Text("Tất cả", fontSize = 13.sp, modifier = Modifier.padding(top = 10.dp), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ItemDanhMuc(dm: DanhMuc, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Image(
                painter = rememberAsyncImagePainter("http://10.0.2.2/shop_quan_ao/uploads/${dm.hinhAnh}"),
                contentDescription = null,
                modifier = Modifier.padding(14.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
        Text(dm.tenDanhMuc, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 10.dp))
    }
}

@Composable
fun BannerSpecialSection(banners: List<Banner>) {
    if (banners.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(20.dp)).background(Color.LightGray))
    } else {
        val pagerState = rememberPagerState(pageCount = { banners.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(24.dp))
        ) { page ->
            Image(
                painter = rememberAsyncImagePainter("http://10.0.2.2/shop_quan_ao/uploads/${banners[page].hinhAnh}"),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
