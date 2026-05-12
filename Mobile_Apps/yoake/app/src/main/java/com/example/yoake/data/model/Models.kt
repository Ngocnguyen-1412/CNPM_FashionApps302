package com.example.yoake.data.model

// ─── Product ─────────────────────────────────────────────────────────────────

data class Product(
    val id: String,
    val name: String,
    val priceFormatted: String,       // e.g. "$ 495.00"
    val priceAmount: Double,
    val description: String,
    val imageUrls: List<String>,      // ordered gallery images
    val availableColors: List<ProductColor>,
    val availableSizes: List<String>,
    val category: String,
    val isNew: Boolean = false
)

data class ProductColor(
    val name: String,
    val hex: String                   // e.g. "#8a9a5b"
)

// ─── Order ───────────────────────────────────────────────────────────────────

data class Order(
    val id: String,                   // e.g. "YK-882910"
    val productName: String,
    val imageUrl: String,
    val status: OrderStatus,
    val dateFormatted: String,        // e.g. "Oct 12, 2023"
    val priceFormatted: String
)

enum class OrderStatus {
    DELIVERED,
    IN_TRANSIT,
    PROCESSING,
    CANCELLED
}

// ─── User ────────────────────────────────────────────────────────────────────

data class User(
    val id: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String?
)
