package com.example.yoake.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

// ─────────────────────────────────────────────────────────────────────────────
//  WHY RETROFIT (not JDBC):
//  Android forbids direct TCP connections to MySQL on the main thread,
//  and Google blocks raw JDBC drivers from Play Store apps.
//  The correct pattern is:  Android ↔ REST API ↔ MySQL
//  Point BASE_URL at your Node/Spring/Laravel/etc. back-end server.
// ─────────────────────────────────────────────────────────────────────────────

object NetworkConfig {
    /**
     * Change this to your actual server URL.
     * Examples:
     *   Local emulator  → "http://10.0.2.2:3000/"
     *   Local device    → "http://192.168.1.X:3000/"
     *   Production      → "https://api.yourdomain.com/"
     */
    const val BASE_URL = "http://10.0.2.2:3000/"
}

// ─── DTOs (match v_product_detail / v_order_history SQL views) ────────────────

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price_amount: Double,
    val price_formatted: String,
    val category: String,
    val is_new: Boolean,
    val is_featured: Boolean,
    val image_urls: List<String>,
    val colors: List<ColorDto>,
    val sizes: List<String>
)

data class ColorDto(
    val name: String,
    val hex: String
)

data class OrderDto(
    val order_id: Int,
    val order_ref: String,          // "YK-000001"
    val product_name: String,
    val image_url: String?,
    val status: String,             // "Delivered", "In_Transit", "Processing", "Cancelled"
    val date_formatted: String,
    val price_formatted: String
)

data class UserDto(
    val user_id: Int,
    val display_name: String,
    val email: String,
    val avatar_url: String?
)

data class CategoryListDto(val categories: List<String>)

data class HomeDto(
    val categories: List<String>,
    val featured_product: ProductDto?,
    val grid_products: List<ProductDto>,
    val curated_title: String,
    val curated_body: String,
    val curated_image_url: String
)

data class AddToCartRequest(
    val user_id: Int,
    val product_id: Int,
    val quantity: Int,
    val selected_color: String,
    val selected_size: String
)

data class AddToCartResponse(val success: Boolean, val order_id: Int?)

// ─── Retrofit interface ───────────────────────────────────────────────────────

interface YoakeApiService {

    // Home screen — categories + featured + grid + curated
    @GET("home")
    suspend fun getHome(@Query("user_id") userId: Int): Response<HomeDto>

    // Product detail
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") productId: Int): Response<ProductDto>

    // Order history  (uses v_order_history view, excludes Cart/Pending)
    @GET("orders")
    suspend fun getOrders(
        @Query("user_id") userId: Int,
        @Query("page")    page: Int    = 1,
        @Query("limit")   limit: Int   = 10
    ): Response<OrdersPageDto>

    // Add item to cart
    @POST("cart/add")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<AddToCartResponse>

    // User profile
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): Response<UserDto>
}

data class OrdersPageDto(
    val orders: List<OrderDto>,
    val total_count: Int
)

// ─── Singleton Retrofit client ────────────────────────────────────────────────

object RetrofitClient {
    val api: YoakeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoakeApiService::class.java)
    }
}
