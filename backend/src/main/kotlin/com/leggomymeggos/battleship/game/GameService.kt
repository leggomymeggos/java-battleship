package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.tile.Tile
import org.springframework.stereotype.Service

@Service
class GameService {
    fun new() : List<List<Tile>> {
        return listOf(
                listOf(Tile(shipId = 1), Tile(shipId = 5), Tile(shipId = 5), Tile(shipId = 5), Tile(), Tile(), Tile(shipId = 3), Tile(), Tile(), Tile()),
                listOf(Tile(shipId = 1), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(shipId = 3), Tile(), Tile(), Tile()),
                listOf(Tile(shipId = 1), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(shipId = 3), Tile(), Tile(), Tile()),
                listOf(Tile(shipId = 1), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(shipId = 1), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(shipId = 4), Tile(shipId = 4), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(shipId = 2), Tile(shipId = 2), Tile(shipId = 2), Tile(shipId = 2))
        )
    }
}