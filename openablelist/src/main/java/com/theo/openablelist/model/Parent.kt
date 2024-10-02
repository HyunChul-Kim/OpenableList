package com.theo.openablelist.model

data class Parent<T>(
    val data: T,
    override var isOpened: Boolean = false,
    override val key: String
): Selection, Openable
