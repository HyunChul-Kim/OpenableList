package com.theo.openablelist

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

open class ParentViewHolder(
    itemView: View
): ViewHolder(itemView) {

    internal var toggle: ((Int) -> Unit)? = null

    init {
        itemView.setOnClickListener {
            toggle?.invoke(bindingAdapterPosition)
            onClick()
        }
    }

    open fun onClick() {

    }
}