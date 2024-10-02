package com.theo.openablelist.model

interface Group<P, C> {
    val parent: Parent<P>
    val children: List<Child<C>>
}