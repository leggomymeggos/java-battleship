package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.tile.Tile
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BoardServiceTest {

    lateinit var boardService: BoardService

    @Before
    fun setup() {
        boardService = BoardService()
    }

    @Test
    fun `initBoard returns a fresh board`() {
        val board = boardService.initBoard()

        assertThat(board.grid).containsExactly(
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile())
        )
    }
}