package com.theo.openablelist.data

import com.theo.openablelist.Group

data class Category(
    override val parent: String,
    override val children: List<String>
): Group<String, String>