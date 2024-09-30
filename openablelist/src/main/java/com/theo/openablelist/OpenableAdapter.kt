package com.theo.openablelist

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 *
 */
abstract class OpenableAdapter<P, C>(
    isOnlyOneSectionOpen: Boolean = false,
    isOpenAllFirst: Boolean = false,
    firstOpenIndex: Int = -1
): RecyclerView.Adapter<ViewHolder>() {

    private val manager = OpenableManager(
        isOnlyOneSectionOpen = isOnlyOneSectionOpen,
        openAllFirst = isOpenAllFirst,
        firstOpenIndex = firstOpenIndex
    )
    private var sections: List<Section<P, C>> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(sectionList: List<Section<P, C>>) {
        sections = sectionList
        notifyDataSetChanged()
    }

    private fun expand(position: Int) {
        val parent = sections.getOrNull(position) ?: return
        expand(parent)
    }

    private fun expand(section: Section<P, C>) {
        if(manager.isOnlyOneSectionOpen) {
            if(manager.existsOpenedSection) {
                collapse(sections[manager.openedSectionIndex])
            }
            manager.openedSectionIndex = sections.indexOf(section)
        }
        section.isOpen = true
        val realIndex = findRealIndex(section)
        notifyItemChanged(realIndex, PAYLOAD_PARENT_OPEN)
        notifyItemRangeInserted(realIndex + 1, section.items.size)
    }

    private fun collapse(position: Int) {
        val parent = sections.getOrNull(position) ?: return
        collapse(parent)
    }

    private fun collapse(section: Section<P, C>) {
        section.isOpen = false
        val realIndex = findRealIndex(section)
        notifyItemChanged(realIndex, PAYLOAD_PARENT_CLOSE)
        notifyItemRangeRemoved(realIndex + 1, section.items.size)
    }

    private fun toggle(position: Int) {
        val openableItem = findOpenableItem(position)
        if(openableItem.type == OpenableType.PARENT) {
            if(openableItem.section.isOpen) {
                collapse(openableItem.section)
            } else {
                expand(openableItem.section)
            }
        }
    }

    private fun findRealIndex(section: Section<P, C>): Int {
        val sectionIndex = sections.indexOf(section)
        var realIndex = 0
        for(i in 0 until sectionIndex) {
            realIndex++
            if(sections[i].isOpen) realIndex += sections[i].items.size
        }
        return realIndex
    }

    private fun findOpenableItem(position: Int): OpenableItem<P, C> {
        var offset = position
        sections.forEachIndexed { index, parent ->
            val visibleCount = getVisibleItemCount(index)
            if(offset == 0) {
                return OpenableItem(OpenableType.PARENT, parent, -1)
            } else if(offset < visibleCount) {
                return OpenableItem(OpenableType.CHILD, parent, offset - 1)
            }
            offset -= visibleCount
        }
        throw IllegalArgumentException("Cannot find openable item: Invalid position")
    }

    private fun getVisibleItemCount(parentIndex: Int): Int {
        return if(sections[parentIndex].isOpen) {
            sections[parentIndex].items.size + 1
        } else {
            1
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            OpenableType.PARENT.value -> onCreateParentViewHolder(parent).apply { itemView.setOnClickListener { toggle(bindingAdapterPosition) } }
            OpenableType.CHILD.value -> onCreateChildViewHolder(parent)
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val openableItem = findOpenableItem(position)
        when(openableItem.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, openableItem.section)
            OpenableType.CHILD -> onBindChildViewHolder(holder, openableItem.section, openableItem.childIndex)
        }
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val openableItem = findOpenableItem(position)
        when(openableItem.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, openableItem.section, payloads)
            OpenableType.CHILD -> onBindChildViewHolder(holder, openableItem.section, openableItem.childIndex, payloads)
        }
    }

    final override fun getItemCount(): Int {
        return sections.fold(0) { total, parent ->
            if(parent.isOpen) {
                total + parent.items.size + 1
            } else {
                total + 1
            }
        }
    }

    final override fun getItemViewType(position: Int): Int {
        return findOpenableItem(position).type.value
    }

    abstract fun onCreateParentViewHolder(parent: ViewGroup): ViewHolder

    abstract fun onCreateChildViewHolder(parent: ViewGroup): ViewHolder

    abstract fun onBindParentViewHolder(holder: ViewHolder, section: Section<P, C>)

    open fun onBindParentViewHolder(holder: ViewHolder, section: Section<P, C>, payloads: MutableList<Any>) {
        onBindParentViewHolder(holder, section)
    }

    abstract fun onBindChildViewHolder(holder: ViewHolder, section: Section<P, C>, childIndex: Int)

    open fun onBindChildViewHolder(holder: ViewHolder, section: Section<P, C>, childIndex: Int, payloads: MutableList<Any>) {
        onBindChildViewHolder(holder, section, childIndex)
    }

    companion object {
        const val PAYLOAD_PARENT_OPEN = 0
        const val PAYLOAD_PARENT_CLOSE = 1
    }
}