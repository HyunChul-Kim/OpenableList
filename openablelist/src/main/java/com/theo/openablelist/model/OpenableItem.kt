package com.theo.openablelist.model

import com.theo.openablelist.OpenableType

data class OpenableItem<P, C>(
    val parent: Parent<P>? = null,
    val child: Child<C>? = null,
    val type: OpenableType,
)
