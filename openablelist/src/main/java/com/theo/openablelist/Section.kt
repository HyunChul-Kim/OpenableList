package com.theo.openablelist

data class Section<out P, out C>(
    val data: P,
    var isOpen: Boolean,
    val items: List<C>
)
