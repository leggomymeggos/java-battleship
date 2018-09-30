package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.tile.Tile

typealias Grid = List<List<Tile>>
typealias MutableGrid = MutableList<List<Tile>>

public inline fun grid(): Grid = listOf()
public inline fun mutableGrid(): MutableGrid = mutableListOf()
