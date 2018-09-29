package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.tile.Tile
import org.springframework.stereotype.Service

@Service
class BoardService {

    fun initBoard(): Board {
        val tiles = mutableListOf<MutableList<Tile>>()

        for (row in 0..9) {
            val innerTiles = mutableListOf<Tile>()
            for (col in 0..9) {
                innerTiles.add(Tile())
            }
            tiles.add(innerTiles)
        }

        return Board(tiles)
    }
}