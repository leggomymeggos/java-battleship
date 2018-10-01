package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameServiceTest {

    private lateinit var playerService: PlayerService
    private lateinit var gameRegistry: GameRegistry
    private lateinit var gameService: GameService

    @Before
    fun setup() {
        playerService = mock()
        gameRegistry = mock()
        gameService = GameService(playerService, gameRegistry)

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
    fun `new adds game to game registry`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.setShips(any())).thenReturn(player)

        gameService.new()

        val argumentCaptor = argumentCaptor<Game>()
        verify(gameRegistry).game = argumentCaptor.capture()

        assertThat(argumentCaptor.firstValue.player).isEqualTo(player)
    }

    @Test
    fun `new returns a game with ships`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.setShips(any())).thenReturn(player)

        val game = gameService.new()
        assertThat(game.player).isEqualTo(player)
    }
}