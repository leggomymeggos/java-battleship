package com.leggomymeggos.battleship.board

data class Tile(val ship: Ship? = null,
                val hit: Boolean = false)
