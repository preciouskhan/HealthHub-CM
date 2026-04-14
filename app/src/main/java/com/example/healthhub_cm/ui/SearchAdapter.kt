package com.healthhubcm.ui.buyer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.healthhubcm.data.model.SearchResult
import com.healthhubcm.databinding.ItemSearchBinding

class SearchAdapter(
    private var items: List<SearchResult>
) : RecyclerView.Adapter<SearchAdapter.VH>() {

    class VH(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.apply {
            tvName.text = item.name
            tvShop.text = item.shopName
            tvPrice.text = item.price
            tvDistance.text = item.distance
        }
    }

    override fun getItemCount() = items.size

    fun update(newItems: List<SearchResult>) {
        items = newItems
        notifyDataSetChanged()
    }
}