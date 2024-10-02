package com.theo.openablelist.viewholder

import com.theo.openablelist.ParentViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class CategoryParentViewHolder(
    private val binding: ViewCategoryParentItemBinding,
    private val onClick: (String) -> Unit
): OpenableViewHolder(binding.root) {

    fun bind(title: String) {
        binding.title.text = title
    }

    fun updateArrow(isOpen: Boolean) {
        binding.arrow.isSelected = isOpen
    }

    override fun onClick(position: Int) {
        onClick(binding.title.text.toString())
    }

}