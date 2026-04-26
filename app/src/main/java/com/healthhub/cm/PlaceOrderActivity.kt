package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class PlaceOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_place_order)

        val drugId = intent.getStringExtra("drug_id") ?: ""
        val drugName = intent.getStringExtra("drug_name") ?: ""
        val drugPrice = intent.getDoubleExtra("drug_price", 0.0)
        val pharmacyId = intent.getStringExtra("pharmacy_id") ?: ""
        val pharmacyName = intent.getStringExtra("pharmacy_name") ?: ""
        val pharmacyWhatsapp = intent.getStringExtra("pharmacy_whatsapp") ?: ""

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val tvDrugName = findViewById<TextView>(R.id.tv_order_drug_name)
        val tvPharmacyName = findViewById<TextView>(R.id.tv_order_pharmacy)
        val tvUnitPrice = findViewById<TextView>(R.id.tv_unit_price)
        val tvTotal = findViewById<TextView>(R.id.tv_total_price)
        val etQty = findViewById<EditText>(R.id.et_quantity)
        val etNote = findViewById<EditText>(R.id.et_note)
        val btnPlace = findViewById<Button>(R.id.btn_place_order)

        tvDrugName.text = drugName
        tvPharmacyName.text = pharmacyName
        tvUnitPrice.text = "XAF ${drugPrice.toInt()} per unit"
        tvTotal.text = "XAF ${drugPrice.toInt()}"

        // Update total when quantity changes
        etQty.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val qty = s.toString().toIntOrNull() ?: 1
                val total = qty * drugPrice
                tvTotal.text = "XAF ${total.toInt()}"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Place order
        btnPlace.setOnClickListener {
            val qty = etQty.text.toString().toIntOrNull()
            if (qty == null || qty <= 0) {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = qty * drugPrice
            val note = etNote.text.toString().trim()

            val order = Order(
                id = OrderManager.generateOrderId(),
                drugName = drugName,
                drugPrice = drugPrice,
                quantity = qty,
                totalPrice = total,
                pharmacyName = pharmacyName,
                pharmacyWhatsapp = pharmacyWhatsapp,
                buyerNote = note,
                status = "pending",
                timestamp = System.currentTimeMillis()
            )

            OrderManager.saveOrder(this, order)

            // Go to confirmation
            val intent = Intent(this, OrderConfirmationActivity::class.java)
            intent.putExtra("order_id", order.id)
            intent.putExtra("drug_name", drugName)
            intent.putExtra("quantity", qty)
            intent.putExtra("total_price", total)
            intent.putExtra("pharmacy_name", pharmacyName)
            intent.putExtra("pharmacy_whatsapp", pharmacyWhatsapp)
            startActivity(intent)
            finish()
        }
    }
}
