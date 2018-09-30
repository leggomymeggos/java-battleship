package com.leggomymeggos.battleship.player

import com.leggomymeggos.battleship.board.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PlayerServiceTest {
    lateinit var boardService: BoardService
    lateinit var playerService: PlayerService

    @Before
    fun setup() {
        boardService = mock()
        playerService = PlayerService(boardService)

        whenever(boardService.initBoard()).thenReturn(Board())
    }

    @Test
    fun `initPlayer gets new board`() {
        playerService.initPlayer()

        verify(boardService).initBoard()
    }

    @Test
    fun `initPlayer returns new player with board`() {
        val board = Board(gridOf(listOf(Tile()), listOf(Tile())))
        whenever(boardService.initBoard()).thenReturn(board)

        val player = playerService.initPlayer()

        assertThat(player.board).isEqualTo(board)
    }
}