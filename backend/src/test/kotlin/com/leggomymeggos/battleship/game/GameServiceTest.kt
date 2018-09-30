package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameServiceTest {

    private lateinit var playerService: PlayerService
    private lateinit var gameService: GameService

    @Before
    fun setup() {
        playerService = mock()
        gameService = GameService(playerService)

        whenever(playerService.initPlayer()).thenReturn(Player(Board()))
        whenever(playerService.setShips(any())).thenReturn(Player(Board()))
    }

    @Test
    fun `new requests player from playerService`() {
        gameService.new()

        verify(playerService).initPlayer()
    }

    @Test
    fun `new sets ships`() {
        val player = Player(Board())
        whenever(playerService.initPlayer()).thenReturn(player)

        gameService.new()

        verify(playerService).setShips(player)
    }

    @Test
    fun `new returns a game with ships`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.setShips(any())).thenReturn(player)

        val game = gameService.new()
        assertThat(game.player).isEqualTo(player)
    }
}