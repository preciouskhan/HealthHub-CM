package com.healthhub.cm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class OrderConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_order_confirmation)

        val orderId = intent.getStringExtra("order_id") ?: ""
        val drugName = intent.getStringExtra("drug_name") ?: ""
        val quantity = intent.getIntExtra("quantity", 1)
        val totalPrice = intent.getDoubleExtra("total_price", 0.0)
        val pharmacyName = intent.getStringExtra("pharmacy_name") ?: ""
        val pharmacyWhatsapp = intent.getStringExtra("pharmacy_whatsapp") ?: ""

        findViewById<TextView>(R.id.tv_order_id).text = "Order ID: $orderId"
        findViewById<TextView>(R.id.tv_confirm_drug).text = drugName
        findViewById<TextView>(R.id.tv_confirm_qty).text = "Quantity: $quantity"
        findViewById<TextView>(R.id.tv_confirm_total).text = "Total: XAF ${totalPrice.toInt()}"
        findViewById<TextView>(R.id.tv_confirm_pharmacy).text = pharmacyName

        // WhatsApp button
        findViewById<Button>(R.id.btn_confirm_whatsapp).setOnClickListener {
            val number = pharmacyWhatsapp.replace("+", "").replace(" ", "")
            val msg = "Hello! I just placed an order for $quantity x $drugName. Order ID: $orderId. Total: XAF ${totalPrice.toInt()}"
            val url = "https://wa.me/$number?text=${Uri.encode(msg)}"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        // Back to home
        findViewById<Button>(R.id.btn_back_home).setOnClickListener {
            val intent = Intent(this, BuyerHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
