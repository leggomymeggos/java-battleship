package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
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
    fun `set winner sets winner on the saved game`() {
        gameRegistry.game = Game(Player(Board()))

        gameRegistry.setWinner()

        assertThat(gameRegistry.game.winner).isTrue()
    }
}