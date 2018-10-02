package com.leggomymeggos.battleship.player

import com.leggomymeggos.battleship.board.*
import com.leggomymeggos.battleship.board.Ship.*
import com.nhaarman.mockito_kotlin.*
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
        val board = Board(gridOf(2))
        whenever(boardService.initBoard()).thenReturn(board)

        val player = playerService.initPlayer()

        assertThat(player.board).isEqualTo(board)
    }

    @Test
    fun `setShips sets all ships`() {
        val boardWithCruiser = Board()
        val boardWithSubmarine = Board()
        val boardWithAircraftCarrier = Board()
        val boardWithDestroyer = Board()
        val boardWithBattleship = Board()

        whenever(boardService.addShip(any(), eq(CRUISER), any(), any())).thenReturn(boardWithCruiser)
        whenever(boardService.addShip(any(), eq(SUBMARINE), any(), any())).thenReturn(boardWithSubmarine)
        whenever(boardService.addShip(any(), eq(AIRCRAFT_CARRIER), any(), any())).thenReturn(boardWithAircraftCarrier)
        whenever(boardService.addShip(any(), eq(DESTROYER), any(), any())).thenReturn(boardWithDestroyer)
        whenever(boardService.addShip(any(), eq(BATTLESHIP), any(), any())).thenReturn(boardWithBattleship)

        val playerBoard = Board()
        val playerWithShips = playerService.setShips(Player(playerBoard))

        verify(boardService).addShip(same(playerBoard), eq(CRUISER), any(), any())
        verify(boardService).addShip(same(boardWithCruiser), eq(SUBMARINE), any(), any())
        verify(boardService).addShip(same(boardWithSubmarine), eq(AIRCRAFT_CARRIER), any(), any())
        verify(boardService).addShip(same(boardWithAircraftCarrier), eq(DESTROYER), any(), any())
        verify(boardService).addShip(same(boardWithDestroyer), eq(BATTLESHIP), any(), any())

        assertThat(playerWithShips.board).isSameAs(boardWithBattleship)
    }

    @Test
    fun `hitBoard hits a board`() {
        val board = Board(gridOf(2))
        val coordinate = Coordinate(1, 2)

        playerService.hitBoard(board, coordinate)

        verify(boardService).hitTile(board, coordinate)
    }

    @Test
    fun `hitBoard returns newly hit board`() {
        val board = Board(gridOf(1))
        whenever(boardService.hitTile(any(), any())).thenReturn(board)

        val result = playerService.hitBoard(Board(), Coordinate(0, 0))

        assertThat(result).isEqualTo(board)
    }
}