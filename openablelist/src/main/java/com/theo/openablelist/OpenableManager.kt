package com.theo.openablelist

class OpenableManager constructor(
    val isOnlyOneSectionOpen: Boolean = false,
    openAllFirst: Boolean,
    private val firstOpenIndex: Int = NONE
) {

    private val isOpenAllFirst: Boolean = if(isOnlyOneSectionOpen) {
        false
    } else {
        openAllFirst
    }

    private var sectionState: BooleanArray = BooleanArray(0)

    var openedSectionIndex: Int = NONE
    val existsOpenedSection: Boolean get() = openedSectionIndex != NONE

    fun updateData(size: Int) {
        sectionState = BooleanArray(size) {
            isOpenAllFirst || it == firstOpenIndex
        }
    }

    companion object {
        private const val NONE = -1
    }
}