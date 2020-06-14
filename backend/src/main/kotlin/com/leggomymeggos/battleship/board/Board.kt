package com.leggomymeggos.battleship.board

data class Board(
        val grid: Grid = gridOf(),
        val sunkenShips: Set<Ship> = setOf()
)

data class ShipPlacement(
        val shipName: Ship,
        val coordinates: List<Coordinate>
)

data class Coordinate(
        val row: Int,
        val column: Int
)