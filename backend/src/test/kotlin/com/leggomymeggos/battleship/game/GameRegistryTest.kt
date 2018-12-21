package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.player.Player
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameRegistryTest {
    lateinit var gameRegistry: GameRegistry

    @Before
    fun setup() {
        gameRegistry = GameRegistry()
    }

    @Test
    fun `setWinner sets winner based on id`() {
        val humanPlayer = Player(id = 123)
        val computerPlayer = Player(id = 456)
        gameRegistry.game = Game(humanPlayer, computerPlayer)

        gameRegistry.setWinner(123)
        assertThat(gameRegistry.game.winner).isEqualTo(humanPlayer)

        gameRegistry.setWinner(456)
        assertThat(gameRegistry.game.winner).isEqualTo(computerPlayer)
    }
    
    @Test
    fun `getPlayer returns player with given id`() {
        val humanPlayer = Player(id = 444)
        val computerPlayer = Player(id = 222)
        gameRegistry.game = Game(humanPlayer, computerPlayer)

        assertThat(gameRegistry.getPlayer(444)).isEqualTo(humanPlayer)
        assertThat(gameRegistry.getPlayer(222)).isEqualTo(computerPlayer)
    }

    @Test
    fun `updatePlayer updates player with id with the provided player`() {
        val humanPlayer = Player(id = 123, board = Board(gridOf(2)))
        val computerPlayer = Player(id = 456)
        gameRegistry.game = Game(humanPlayer = humanPlayer, computerPlayer = computerPlayer)

        val updatedPlayer = Player(id = 123, board = Board(gridOf(1)))
        gameRegistry.updatePlayer(updatedPlayer)

        assertThat(gameRegistry.game.humanPlayer).isEqualTo(updatedPlayer)
    }
}