package com.theo.openablelist.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class OpenableViewHolder(
    itemView: View
): ViewHolder(itemView) {

    internal var internalOnClick: ((Int) -> Unit)? = null

    init {
        itemView.setOnClickListener {
            with(bindingAdapterPosition) {
                internalOnClick?.invoke(this)
                onClick(this)
            }
        }
    }

    abstract fun onClick(position: Int)
}