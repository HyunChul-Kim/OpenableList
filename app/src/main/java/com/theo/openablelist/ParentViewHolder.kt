package com.theo.openablelist

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class ParentViewHolder(
    private val binding: ViewCategoryParentItemBinding
): ViewHolder(binding.root) {

    fun bind(title: String) {
        binding.title.text = title
    }

    fun updateArrow(isOpen: Boolean) {
        binding.arrow.isSelected = isOpen
    }
}