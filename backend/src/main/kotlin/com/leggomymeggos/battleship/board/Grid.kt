package com.leggomymeggos.battleship.board

typealias Grid = List<List<Tile>>

fun gridOf(numberOfElements: Int = 0): Grid {
    if(numberOfElements <= 0) {
        return emptyList()
    }

    val grid = mutableListOf<List<Tile>>()

    for (rowIndex in 0 until numberOfElements) {
        val row = mutableListOf<Tile>()
        for (colIndex in 0 until numberOfElements) {
            row.add(Tile())
        }
        grid.add(row)
    }

    return grid
}
