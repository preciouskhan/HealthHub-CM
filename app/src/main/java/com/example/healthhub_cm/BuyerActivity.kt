package com.healthhubcm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.healthhubcm.data.model.SearchResult
import com.healthhubcm.databinding.ActivityBuyerBinding
import com.healthhubcm.ui.buyer.adapter.SearchAdapter

class BuyerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerBinding
    private lateinit var adapter: SearchAdapter

    private val fakeData = listOf(
        SearchResult("Paracetamol", "Good Health Pharmacy", "1000 XAF", "1.2 km"),
        SearchResult("Amoxicillin", "City Clinic", "2500 XAF", "2.5 km"),
        SearchResult("Vitamin C", "Pharma Plus", "1500 XAF", "3.0 km")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SearchAdapter(fakeData)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()

            val filtered = fakeData.filter {
                it.name.contains(query, ignoreCase = true)
            }

            adapter.update(filtered)
        }
    }
}