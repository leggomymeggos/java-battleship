package com.leggomymeggos.battleship.board.tile

import com.leggomymeggos.battleship.board.Ship

data class Tile(val ship: Ship? = null,
                val hit: Boolean = false)
