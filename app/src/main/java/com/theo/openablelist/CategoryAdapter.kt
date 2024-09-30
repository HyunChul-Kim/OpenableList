package com.theo.openablelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class CategoryAdapter: OpenableAdapter<String, String>(
    isOnlyOneSectionOpen = true
) {

    override fun onCreateParentViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ParentViewHolder(
            ViewCategoryParentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onCreateChildViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ChildViewHolder(
            ViewCategoryChildItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindParentViewHolder(
        holder: RecyclerView.ViewHolder,
        section: Section<String, String>
    ) {
        (holder as ParentViewHolder).bind(section.data)
    }

    override fun onBindParentViewHolder(
        holder: RecyclerView.ViewHolder,
        section: Section<String, String>,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.getOrNull(0) ?: super.onBindParentViewHolder(holder, section, payloads)
        when(payload) {
            PAYLOAD_PARENT_OPEN -> {
                (holder as ParentViewHolder).updateArrow(true)
            }
            PAYLOAD_PARENT_CLOSE -> {
                (holder as ParentViewHolder).updateArrow(false)
            }
            else -> {
                super.onBindParentViewHolder(holder, section, payloads)
            }
        }
    }

    override fun onBindChildViewHolder(
        holder: RecyclerView.ViewHolder,
        section: Section<String, String>,
        childIndex: Int
    ) {
        (holder as ChildViewHolder).bind(section.items[childIndex])
    }

}