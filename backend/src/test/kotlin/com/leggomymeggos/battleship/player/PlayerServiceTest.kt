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
        assertThat(player.id).isNotEqualTo(-1)
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
        val playerWithShips = playerService.setShips(Player(id = 1, board = playerBoard))

        verify(boardService).addShip(same(playerBoard), eq(CRUISER), any(), any())
        verify(boardService).addShip(same(boardWithCruiser), eq(SUBMARINE), any(), any())
        verify(boardService).addShip(same(boardWithSubmarine), eq(AIRCRAFT_CARRIER), any(), any())
        verify(boardService).addShip(same(boardWithAircraftCarrier), eq(DESTROYER), any(), any())
        verify(boardService).addShip(same(boardWithDestroyer), eq(BATTLESHIP), any(), any())

        assertThat(playerWithShips.board).isSameAs(boardWithBattleship)
        assertThat(playerWithShips.id).isEqualTo(1)
    }

    @Test
    fun `hitBoard hits a board`() {
        whenever(boardService.hitTile(any(), any())).thenReturn(Board())

        val board = Board(gridOf(2))
        val coordinate = Coordinate(1, 2)

        playerService.hitBoard(Player(board = board), coordinate)

        verify(boardService).hitTile(board, coordinate)
    }

    @Test
    fun `hitBoard returns player with newly hit board`() {
        val board = Board(gridOf(1))
        whenever(boardService.hitTile(any(), any())).thenReturn(board)

        val result = playerService.hitBoard(Player(id = 3), Coordinate(0, 0))

        assertThat(result.board).isEqualTo(board)
        assertThat(result.id).isEqualTo(3)
    }

    @Test
    fun `isDefeated returns true if all ships are sunk`() {
        val player = Player(board = Board(
                grid = listOf(listOf(
                        Tile(ship = CRUISER),
                        Tile(ship = DESTROYER),
                        Tile(ship = SUBMARINE),
                        Tile(ship = AIRCRAFT_CARRIER),
                        Tile(ship = BATTLESHIP)
                )),
                sunkenShips = mutableSetOf(
                        CRUISER,
                        DESTROYER,
                        SUBMARINE,
                        AIRCRAFT_CARRIER,
                        BATTLESHIP
                )
        ))

        assertThat(playerService.isDefeated(player)).isTrue()
    }

    @Test
    fun `isDefeated returns false if not all ships are sunk`() {
        val player = Player(board = Board(
                grid = listOf(listOf(
                        Tile(ship = CRUISER),
                        Tile(ship = DESTROYER),
                        Tile(ship = SUBMARINE),
                        Tile(ship = AIRCRAFT_CARRIER),
                        Tile(ship = BATTLESHIP)
                )),
                sunkenShips = mutableSetOf(
                        CRUISER,
                        SUBMARINE,
                        AIRCRAFT_CARRIER,
                        BATTLESHIP
                )
        ))

        assertThat(playerService.isDefeated(player)).isFalse()
    }

    @Test
    fun `isDefeated returns true if all ships on the board are sunk`() {
        val player = Player(board = Board(
                grid = listOf(listOf(
                        Tile(ship = AIRCRAFT_CARRIER),
                        Tile(ship = AIRCRAFT_CARRIER),
                        Tile(ship = BATTLESHIP)
                )),
                sunkenShips = mutableSetOf(
                        AIRCRAFT_CARRIER,
                        BATTLESHIP
                )
        ))

        assertThat(playerService.isDefeated(player)).isTrue()
    }
}