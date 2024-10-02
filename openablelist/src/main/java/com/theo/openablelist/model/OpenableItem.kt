package com.theo.openablelist.model

import com.theo.openablelist.OpenableType

data class OpenableItem<P, C>(
    val type: OpenableType,
    val section: Section<P, C>,
    val childIndex: Int
)