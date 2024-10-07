package com.theo.openablelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theo.openablelist.model.Child
import com.theo.openablelist.model.Parent
import com.theo.openablelist.model.Section
import com.theo.openablelist.viewholder.CategoryChildViewHolder
import com.theo.openablelist.viewholder.CategoryParentViewHolder
import com.theo.openablelist.viewholder.OpenableViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class CategoryListAdapter(
    categories: List<Section<String, String>>
): OpenableAdapter<String, String>(categories) {

    override fun onCreateParentViewHolder(parent: ViewGroup): OpenableViewHolder {
        return CategoryParentViewHolder(
            binding = ViewCategoryParentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = {}
        )
    }

    override fun onCreateChildViewHolder(parent: ViewGroup): OpenableViewHolder {
        return CategoryChildViewHolder(
            binding = ViewCategoryChildItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = {}
        )
    }

    override fun onBindParentViewHolder(
        holder: RecyclerView.ViewHolder,
        item: Parent<String>,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()) {
            super.onBindParentViewHolder(holder, item, payloads)
        } else {
            payloads.forEach { payload ->
                when(payload) {
                    OpenableItemCallback.PAYLOAD_SELECT_CHANGED -> {
                        (holder as CategoryParentViewHolder).updateArrow(item.isOpened)
                    }
                    else -> {
                        super.onBindParentViewHolder(holder, item, payloads)
                    }
                }
            }
        }
    }

    override fun onBindChildViewHolder(
        holder: RecyclerView.ViewHolder,
        item: Child<String>,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()) {
            super.onBindChildViewHolder(holder, item, payloads)
        } else {
            payloads.forEach { payload ->
                when(payload) {
                    OpenableItemCallback.PAYLOAD_SELECT_CHANGED -> {
                        (holder as CategoryChildViewHolder).updateSelect(item.isSelected)
                    }
                    else -> {
                        super.onBindChildViewHolder(holder, item, payloads)
                    }
                }
            }
        }
    }

    override fun onBindChildViewHolder(holder: RecyclerView.ViewHolder, item: Child<String>) {
        (holder as CategoryChildViewHolder).bind(item.data, item.isSelected)
    }

    override fun onBindParentViewHolder(holder: RecyclerView.ViewHolder, item: Parent<String>) {
        (holder as CategoryParentViewHolder).bind(item.data)
    }

}