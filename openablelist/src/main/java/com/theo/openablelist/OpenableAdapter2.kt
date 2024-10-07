package com.theo.openablelist

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theo.openablelist.model.Child
import com.theo.openablelist.viewholder.OpenableViewHolder
import com.theo.openablelist.model.OpenableItem2
import com.theo.openablelist.model.Parent
import com.theo.openablelist.model.Section

abstract class OpenableAdapter2<P, C>(
    private val sectionList: List<Section<P, C>>,
    private val isOnlyOneSelectParent: Boolean = true,
    private val isOnlyOneSelectChild: Boolean = true
): ListAdapter<OpenableItem2<P, C>, OpenableViewHolder>(OpenableItemCallback()) {

    private var sections = sectionList.flatMap {
        listOf(
            OpenableItem2<P, C>(
                parent = it.parent,
                type = OpenableType.PARENT
            )
        ) + if(it.isOpened) {
            it.children.map { child ->
                OpenableItem2<P, C>(
                    child = child,
                    type = OpenableType.CHILD
                )
            }
        } else {
            emptyList()
        }
    }
    private var selectedParentKey: String = ""
    private val selectedChildKeys: MutableList<String> = mutableListOf()

    fun update() {
        submitList(sections)
    }

    private fun updateSelections(item: OpenableItem2<P, C>) {
        sections = when(item.type) {
            OpenableType.PARENT -> {
                sectionList.flatMap {
                    it.parent.isOpened = if(it.parent.key == item.parent?.key) {
                        !it.parent.isOpened
                    } else if(isOnlyOneSelectChild) {
                        false
                    } else {
                        it.parent.isOpened
                    }
                    listOf(
                        OpenableItem2<P, C>(
                            parent = it.parent,
                            type = OpenableType.PARENT
                        )
                    ) + if(it.parent.isOpened) {
                        it.children.map { child ->
                            OpenableItem2<P, C>(
                                child = child.copy(
                                    isSelected = isChildSelected(child.key)
                                ),
                                type = OpenableType.CHILD
                            )
                        }
                    } else {
                        emptyList()
                    }
                }
            }
            OpenableType.CHILD -> {
                sectionList.flatMap {
                    listOf(
                        OpenableItem2<P, C>(
                            parent = it.parent,
                            type = OpenableType.PARENT
                        )
                    ) + if(it.parent.isOpened) {
                        it.children.map { child ->
                            OpenableItem2<P, C>(
                                child = child.copy(
                                    isSelected = isChildSelected(child.key, item.child?.key)
                                ),
                                type = OpenableType.CHILD
                            )
                        }
                    } else {
                        emptyList()
                    }
                }
            }
        }
        update()
    }

    private fun isChildSelected(key: String) = selectedChildKeys.contains(key)

    private fun isChildSelected(key: String, selectedKey: String?): Boolean {
        return if(key == selectedKey) {
            if(selectedChildKeys.contains(key)) {
                selectedChildKeys.remove(key)
                false
            } else {
                if(isOnlyOneSelectChild) {
                    selectedChildKeys.clear()
                }
                selectedChildKeys.add(key)
                true
            }
        } else {
            if(isOnlyOneSelectChild) {
                selectedChildKeys.remove(key)
                false
            } else {
                selectedChildKeys.contains(key)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpenableViewHolder {
        return when(viewType) {
            OpenableType.PARENT.value -> {
                onCreateParentViewHolder(parent).apply {
                    internalOnClick = { position ->
                        selectedParentKey = sections[position].parent?.key ?: ""
                        updateSelections(sections[position])
                    }
                }
            }
            OpenableType.CHILD.value -> {
                onCreateChildViewHolder(parent).apply {
                    internalOnClick = { position ->
                        updateSelections(sections[position])
                    }
                }
            }
            else -> throw IllegalArgumentException("Undefined view type")
        }
    }

    override fun onBindViewHolder(
        holder: OpenableViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = sections[position]
        when(item.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, item.parent!!, payloads)
            OpenableType.CHILD -> onBindChildViewHolder(holder, item.child!!, payloads)
        }
    }

    override fun onBindViewHolder(holder: OpenableViewHolder, position: Int) {
        val item = sections[position]
        when(item.type) {
            OpenableType.PARENT -> onBindParentViewHolder(holder, item.parent!!)
            OpenableType.CHILD -> onBindChildViewHolder(holder, item.child!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return sections[position].type.value
    }

    abstract fun onCreateParentViewHolder(parent: ViewGroup): OpenableViewHolder

    abstract fun onCreateChildViewHolder(parent: ViewGroup): OpenableViewHolder

    abstract fun onBindParentViewHolder(holder: ViewHolder, item: Parent<P>)

    open fun onBindParentViewHolder(holder: ViewHolder, item: Parent<P>, payloads: MutableList<Any>) {
        onBindParentViewHolder(holder, item)
    }

    abstract fun onBindChildViewHolder(holder: ViewHolder, item: Child<C>)

    open fun onBindChildViewHolder(holder: ViewHolder, item: Child<C>, payloads: MutableList<Any>) {
        onBindChildViewHolder(holder, item)
    }

}