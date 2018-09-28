package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.tile.Tile
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameServiceTest {

    lateinit var gameService: GameService

    @Before
    fun setup() {
        gameService = GameService()
    }

    @Test
    fun `new returns a board`() {
        val board = gameService.new()


        assertThat(board).containsExactly(
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