package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.gridOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PlayerRegistryTest {
    private lateinit var playerRegistry: PlayerRegistry

    @Before
    fun setup() {
        playerRegistry = PlayerRegistry()
    }

    @Test
    fun `register keeps track of a player`() {
        val board = Board(gridOf(3))
        val player = Player(456, board)
        playerRegistry.register(123, player)

        assertThat(playerRegistry.getPlayer(123, 456)).isEqualTo(
                PlayerEntity(id = 456, board = board, gameId = 123)
        )
    }

    @Test
    fun `updatePlayer updates player`() {
        val board = Board(gridOf(3))
        val player = Player(456, board)
        playerRegistry.register(123, player)

        playerRegistry.updatePlayer(123, Player(456, Board(gridOf(4))))

        assertThat(playerRegistry.getPlayer(123, 456)).isEqualTo(
                PlayerEntity(id = 456, board = Board(gridOf(4)), gameId = 123)
        )
    }
}