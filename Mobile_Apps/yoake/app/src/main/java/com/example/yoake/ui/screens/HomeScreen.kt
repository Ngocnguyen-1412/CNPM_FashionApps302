package com.example.yoake.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import coil.compose.AsyncImage
import com.example.yoake.data.model.Product
import com.example.yoake.ui.components.YoakeTopAppBar
import com.example.yoake.ui.theme.*

// ─── Data contract ────────────────────────────────────────────────────────────
// Replace the ViewModel stub below with a proper Hilt ViewModel + repository.

data class HomeUiState(
    val isLoading: Boolean         = true,
    val categories: List<String>   = emptyList(),
    val featuredProduct: Product?  = null,
    val gridProducts: List<Product> = emptyList(),
    val curatedTitle: String       = "",
    val curatedBody: String        = "",
    val curatedImageUrl: String    = ""
)

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    uiState: HomeUiState = HomeUiState(),   // inject via ViewModel
    onProductClick: (String) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCartClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            YoakeTopAppBar(onMenuClick = onMenuClick, onCartClick = onCartClick)
        },
        containerColor = Background
    ) { innerPadding ->

        if (uiState.isLoading) {
            HomeSkeletonLoader(Modifier.padding(innerPadding))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Category chips ───────────────────────────────────────────────
            var selectedCategory by remember { mutableStateOf(uiState.categories.firstOrNull()) }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(uiState.categories) { category ->
                    val isSelected = category == selectedCategory
                    OutlinedButton(
                        onClick = { selectedCategory = category },
                        border  = BorderStroke(1.dp, if (isSelected) OnSurface else OutlineVariant),
                        shape   = MaterialTheme.shapes.extraSmall,
                        colors  = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) SurfaceVariant else Color.Transparent,
                            contentColor   = if (isSelected) OnSurface else OnSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(category.uppercase(), style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // ── Asymmetric grid ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 64.dp)
            ) {
                // Hero (large featured item — full width, 3:4 ratio)
                uiState.featuredProduct?.let { hero ->
                    HeroProductCard(
                        product     = hero,
                        onClick     = { onProductClick(hero.id) },
                        modifier    = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .padding(bottom = 16.dp)
                    )
                }

                // Two-column grid for remaining products
                val chunked = uiState.gridProducts.chunked(2)
                chunked.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        row.forEach { product ->
                            ProductGridCard(
                                product  = product,
                                onClick  = { onProductClick(product.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Fill empty cell if row has only one item
                        if (row.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            // ── Curated banner ───────────────────────────────────────────────
            CuratedSection(
                title      = uiState.curatedTitle,
                body       = uiState.curatedBody,
                imageUrl   = uiState.curatedImageUrl,
                onCtaClick = {}
            )

            Spacer(Modifier.height(80.dp)) // bottom nav clearance
        }
    }
}

// ─── Composable sub-components ────────────────────────────────────────────────

@Composable
private fun HeroProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model              = product.imageUrls.firstOrNull(),
            contentDescription = product.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )
        // Gradient scrim + text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.55f))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Text(
                    product.description,
                    style    = MaterialTheme.typography.bodyMedium,
                    color    = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ProductGridCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        AsyncImage(
            model              = product.imageUrls.firstOrNull(),
            contentDescription = product.name,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(SurfaceContainer)
        )
        Spacer(Modifier.height(8.dp))
        Text(product.name, style = MaterialTheme.typography.bodyMedium, color = OnSurface)
        Text(product.priceFormatted, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CuratedSection(
    title: String,
    body: String,
    imageUrl: String,
    onCtaClick: () -> Unit
) {
    Surface(color = SurfaceContainerLow) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 64.dp)) {
            Text(title, style = MaterialTheme.typography.displayMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                body,
                style = MaterialTheme.typography.bodyLarge,
                color = OnSurfaceVariant
            )
            Spacer(Modifier.height(32.dp))
            OutlinedButton(
                onClick        = onCtaClick,
                shape          = MaterialTheme.shapes.extraSmall,
                border         = BorderStroke(1.dp, Outline),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("DISCOVER MORE", style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(32.dp))
            AsyncImage(
                model              = imageUrl,
                contentDescription = "Curated collection",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(SurfaceContainer)
            )
        }
    }
}

// ─── Skeleton loader shown while data is fetching ────────────────────────────

@Composable
private fun HomeSkeletonLoader(modifier: Modifier = Modifier) {
    val shimmer by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue   = 0.3f,
        targetValue    = 0.7f,
        animationSpec  = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label          = "alpha"
    )
    val shimmerColor = SurfaceContainer.copy(alpha = shimmer)

    Column(modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        // Category chips skeleton
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 24.dp)) {
            repeat(4) {
                Box(
                    Modifier
                        .width(80.dp)
                        .height(32.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(shimmerColor)
                )
            }
        }
        // Hero skeleton
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(shimmerColor)
        )
        Spacer(Modifier.height(16.dp))
        // Grid skeleton
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(2) {
                Box(
                    Modifier
                        .weight(1f)
                        .aspectRatio(3f / 4f)
                        .clip(MaterialTheme.shapes.extraSmall)
                        .background(shimmerColor)
                )
            }
        }
    }
}
