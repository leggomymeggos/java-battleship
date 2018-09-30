package com.leggomymeggos.battleship.board

typealias Grid = List<List<Tile>>
typealias MutableGrid = MutableList<List<Tile>>

public inline fun grid(): Grid = listOf()
public inline fun mutableGrid(): MutableGrid = mutableListOf()
