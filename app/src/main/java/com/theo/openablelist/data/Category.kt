package com.theo.openablelist.data

import com.theo.openablelist.model.Child
import com.theo.openablelist.model.Group
import com.theo.openablelist.model.Parent

data class Category(
    override val parent: Parent<String>,
    override val children: List<Child<String>>
): Group<String, String>