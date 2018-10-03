package com.leggomymeggos.battleship.board

data class Board(val grid: Grid = gridOf(),
                 val sunkenShips: MutableSet<Ship> = mutableSetOf())