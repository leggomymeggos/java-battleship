package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
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
    }

    @Test
    fun `new requests board from boardService`() {
        gameService.new()

        verify(playerService).initPlayer()
    }

    @Test
    fun `new gets a board`() {
        gameService.new()

        verify(playerService).initPlayer()
    }

    @Test
    fun `new returns a game`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.initPlayer()).thenReturn(player)

        val game = gameService.new()
        assertThat(game.player).isEqualTo(player)
    }
}