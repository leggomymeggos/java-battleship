package com.leggomymeggos.battleship.board

typealias Grid = List<List<Tile>>
typealias MutableGrid = MutableList<List<Tile>>

fun gridOf(vararg elements: List<Tile> = arrayOf()): Grid = elements.toList()
fun mutableGridOf(vararg elements: List<Tile> = arrayOf()): MutableGrid = elements.toMutableList()
