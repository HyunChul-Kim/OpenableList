package com.theo.openablelist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


abstract class OpenableAdapter<P, C>(
    groups: List<Group<P, C>>,
    isOnlyOneSectionOpen: Boolean = false,
    isOpenAllFirst: Boolean = false,
    firstOpenIndex: Int = -1
): RecyclerView.Adapter<ViewHolder>() {

    private val manager = OpenableManager(
        groups = groups,
        isOnlyOneSectionOpen = isOnlyOneSectionOpen,
        openAllFirst = isOpenAllFirst,
        firstOpenIndex = firstOpenIndex
    ) { event, index, count ->
        when(event) {
            OpenableEvent.OPEN -> {
                notifyItemChanged(index, PAYLOAD_PARENT_OPEN)
                notifyItemRangeInserted(index + 1, count)
            }
            OpenableEvent.CLOSE -> {
                notifyItemChanged(index, PAYLOAD_PARENT_CLOSE)
                notifyItemRangeRemoved(index + 1, count)
            }
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            OpenableType.PARENT.value -> {
                onCreateParentViewHolder(parent).apply {
                    toggle = { position ->
                        manager.toggle(position)
                    }
                }
            }
            OpenableType.CHILD.value -> onCreateChildViewHolder(parent)
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val openableItem = manager.findOpenableItem(position)
        when(openableItem.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, openableItem.section)
            OpenableType.CHILD -> onBindChildViewHolder(holder, openableItem.section, openableItem.childIndex)
        }
    }

    final override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val openableItem = manager.findOpenableItem(position)
        when(openableItem.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, openableItem.section, payloads)
            OpenableType.CHILD -> onBindChildViewHolder(holder, openableItem.section, openableItem.childIndex, payloads)
        }
    }

    final override fun getItemCount(): Int {
        return manager.itemCount
    }

    final override fun getItemViewType(position: Int): Int {
        return manager.findOpenableItem(position).type.value
    }

    fun getParentItem(position: Int): P {
        val openableItem = manager.findOpenableItem(position)
        return openableItem.section.parent
    }

    fun getChildItem(position: Int): C? {
        val openableItem = manager.findOpenableItem(position)
        if(openableItem.childIndex == -1) return null
        return openableItem.section.children[openableItem.childIndex]
    }

    abstract fun onCreateParentViewHolder(parent: ViewGroup): ParentViewHolder

    abstract fun onCreateChildViewHolder(parent: ViewGroup): ViewHolder

    abstract fun onBindParentViewHolder(holder: ViewHolder, section: Section<P, C>)

    abstract fun onBindChildViewHolder(holder: ViewHolder, section: Section<P, C>, childIndex: Int)

    open fun onBindParentViewHolder(holder: ViewHolder, section: Section<P, C>, payloads: MutableList<Any>) {
        onBindParentViewHolder(holder, section)
    }

    open fun onBindChildViewHolder(holder: ViewHolder, section: Section<P, C>, childIndex: Int, payloads: MutableList<Any>) {
        onBindChildViewHolder(holder, section, childIndex)
    }

    companion object {
        private const val TAG = "OpenableAdapter"
        const val PAYLOAD_PARENT_OPEN = 0
        const val PAYLOAD_PARENT_CLOSE = 1
    }
}