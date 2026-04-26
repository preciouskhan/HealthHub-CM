package com.healthhub.cm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat

class PharmacyResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_pharmacy_results)

        val drugId = intent.getStringExtra("drug_id") ?: ""
        val drugName = intent.getStringExtra("drug_name") ?: ""
        val drugPrice = intent.getDoubleExtra("drug_price", 0.0)

        findViewById<TextView>(R.id.tv_drug_title).text = "Pharmacies with $drugName"
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val pharmacies = AppData.getPharmaciesWithDrug(drugId)
        val llList = findViewById<LinearLayout>(R.id.ll_pharmacy_list)

        findViewById<TextView>(R.id.tv_count).text = "${pharmacies.size} pharmacies found near you"

        pharmacies.forEach { pharmacy ->
            val card = layoutInflater.inflate(R.layout.item_pharmacy_card, llList, false)

            card.findViewById<TextView>(R.id.tv_pharmacy_name).text = pharmacy.name
            card.findViewById<TextView>(R.id.tv_pharmacy_location).text = "📍 ${pharmacy.location}"
            card.findViewById<TextView>(R.id.tv_pharmacy_distance).text = pharmacy.distance
            card.findViewById<TextView>(R.id.tv_pharmacy_status).apply {
                text = if (pharmacy.isOpen) "Open Now" else "Closed"
                setTextColor(if (pharmacy.isOpen) 0xFF007628.toInt() else 0xFF760000.toInt())
                setBackgroundColor(if (pharmacy.isOpen) 0xFFE4F5EA.toInt() else 0xFFFDF0F0.toInt())
            }

            // WhatsApp button
            card.findViewById<Button>(R.id.btn_whatsapp).setOnClickListener {
                val number = pharmacy.whatsapp.replace("+", "").replace(" ", "")
                val url = "https://wa.me/$number?text=Hello, I'm looking for $drugName. Is it available?"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }

            // Select button → Place Order
            card.findViewById<Button>(R.id.btn_select).setOnClickListener {
                val intent = Intent(this, PlaceOrderActivity::class.java)
                intent.putExtra("drug_id", drugId)
                intent.putExtra("drug_name", drugName)
                intent.putExtra("drug_price", drugPrice)
                intent.putExtra("pharmacy_id", pharmacy.id)
                intent.putExtra("pharmacy_name", pharmacy.name)
                intent.putExtra("pharmacy_whatsapp", pharmacy.whatsapp)
                startActivity(intent)
            }

            llList.addView(card)
        }
    }
}
