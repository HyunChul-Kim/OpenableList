package com.theo.openablelist

interface Group<P, C> {
    val parent: P
    val children: List<C>
}