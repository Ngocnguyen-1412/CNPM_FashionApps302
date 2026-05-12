package com.example.yoake.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.yoake.data.model.Product
import com.example.yoake.data.model.ProductColor
import com.example.yoake.ui.components.YoakeDetailTopAppBar
import com.example.yoake.ui.theme.*

// ─── Data contract ────────────────────────────────────────────────────────────

data class ProductDetailUiState(
    val isLoading: Boolean   = true,
    val product: Product?    = null,
    val addedToCart: Boolean = false
)

// ─── Screen ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    uiState: ProductDetailUiState   = ProductDetailUiState(),
    onBackClick: () -> Unit         = {},
    onCartClick: () -> Unit         = {},
    onAddToCart: (String, String) -> Unit = { _, _ -> }   // (colorName, size)
) {
    val product = uiState.product

    Scaffold(
        topBar = {
            YoakeDetailTopAppBar(onBackClick = onBackClick, onCartClick = onCartClick)
        },
        containerColor = Background
    ) { innerPadding ->

        if (uiState.isLoading || product == null) {
            ProductDetailSkeleton(Modifier.padding(innerPadding))
            return@Scaffold
        }

        // State
        val pagerState = rememberPagerState { product.imageUrls.size.coerceAtLeast(1) }
        var selectedColor by remember { mutableStateOf(product.availableColors.firstOrNull()) }
        var selectedSize  by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Image pager ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(SurfaceContainerLow)
            ) {
                HorizontalPager(
                    state    = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    AsyncImage(
                        model              = product.imageUrls[page],
                        contentDescription = "${product.name} image ${page + 1}",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                }

                // Page counter badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    color         = Surface.copy(alpha = 0.85f),
                    shape         = MaterialTheme.shapes.extraSmall,
                    tonalElevation = 0.dp
                ) {
                    Text(
                        "${pagerState.currentPage + 1} / ${product.imageUrls.size}",
                        style    = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    repeat(product.imageUrls.size) { index ->
                        Box(
                            Modifier
                                .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == pagerState.currentPage) OnSurface
                                    else OnSurface.copy(alpha = 0.3f)
                                )
                        )
                    }
                }
            }

            // ── Product info ─────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {

                Text(product.name, style = MaterialTheme.typography.displayLarge)
                Spacer(Modifier.height(8.dp))
                Text(product.priceFormatted, style = MaterialTheme.typography.bodyLarge, color = OnSurfaceVariant)
                Spacer(Modifier.height(24.dp))
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant
                )

                Spacer(Modifier.height(40.dp))

                // ── Color selector ───────────────────────────────────────────
                ColorSelector(
                    colors   = product.availableColors,
                    selected = selectedColor,
                    onSelect = { selectedColor = it }
                )

                Spacer(Modifier.height(32.dp))

                // ── Size selector ────────────────────────────────────────────
                SizeSelector(
                    sizes    = product.availableSizes,
                    selected = selectedSize,
                    onSelect = { selectedSize = it }
                )

                Spacer(Modifier.height(40.dp))

                // ── Add to cart CTA ──────────────────────────────────────────
                Button(
                    onClick  = { selectedColor?.let { c -> selectedSize?.let { s -> onAddToCart(c.name, s) } } },
                    enabled  = selectedColor != null && selectedSize != null,
                    shape    = MaterialTheme.shapes.extraSmall,
                    modifier = Modifier.fillMaxWidth(),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = Primary,
                        contentColor           = OnPrimary,
                        disabledContainerColor = SurfaceContainerHigh,
                        disabledContentColor   = OnSurfaceVariant
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text("ADD TO CART", style = MaterialTheme.typography.labelSmall)
                }

                // Prompt when size not selected yet
                AnimatedVisibility(visible = selectedSize == null, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        "Please select a size",
                        style    = MaterialTheme.typography.bodySmall,
                        color    = Secondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

// ─── Color selector ───────────────────────────────────────────────────────────

@Composable
private fun ColorSelector(
    colors: List<ProductColor>,
    selected: ProductColor?,
    onSelect: (ProductColor) -> Unit
) {
    Column {
        Text(
            "COLOR : ${selected?.name?.uppercase() ?: ""}",
            style = MaterialTheme.typography.bodySmall,
            color = OnSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            colors.forEach { color ->
                val parsedColor = runCatching { Color(android.graphics.Color.parseColor(color.hex)) }
                    .getOrDefault(SurfaceContainer)
                val isSelected  = color == selected

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(parsedColor)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) OnSurface else OutlineVariant,
                            shape = CircleShape
                        )
                        .clickable { onSelect(color) }
                )
            }
        }
    }
}

// ─── Size selector ────────────────────────────────────────────────────────────

@Composable
private fun SizeSelector(
    sizes: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                "SIZE",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant
            )
            TextButton(onClick = {}) {
                Text(
                    "Size Guide",
                    style = MaterialTheme.typography.bodySmall,
                    color = Secondary
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            sizes.forEach { size ->
                val isSelected = size == selected
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(if (isSelected) OnSurface else Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) OnSurface else OutlineVariant,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .clickable { onSelect(size) }
                ) {
                    Text(
                        size,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) Surface else OnSurface
                    )
                }
            }
        }
    }
}

// ─── Skeleton ─────────────────────────────────────────────────────────────────

@Composable
private fun ProductDetailSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .background(SurfaceContainer)
        )
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.fillMaxWidth(0.75f).height(32.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
            Box(Modifier.fillMaxWidth(0.3f).height(20.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(Modifier.fillMaxWidth().height(14.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
            }
        }
    }
}
