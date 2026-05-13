package com.example.myapplication.giao_dien

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.du_lieu.SanPham
import com.example.myapplication.ui.theme.PinkPrimary
import java.util.Locale

@Composable
fun LogoN3h2(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PinkPrimary, Color(0xFFFF80AB))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "N3", fontSize = 36.sp, fontWeight = FontWeight.Black, color = PinkPrimary)
            Text(text = "h2", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFFFF4081))
        }
        Text(text = "FASHION SHOP", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 5.sp)
    }
}

@Composable
fun ItemSanPham(sp: SanPham, onClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick(sp.id) }
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter("http://10.0.2.2/shop_quan_ao/uploads/${sp.hinhanh1}"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    contentScale = ContentScale.Crop
                )
                if (sp.giamGia > 0) {
                    Surface(
                        color = Color(0xFFFF1744),
                        shape = RoundedCornerShape(bottomEnd = 14.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            " -${sp.giamGia}% ",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    sp.thuongHieu?.uppercase() ?: "N3H2 PREMIUM",
                    color = PinkPrimary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    sp.tenSanPham,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                val giaSauGiam = sp.gia * (1 - sp.giamGia / 100.0)
                Text(
                    "${String.format(Locale.getDefault(), "%,.0f", giaSauGiam)}đ",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}
