package com.theo.openablelist

data class OpenableItem<P, C>(
    val type: OpenableType,
    val section: Section<P, C>,
    val childIndex: Int
)