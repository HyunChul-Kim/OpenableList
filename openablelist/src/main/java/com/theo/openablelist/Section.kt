package com.theo.openablelist


data class Section<P, C>(
    val parent: P,
    val children: List<C>,
    var isOpen: Boolean,
)
