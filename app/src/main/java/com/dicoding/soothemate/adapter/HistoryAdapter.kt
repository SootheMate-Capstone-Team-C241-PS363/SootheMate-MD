package com.dicoding.soothemate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.soothemate.data.api.DataItem
import com.dicoding.soothemate.databinding.ItemLayoutHistoryBinding

class HistoryAdapter(private var items: List<DataItem>, private val clickCallback: (DataItem) -> Unit)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemLayoutHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem) {
            binding.tvPercentage.text = "${item.stressLevel}%"
            itemView.setOnClickListener { clickCallback(item) }
        }
    }

    fun submitList(newItems: List<DataItem?>) {
        items = newItems as List<DataItem>
        notifyDataSetChanged()
    }
}
