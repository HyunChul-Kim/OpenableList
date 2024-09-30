package com.theo.openablelist.viewholder

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding

class SubCategoryViewHolder(
    private val binding: ViewCategoryChildItemBinding,
    onClick: (String) -> Unit
): ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onClick(binding.root.text.toString())
        }
    }

    fun bind(title: String) {
        binding.root.text = title
    }
}