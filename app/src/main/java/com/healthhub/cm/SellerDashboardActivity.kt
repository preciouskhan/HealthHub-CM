package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class SellerDashboardActivity : AppCompatActivity() {

    private lateinit var llListings: LinearLayout
    private lateinit var tvListingEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_seller_dashboard)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<Button>(R.id.btn_add_listing).setOnClickListener {
            startActivity(Intent(this, SellerScanActivity::class.java))
        }

        llListings = findViewById(R.id.ll_listings)
        tvListingEmpty = findViewById(R.id.tv_listing_empty)

        loadOrders()
        loadListings()
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
        loadListings()
    }

    private fun loadOrders() {
        val llOrders = findViewById<LinearLayout>(R.id.ll_orders)
        val tvEmpty = findViewById<TextView>(R.id.tv_empty)
        llOrders.removeAllViews()

        val orders = OrderManager.getAllOrders(this)

        if (orders.isEmpty()) {
            tvEmpty.visibility = android.view.View.VISIBLE
            return
        }

        tvEmpty.visibility = android.view.View.GONE
        val tvOrderCount = findViewById<TextView>(R.id.tv_order_count)
        val pending = orders.count { it.status == "pending" }
        tvOrderCount.text = "${orders.size} orders · $pending pending"

        // Show newest first
        orders.reversed().forEach { order ->
            val card = layoutInflater.inflate(R.layout.item_order_card, llOrders, false)

            card.findViewById<TextView>(R.id.tv_order_drug).text = order.drugName
            card.findViewById<TextView>(R.id.tv_order_qty).text = "Qty: ${order.quantity}"
            card.findViewById<TextView>(R.id.tv_order_total).text = "XAF ${order.totalPrice.toInt()}"
            card.findViewById<TextView>(R.id.tv_order_note).text =
                if (order.buyerNote.isNotEmpty()) "Note: ${order.buyerNote}" else "No note"

            val tvStatus = card.findViewById<TextView>(R.id.tv_order_status)
            tvStatus.text = order.status.uppercase()
            when (order.status) {
                "pending" -> {
                    tvStatus.setTextColor(0xFFd97706.toInt())
                    tvStatus.setBackgroundColor(0xFFFEF3C7.toInt())
                }
                "accepted" -> {
                    tvStatus.setTextColor(0xFF007628.toInt())
                    tvStatus.setBackgroundColor(0xFFE4F5EA.toInt())
                }
                "rejected" -> {
                    tvStatus.setTextColor(0xFF760000.toInt())
                    tvStatus.setBackgroundColor(0xFFFDF0F0.toInt())
                }
            }

            val btnAccept = card.findViewById<Button>(R.id.btn_accept)
            val btnReject = card.findViewById<Button>(R.id.btn_reject)

            if (order.status != "pending") {
                btnAccept.isEnabled = false
                btnReject.isEnabled = false
                btnAccept.alpha = 0.4f
                btnReject.alpha = 0.4f
            }

            btnAccept.setOnClickListener {
                OrderManager.updateOrderStatus(this, order.id, "accepted")
                Toast.makeText(this, "Order accepted!", Toast.LENGTH_SHORT).show()
                loadOrders()
            }

            btnReject.setOnClickListener {
                OrderManager.updateOrderStatus(this, order.id, "rejected")
                Toast.makeText(this, "Order rejected", Toast.LENGTH_SHORT).show()
                loadOrders()
            }

            llOrders.addView(card)
        }
    }

    private fun loadListings() {
        llListings.removeAllViews()
        val listings = ListingManager.getAllListings(this)

        if (listings.isEmpty()) {
            tvListingEmpty.visibility = android.view.View.VISIBLE
            return
        }

        tvListingEmpty.visibility = android.view.View.GONE
        listings.reversed().forEach { listing ->
            val card = layoutInflater.inflate(R.layout.item_seller_listing_card, llListings, false)
            card.findViewById<TextView>(R.id.tv_listing_drug_name).text = listing.drugName
            card.findViewById<TextView>(R.id.tv_listing_drug_price).text = "XAF ${listing.price.toInt()} ${listing.unit}"
            card.findViewById<TextView>(R.id.tv_listing_subtitle).text = "Published seller price"
            llListings.addView(card)
        }
    }
}
