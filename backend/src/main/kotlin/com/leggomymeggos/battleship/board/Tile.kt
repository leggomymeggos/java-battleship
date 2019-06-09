package com.leggomymeggos.battleship.board

data class Tile(val ship: PlacedShip? = null,
                val hit: Boolean = false)

data class PlacedShip(val name: Ship,
                      val orientation: Orientation
) {
    val length: Int = name.size
}