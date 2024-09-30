package com.theo.openablelist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding

class ChildViewHolder(
    private val binding: ViewCategoryChildItemBinding
): ViewHolder(binding.root) {

    fun bind(title: String) {
        binding.root.text = title
    }
}