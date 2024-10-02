package com.theo.openablelist.model

data class Child<T>(
    val data: T,
    override var isSelected: Boolean = false,
    override val key: String
): Selection, Selectable