package com.example.yoake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.yoake.data.model.Order
import com.example.yoake.data.model.OrderStatus
import com.example.yoake.ui.components.YoakeTopAppBar
import com.example.yoake.ui.theme.*

// ─── Data contract ────────────────────────────────────────────────────────────

data class OrderHistoryUiState(
    val isLoading: Boolean       = true,
    val orders: List<Order>      = emptyList(),
    val totalCount: Int          = 0,
    val canLoadMore: Boolean     = false
)

// ─── Screen ───────────────────────────────────────────────────────────────────

@Composable
fun OrderHistoryScreen(
    uiState: OrderHistoryUiState = OrderHistoryUiState(),
    onBackClick: () -> Unit = {},
    onViewDetails: (Order) -> Unit = {},
    onTrackOrder: (Order) -> Unit = {},
    onReorder: (Order) -> Unit = {},
    onLoadMore: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            YoakeTopAppBar(onMenuClick = onBackClick)
        },
        containerColor = Background
    ) { innerPadding ->

        LazyColumn(
            modifier        = Modifier.padding(innerPadding),
            contentPadding  = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Section header ───────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        "Order History",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "A curated archive of your past acquisitions.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Secondary
                    )
                }
            }

            // ── Order cards ──────────────────────────────────────────────────
            if (uiState.isLoading) {
                items(3) { OrderCardSkeleton() }
            } else {
                items(uiState.orders, key = { it.id }) { order ->
                    OrderCard(
                        order        = order,
                        onPrimaryAction = {
                            when (order.status) {
                                OrderStatus.DELIVERED   -> onViewDetails(order)
                                OrderStatus.IN_TRANSIT  -> onTrackOrder(order)
                                else                    -> onReorder(order)
                            }
                        }
                    )
                }
            }

            // ── Pagination footer ────────────────────────────────────────────
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    Text(
                        "Showing ${uiState.orders.size} of ${uiState.totalCount} orders".uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Secondary
                    )
                    Spacer(Modifier.height(16.dp))
                    if (uiState.canLoadMore) {
                        TextButton(onClick = onLoadMore) {
                            Text(
                                "LOAD MORE",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            // Bottom nav clearance
            item { Spacer(Modifier.height(72.dp)) }
        }
    }
}

// ─── Order card ───────────────────────────────────────────────────────────────

@Composable
private fun OrderCard(
    order: Order,
    onPrimaryAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 0.dp, shape = MaterialTheme.shapes.extraSmall)
            .background(SurfaceContainerLowest)
            .height(IntrinsicSize.Min)
    ) {
        // Thumbnail
        AsyncImage(
            model              = order.imageUrl,
            contentDescription = order.productName,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.extraSmall)
                .background(SurfaceVariant)
        )

        // Info column
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Status + date row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = order.status.label(),
                    style = MaterialTheme.typography.labelSmall,
                    color = order.status.color()
                )
                Text(
                    text  = order.dateFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Secondary
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(order.productName, style = MaterialTheme.typography.headlineSmall)
            Text(
                "Order #${order.id}",
                style = MaterialTheme.typography.bodySmall,
                color = Outline
            )
            Spacer(Modifier.height(16.dp))

            // Price + action row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Bottom
            ) {
                Text(order.priceFormatted, style = MaterialTheme.typography.displayMedium)
                OutlinedButton(
                    onClick        = onPrimaryAction,
                    shape          = MaterialTheme.shapes.extraSmall,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        order.status.actionLabel(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

// ─── Skeleton card ────────────────────────────────────────────────────────────

@Composable
private fun OrderCardSkeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(SurfaceContainerLowest)
    ) {
        Box(
            Modifier
                .width(120.dp)
                .fillMaxHeight()
                .background(SurfaceContainer)
        )
        Column(
            Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.width(80.dp).height(12.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
            Box(Modifier.fillMaxWidth(0.7f).height(16.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
            Box(Modifier.fillMaxWidth(0.5f).height(12.dp).clip(MaterialTheme.shapes.extraSmall).background(SurfaceContainer))
        }
    }
}

// ─── Extension helpers on OrderStatus ────────────────────────────────────────

private fun OrderStatus.label(): String = when (this) {
    OrderStatus.DELIVERED  -> "DELIVERED"
    OrderStatus.IN_TRANSIT -> "IN TRANSIT"
    OrderStatus.PROCESSING -> "PROCESSING"
    OrderStatus.CANCELLED  -> "CANCELLED"
}

private fun OrderStatus.actionLabel(): String = when (this) {
    OrderStatus.DELIVERED  -> "VIEW DETAILS"
    OrderStatus.IN_TRANSIT -> "TRACK ORDER"
    OrderStatus.PROCESSING -> "VIEW DETAILS"
    OrderStatus.CANCELLED  -> "REORDER"
}

@Composable
private fun OrderStatus.color(): Color = when (this) {
    OrderStatus.DELIVERED  -> Primary
    OrderStatus.IN_TRANSIT -> OnSurfaceVariant
    OrderStatus.PROCESSING -> Secondary
    OrderStatus.CANCELLED  -> Error
}
