package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.BoardService
import com.leggomymeggos.battleship.board.tile.Tile
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameServiceTest {

    private lateinit var boardService: BoardService
    private lateinit var gameService: GameService

    @Before
    fun setup() {
        boardService = mock()
        gameService = GameService(boardService)
    }

    @Test
    fun `new requests board from boardService`() {
        gameService.new()

        verify(boardService).initBoard()
    }

    @Test
    fun `new gets a board`() {
        gameService.new()

        verify(boardService).initBoard()
    }

    @Test
    fun `new returns a game`() {
        val board = Board(listOf(listOf(Tile())))
        whenever(boardService.initBoard()).thenReturn(board)

        val game = gameService.new()
        assertThat(game.board).isEqualTo(board)
    }
}