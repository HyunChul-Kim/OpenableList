package com.theo.openablelist.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding

class CategoryChildViewHolder(
    private val binding: ViewCategoryChildItemBinding,
    onClick: (String) -> Unit
): OpenableViewHolder(binding.root) {

    fun bind(title: String) {
        binding.root.text = title
    }

    fun updateSelect(isSelected: Boolean) {
        if(isSelected) {
            binding.root.setTextColor(Color.RED)
        } else {
            binding.root.setTextColor(Color.BLACK)
        }
    }

    override fun onClick(position: Int) {

    }
}