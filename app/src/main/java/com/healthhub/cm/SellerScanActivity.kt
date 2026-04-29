package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class SellerScanActivity : AppCompatActivity() {

    private lateinit var etScanInput: EditText
    private lateinit var btnScan: Button
    private lateinit var tvDrugName: TextView
    private lateinit var tvDrugCategory: TextView
    private lateinit var tvDrugDesc: TextView
    private lateinit var tvDrugUnit: TextView
    private lateinit var llDrugInfo: LinearLayout
    private lateinit var etSellerPrice: EditText
    private lateinit var btnPublish: Button
    private lateinit var tvPublishStatus: TextView
    private var selectedDrug: Drug? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_seller_scan)

        etScanInput = findViewById(R.id.et_scan_input)
        btnScan = findViewById(R.id.btn_scan_drug)
        tvDrugName = findViewById(R.id.tv_found_drug_name)
        tvDrugCategory = findViewById(R.id.tv_found_drug_category)
        tvDrugDesc = findViewById(R.id.tv_found_drug_desc)
        tvDrugUnit = findViewById(R.id.tv_found_drug_unit)
        llDrugInfo = findViewById(R.id.ll_drug_info)
        etSellerPrice = findViewById(R.id.et_seller_price)
        btnPublish = findViewById(R.id.btn_publish_drug)
        tvPublishStatus = findViewById(R.id.tv_publish_status)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        btnScan.setOnClickListener { onScanDrug() }
        btnPublish.setOnClickListener { publishSelectedDrug() }

        llDrugInfo.visibility = View.GONE
        btnPublish.isEnabled = false
    }

    private fun onScanDrug() {
        val query = etScanInput.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a drug name or ID to scan", Toast.LENGTH_SHORT).show()
            return
        }

        val results = AppData.searchDrugs(query)
        if (results.isEmpty()) {
            tvPublishStatus.text = "No drug found for \"$query\". Try another name or ID."
            tvPublishStatus.visibility = View.VISIBLE
            llDrugInfo.visibility = View.GONE
            btnPublish.isEnabled = false
            selectedDrug = null
            return
        }

        selectedDrug = results.first()
        showDrugInfo(selectedDrug!!)
        tvPublishStatus.visibility = View.GONE
        btnPublish.isEnabled = true
    }

    private fun showDrugInfo(drug: Drug) {
        llDrugInfo.visibility = View.VISIBLE
        tvDrugName.text = drug.name
        tvDrugCategory.text = drug.category
        tvDrugDesc.text = drug.description
        tvDrugUnit.text = "Base price: XAF ${drug.price.toInt()} ${drug.unit}"
    }

    private fun publishSelectedDrug() {
        val drug = selectedDrug ?: return
        val priceText = etSellerPrice.text.toString().trim()
        val price = priceText.toDoubleOrNull()
        if (price == null || price <= 0.0) {
            Toast.makeText(this, "Enter a valid seller price", Toast.LENGTH_SHORT).show()
            return
        }

        val listing = SellerListing(
            id = ListingManager.generateListingId(),
            drugId = drug.id,
            drugName = drug.name,
            price = price,
            unit = drug.unit,
            timestamp = System.currentTimeMillis()
        )
        ListingManager.saveListing(this, listing)

        tvPublishStatus.text = "Listing published: ${drug.name} at XAF ${price.toInt()}"
        tvPublishStatus.visibility = View.VISIBLE
        etSellerPrice.text.clear()
        etScanInput.text.clear()
        llDrugInfo.visibility = View.GONE
        btnPublish.isEnabled = false

        startActivity(Intent(this, SellerDashboardActivity::class.java))
        finish()
    }
}
