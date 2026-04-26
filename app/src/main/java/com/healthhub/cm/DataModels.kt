package com.healthhub.cm

// ── DATA CLASSES ──────────────────────────────────────

data class Drug(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Double,
    val unit: String
)

data class Pharmacy(
    val id: String,
    val name: String,
    val location: String,
    val distance: String,
    val whatsapp: String,
    val isOpen: Boolean,
    val drugs: List<String> // drug IDs available
)

data class Order(
    val id: String,
    val drugName: String,
    val drugPrice: Double,
    val quantity: Int,
    val totalPrice: Double,
    val pharmacyName: String,
    val pharmacyWhatsapp: String,
    val buyerNote: String,
    val status: String, // "pending", "accepted", "rejected"
    val timestamp: Long
)
