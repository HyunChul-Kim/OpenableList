package com.theo.openablelist

import com.theo.openablelist.model.Child
import com.theo.openablelist.model.Group
import com.theo.openablelist.model.OpenableItem
import com.theo.openablelist.model.Parent
import com.theo.openablelist.model.Section

internal class OpenableManager<P, C>(
    groups: List<Group<P, C>>,
    private val isOnlyOneSectionOpen: Boolean = false,
    openAllFirst: Boolean,
    firstOpenIndex: Int = NONE,

    private val onSectionClicked: (OpenableEvent, Int, Int) -> Unit
) {

    private val isOpenAllFirst: Boolean = if(isOnlyOneSectionOpen) {
        false
    } else {
        openAllFirst
    }
    private val sections: List<Section<P, C>> = groups.mapIndexed { index, group ->
        Section(
            parent = group.parent,
            children = group.children,
            isOpened = isOpenAllFirst || firstOpenIndex == index,
        )
    }
    val itemCount: Int get() = sections.fold(0) { total, section ->
        if(section.isOpened) {
            total + section.children.size + 1
        } else {
            total + 1
        }
    }

    private var openedSectionIndex: Int = firstOpenIndex
    private val isExistsOpenedSection: Boolean get() = openedSectionIndex != NONE

    private fun expand(section: Section<P, C>) {
        if(isOnlyOneSectionOpen) {
            if(isExistsOpenedSection) {
                collapse(sections[openedSectionIndex])
            }
            openedSectionIndex = sections.indexOf(section)
        }
        section.isOpened = true
        val position = findAdapterPosition(section)
        onSectionClicked(OpenableEvent.OPEN, position, section.children.size)
    }

    private fun collapse(section: Section<P, C>) {
        if(isOnlyOneSectionOpen) {
            openedSectionIndex = NONE
        }
        section.isOpened = false
        val position = findAdapterPosition(section)
        onSectionClicked(OpenableEvent.CLOSE, position, section.children.size)
    }

    fun toggle(position: Int) {
        val openableItem = findOpenableItem(position)
        if(openableItem.type != OpenableType.PARENT) return
        if(openableItem.section.isOpened) {
            collapse(openableItem.section)
        } else {
            expand(openableItem.section)
        }
    }

    private fun findAdapterPosition(section: Section<P, C>): Int {
        val sectionIndex = sections.indexOf(section)
        var position = 0
        for(i in 0 until sectionIndex) {
            position += getVisibleItemCount(i)
        }
        return position
    }

    fun findOpenableItem(position: Int): OpenableItem<P, C> {
        var offset = position
        sections.forEachIndexed { index, section ->
            val visibleCount = getVisibleItemCount(index)
            if(offset == 0) {
                return OpenableItem(OpenableType.PARENT, section, -1)
            } else if(offset < visibleCount) {
                return OpenableItem(OpenableType.CHILD, section, offset - 1)
            }
            offset -= visibleCount
        }
        throw IllegalArgumentException("Cannot find openable item: Invalid position")
    }

    private fun getVisibleItemCount(sectionIndex: Int): Int {
        return if(sections[sectionIndex].isOpened) {
            sections[sectionIndex].children.size + 1
        } else {
            1
        }
    }


    companion object {
        private const val TAG = "OpenableManager"
        private const val NONE = -1
    }
}