package com.theo.openablelist.model

data class Section<P, C>(
    val parent: Parent<P>,
    val children: List<Child<C>>,
    var isOpened: Boolean = false,
)
