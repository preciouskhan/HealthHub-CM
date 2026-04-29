package com.healthhub.cm

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ── HARDCODED DATA ────────────────────────────────────

object AppData {

    // ── DRUGS ──
    val drugs = listOf(
        Drug("d1", "Paracetamol 500mg", "Pain Relief",
            "Used for fever, headache and mild to moderate pain.", 500.0, "per strip"),
        Drug("d2", "Amoxicillin 250mg", "Antibiotic",
            "Broad-spectrum antibiotic for bacterial infections.", 1200.0, "per pack"),
        Drug("d3", "Neoskin Cream", "Skincare",
            "Treats skin infections, rashes and irritation.", 2500.0, "per tube"),
        Drug("d4", "Epiderm Cream", "Skincare",
            "Skin brightening and hyperpigmentation treatment.", 3500.0, "per tube"),
        Drug("d5", "Efferegant", "Vitamins",
            "Effervescent multivitamin tablet for daily health.", 800.0, "per tube"),
        Drug("d6", "Oral Rehydration Salts", "Hydration",
            "Treats dehydration caused by diarrhea or vomiting.", 300.0, "per sachet")
    )

    // ── PHARMACIES ──
    val pharmacies = listOf(
        Pharmacy(
            id = "p1",
            name = "Pharmacie Vatican",
            location = "Biyemassi, Yaoundé",
            distance = "0.3 km",
            whatsapp = "+237654241088",
            isOpen = true,
            drugs = listOf("d1", "d2", "d3", "d4", "d5", "d6")
        ),
        Pharmacy(
            id = "p2",
            name = "CityMed Drugstore",
            location = "Centre Ville, Yaoundé",
            distance = "0.8 km",
            whatsapp = "+237654241088",
            isOpen = true,
            drugs = listOf("d1", "d2", "d5", "d6")
        ),
        Pharmacy(
            id = "p3",
            name = "Clinique Sainte Marie",
            location = "Melen, Yaoundé",
            distance = "1.2 km",
            whatsapp = "+237654241088",
            isOpen = false,
            drugs = listOf("d1", "d3", "d4", "d6")
        ),
        Pharmacy(
            id = "p4",
            name = "Pharmacie Centrale",
            location = "Mvog-Mbi, Yaoundé",
            distance = "2.1 km",
            whatsapp = "+237654241088",
            isOpen = true,
            drugs = listOf("d1", "d2", "d3", "d4", "d5", "d6")
        )
    )

    // ── SEARCH DRUGS ──
    fun searchDrugs(query: String): List<Drug> {
        if (query.isBlank()) return drugs
        return drugs.filter {
            it.id.contains(query, ignoreCase = true) ||
            it.name.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
    }

    // ── GET PHARMACIES WITH DRUG ──
    fun getPharmaciesWithDrug(drugId: String): List<Pharmacy> {
        return pharmacies.filter { it.drugs.contains(drugId) }
    }
}

// ── ORDER MANAGER (SharedPreferences) ────────────────

object OrderManager {

    private const val PREF_NAME = "healthhub_orders"
    private const val KEY_ORDERS = "orders"
    private val gson = Gson()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getAllOrders(context: Context): MutableList<Order> {
        val json = getPrefs(context).getString(KEY_ORDERS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Order>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveOrder(context: Context, order: Order) {
        val orders = getAllOrders(context)
        orders.add(order)
        val json = gson.toJson(orders)
        getPrefs(context).edit().putString(KEY_ORDERS, json).apply()
    }

    fun updateOrderStatus(context: Context, orderId: String, newStatus: String) {
        val orders = getAllOrders(context)
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val updated = orders[index].copy(status = newStatus)
            orders[index] = updated
            val json = gson.toJson(orders)
            getPrefs(context).edit().putString(KEY_ORDERS, json).apply()
        }
    }

    fun getPendingOrders(context: Context): List<Order> {
        return getAllOrders(context).filter { it.status == "pending" }
    }

    fun generateOrderId(): String {
        return "ORD-${System.currentTimeMillis()}"
    }
}

object ListingManager {

    private const val PREF_NAME = "healthhub_listings"
    private const val KEY_LISTINGS = "listings"
    private val gson = Gson()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getAllListings(context: Context): MutableList<SellerListing> {
        val json = getPrefs(context).getString(KEY_LISTINGS, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<SellerListing>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveListing(context: Context, listing: SellerListing) {
        val listings = getAllListings(context)
        val existing = listings.indexOfFirst { it.drugId == listing.drugId }
        if (existing != -1) {
            listings[existing] = listing
        } else {
            listings.add(listing)
        }
        val json = gson.toJson(listings)
        getPrefs(context).edit().putString(KEY_LISTINGS, json).apply()
    }

    fun getListingForDrug(context: Context, drugId: String): SellerListing? {
        return getAllListings(context).firstOrNull { it.drugId == drugId }
    }

    fun generateListingId(): String {
        return "LIST-${System.currentTimeMillis()}"
    }
}
