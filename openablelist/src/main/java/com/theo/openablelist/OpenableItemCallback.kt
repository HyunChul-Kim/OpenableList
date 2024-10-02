package com.theo.openablelist

import androidx.recyclerview.widget.DiffUtil
import com.theo.openablelist.model.OpenableItem2

class OpenableItemCallback<P, C>: DiffUtil.ItemCallback<OpenableItem2<P, C>>() {
    override fun areItemsTheSame(oldItem: OpenableItem2<P, C>, newItem: OpenableItem2<P, C>): Boolean {
        if(oldItem.type == newItem.type) {
            return when(oldItem.type) {
                OpenableType.PARENT -> {
                    oldItem.parent?.key == newItem.parent?.key
                }

                OpenableType.CHILD -> {
                    oldItem.child?.key == newItem.child?.key
                }
            }
        }
        return false
    }

    override fun areContentsTheSame(oldItem: OpenableItem2<P, C>, newItem: OpenableItem2<P, C>): Boolean {
        if(oldItem.type == newItem.type) {
            return when(oldItem.type) {
                OpenableType.PARENT -> {
                    oldItem.parent == newItem.parent
                }

                OpenableType.CHILD -> {
                    oldItem.child == newItem.child
                }
            }
        }
        return false
    }

    override fun getChangePayload(
        oldItem: OpenableItem2<P, C>,
        newItem: OpenableItem2<P, C>
    ): Any? {
        if(oldItem.type == newItem.type) {
            return when(oldItem.type) {
                OpenableType.PARENT -> {
                    if(oldItem.parent?.isOpened == newItem.parent?.isOpened) {
                        super.getChangePayload(oldItem, newItem)
                    } else {
                        PAYLOAD_SELECT_CHANGED
                    }
                }
                OpenableType.CHILD -> {
                    if(oldItem.child?.isSelected == newItem.child?.isSelected) {
                        super.getChangePayload(oldItem, newItem)
                    } else {
                        PAYLOAD_SELECT_CHANGED
                    }
                }
            }
        }
        return super.getChangePayload(oldItem, newItem)
    }

    companion object {
        const val PAYLOAD_SELECT_CHANGED = 0
    }
}