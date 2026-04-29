package com.healthhub.cm

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat

class DrugSearchActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var llResults: LinearLayout
    private lateinit var tvResultsLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_drug_search)

        etSearch = findViewById(R.id.et_search)
        btnSearch = findViewById(R.id.btn_search)
        llResults = findViewById(R.id.ll_results)
        tvResultsLabel = findViewById(R.id.tv_results_label)

        // Back button
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        // Show all drugs on load
        showDrugs(AppData.drugs)

        // Search button
        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            val results = AppData.searchDrugs(query)
            showDrugs(results)
            if (results.isEmpty()) {
                tvResultsLabel.text = "No drugs found for \"$query\""
            } else {
                tvResultsLabel.text = "${results.size} drug(s) found"
            }
        }
    }

    private fun showDrugs(drugList: List<Drug>) {
        llResults.removeAllViews()
        tvResultsLabel.text = "${drugList.size} drug(s) available"

        drugList.forEach { drug ->
            val card = layoutInflater.inflate(R.layout.item_drug_card, llResults, false)

            card.findViewById<TextView>(R.id.tv_drug_name).text = drug.name
            card.findViewById<TextView>(R.id.tv_drug_category).text = drug.category
            card.findViewById<TextView>(R.id.tv_drug_desc).text = drug.description
            val listing = ListingManager.getListingForDrug(this, drug.id)
            card.findViewById<TextView>(R.id.tv_drug_price).text = if (listing != null) {
                "XAF ${listing.price.toInt()} ${drug.unit}"
            } else {
                "XAF ${drug.price.toInt()} ${drug.unit}"
            }

            card.findViewById<Button>(R.id.btn_find_pharmacy).setOnClickListener {
                val intent = Intent(this, PharmacyResultsActivity::class.java)
                intent.putExtra("drug_id", drug.id)
                intent.putExtra("drug_name", drug.name)
                intent.putExtra("drug_price", drug.price)
                startActivity(intent)
            }

            llResults.addView(card)
        }
    }
}
