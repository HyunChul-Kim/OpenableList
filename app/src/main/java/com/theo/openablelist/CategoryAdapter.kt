package com.theo.openablelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theo.openablelist.viewholder.CategoryViewHolder
import com.theo.openablelist.viewholder.SubCategoryViewHolder
import com.theo.openablelistdemo.databinding.ViewCategoryChildItemBinding
import com.theo.openablelistdemo.databinding.ViewCategoryParentItemBinding

class CategoryAdapter(
    categories: List<Group<String, String>>,
    private val onClickParent: (String) -> Unit,
    private val onClickChild: (String) -> Unit
): OpenableAdapter<String, String>(
    groups = categories,
    isOnlyOneSectionOpen = true
) {

    override fun onCreateParentViewHolder(parent: ViewGroup): CategoryViewHolder {
        return CategoryViewHolder(
            binding = ViewCategoryParentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClickParent
        )
    }

    override fun onCreateChildViewHolder(parent: ViewGroup): SubCategoryViewHolder {
        return SubCategoryViewHolder(
            binding = ViewCategoryChildItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClickChild
        )
    }

    override fun onBindParentViewHolder(
        holder: RecyclerView.ViewHolder,
        section: Section<String, String>
    ) {
        (holder as CategoryViewHolder).bind(section.parent)
    }

    override fun onBindParentViewHolder(
        holder: RecyclerView.ViewHolder,
        section: Section<String, String>,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.getOrNull(0) ?: super.onBindParentViewHolder(holder, section, payloads)
        when(payload) {
            PAYLOAD_PARENT_OPEN -> {
                (holder as CategoryViewHolder).updateArrow(true)
            }
            PAYLOAD_PARENT_CLOSE -> {
                (holder as CategoryViewHolder).updateArrow(false)
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
        (holder as SubCategoryViewHolder).bind(section.children[childIndex])
    }

}