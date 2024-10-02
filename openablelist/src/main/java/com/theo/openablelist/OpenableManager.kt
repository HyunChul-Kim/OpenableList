package com.theo.openablelist

class OpenableManager<P, C>(
    groups: List<Group<P, C>>,
    private val isOnlyOneSectionOpen: Boolean = false,
    openAllFirst: Boolean,
    private val firstOpenIndex: Int = NONE,
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
            isOpen = isOpenAllFirst || firstOpenIndex == index,
        )
    }
    val itemCount: Int get() = sections.fold(0) { total, parent ->
        if(parent.isOpen) {
            total + parent.children.size + 1
        } else {
            total + 1
        }
    }

    private var openedSectionIndex: Int = NONE
    private val isExistsOpenedSection: Boolean get() = openedSectionIndex != NONE

    private fun expand(section: Section<P, C>) {
        if(isOnlyOneSectionOpen) {
            if(isExistsOpenedSection) {
                collapse(sections[openedSectionIndex])
            }
            openedSectionIndex = sections.indexOf(section)
        }
        section.isOpen = true
        val realIndex = findRealIndex(section)
        onSectionClicked(OpenableEvent.OPEN, realIndex, section.children.size)
    }

    private fun collapse(section: Section<P, C>) {
        if(isOnlyOneSectionOpen) {
            openedSectionIndex = NONE
        }
        section.isOpen = false
        val realIndex = findRealIndex(section)
        onSectionClicked(OpenableEvent.CLOSE, realIndex, section.children.size)
    }

    fun toggle(position: Int) {
        val openableItem = findOpenableItem(position)
        if(openableItem.type != OpenableType.PARENT) return
        if(openableItem.section.isOpen) {
            collapse(openableItem.section)
        } else {
            expand(openableItem.section)
        }
    }

    private fun findRealIndex(section: Section<P, C>): Int {
        val sectionIndex = sections.indexOf(section)
        var realIndex = 0
        for(i in 0 until sectionIndex) {
            realIndex += getVisibleItemCount(i)
        }
        return realIndex
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
        return if(sections[sectionIndex].isOpen) {
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