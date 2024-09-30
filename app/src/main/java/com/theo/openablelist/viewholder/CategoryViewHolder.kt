package com.theo.openablelist.viewholder

import com.theo.openablelist.ParentViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class CategoryViewHolder(
    private val binding: ViewCategoryParentItemBinding,
    private val onClick: (String) -> Unit
): ParentViewHolder(binding.root) {

    fun bind(title: String) {
        binding.title.text = title
    }

    fun updateArrow(isOpen: Boolean) {
        binding.arrow.isSelected = isOpen
    }

    override fun onClick() {
        onClick(binding.title.text.toString())
    }
}