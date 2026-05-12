package com.example.yoake.data.repository

import com.example.yoake.data.model.*
import com.example.yoake.network.*
import com.example.yoake.ui.screens.HomeUiState

// ─────────────────────────────────────────────────────────────────────────────
//  YoakeRepository
//
//  Single source of truth. All ViewModels call this; none touch Retrofit directly.
//  Each function returns Result<T> so the ViewModel can handle errors cleanly.
// ─────────────────────────────────────────────────────────────────────────────

class YoakeRepository(
    private val api: YoakeApiService = RetrofitClient.api
) {

    // ── Home ──────────────────────────────────────────────────────────────────

    suspend fun getHomeData(userId: Int): Result<HomeUiState> = runCatching {
        val response = api.getHome(userId)
        val body = response.body() ?: error("Empty response from /home")
        HomeUiState(
            isLoading       = false,
            categories      = body.categories,
            featuredProduct = body.featured_product?.toDomain(),
            gridProducts    = body.grid_products.map { it.toDomain() },
            curatedTitle    = body.curated_title,
            curatedBody     = body.curated_body,
            curatedImageUrl = body.curated_image_url
        )
    }

    // ── Product detail ────────────────────────────────────────────────────────

    suspend fun getProduct(productId: String): Result<Product> = runCatching {
        val response = api.getProduct(productId.toInt())
        response.body()?.toDomain() ?: error("Product $productId not found")
    }

    // ── Orders ────────────────────────────────────────────────────────────────

    suspend fun getOrders(userId: Int, page: Int = 1): Result<OrdersPageDto> = runCatching {
        val response = api.getOrders(userId, page)
        response.body() ?: error("Empty response from /orders")
    }

    // ── Cart ──────────────────────────────────────────────────────────────────

    suspend fun addToCart(
        userId: Int,
        productId: String,
        color: String,
        size: String
    ): Result<AddToCartResponse> = runCatching {
        val response = api.addToCart(
            AddToCartRequest(
                user_id        = userId,
                product_id     = productId.toInt(),
                quantity       = 1,
                selected_color = color,
                selected_size  = size
            )
        )
        response.body() ?: error("Empty response from /cart/add")
    }

    // ── User ──────────────────────────────────────────────────────────────────

    suspend fun getUser(userId: Int): Result<User> = runCatching {
        val response = api.getUser(userId)
        response.body()?.toDomain() ?: error("User $userId not found")
    }
}

// ─── DTO → Domain mappers ─────────────────────────────────────────────────────

private fun ProductDto.toDomain() = Product(
    id              = id.toString(),
    name            = name,
    priceFormatted  = price_formatted,
    priceAmount     = price_amount,
    description     = description,
    imageUrls       = image_urls,
    availableColors = colors.map { ProductColor(it.name, it.hex) },
    availableSizes  = sizes,
    category        = category,
    isNew           = is_new
)

fun OrderDto.toDomain() = Order(
    id             = order_ref,
    productName    = product_name,
    imageUrl       = image_url ?: "",
    status         = status.toOrderStatus(),
    dateFormatted  = date_formatted,
    priceFormatted = price_formatted
)

private fun String.toOrderStatus(): OrderStatus = when (this.lowercase()) {
    "delivered"  -> OrderStatus.DELIVERED
    "in_transit" -> OrderStatus.IN_TRANSIT
    "processing" -> OrderStatus.PROCESSING
    "cancelled"  -> OrderStatus.CANCELLED
    else         -> OrderStatus.PROCESSING
}

private fun UserDto.toDomain() = User(
    id          = user_id.toString(),
    displayName = display_name,
    email       = email,
    avatarUrl   = avatar_url
)
