package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.*
import com.leggomymeggos.battleship.board.Ship.*
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PlayerServiceTest {
    lateinit var boardService: BoardService
    lateinit var playerRegistry: PlayerRegistry
    lateinit var playerService: PlayerService

    @Before
    fun setup() {
        boardService = mock()
        playerRegistry = mock()

        playerService = PlayerService(boardService, playerRegistry)

        whenever(boardService.initBoard()).thenReturn(Board())
        whenever(boardService.addShip(any(), any(), any(), any())).thenReturn(Board())
        whenever(boardService.addShipRandomly(any(), any(), any())).thenReturn(Board())
        whenever(boardService.hitTile(any(), any())).thenReturn(BoardHitResponse(HitResult.Miss(), Board()))

        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(-1, -1, Board()))
    }

    // region initPlayer
    @Test
    fun `initPlayer gets new board`() {
        playerService.initPlayer(123)

        verify(boardService).initBoard()
    }

    @Test
    fun `initPlayer saves the player`() {
        val board = Board(gridOf(2))
        whenever(boardService.initBoard()).thenReturn(board)

        playerService.initPlayer(123)

        val captor = argumentCaptor<Player>()
        verify(playerRegistry).register(eq(123), captor.capture())

        val player = captor.firstValue
        assertThat(player.board).isEqualTo(board)
        assertThat(player.id).isNotEqualTo(-1)
    }

    @Test
    fun `initPlayer returns new player with board`() {
        val board = Board(gridOf(2))
        whenever(boardService.initBoard()).thenReturn(board)

        val player = playerService.initPlayer(32)

        assertThat(player.board).isEqualTo(board)
        assertThat(player.id).isNotEqualTo(-1)

        val captor = argumentCaptor<Player>()
        verify(playerRegistry).register(any(), captor.capture())
        assertThat(player).isSameAs(captor.firstValue)
    }
    // endregion

    // region setShips
    @Test
    fun `setShips gets the player`() {
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(0, 0, Board()))
        playerService.setShips(123, 456)

        verify(playerRegistry).getPlayer(123, 456)
    }

    @Test
    fun `setShips adds all ships`() {
        val playerBoard = Board()
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(-1, -1, playerBoard))

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

        playerService.setShips(1, 2)

        verify(boardService).addShip(same(playerBoard), eq(CRUISER), any(), any())
        verify(boardService).addShip(same(boardWithCruiser), eq(SUBMARINE), any(), any())
        verify(boardService).addShip(same(boardWithSubmarine), eq(AIRCRAFT_CARRIER), any(), any())
        verify(boardService).addShip(same(boardWithAircraftCarrier), eq(DESTROYER), any(), any())
        verify(boardService).addShip(same(boardWithDestroyer), eq(BATTLESHIP), any(), any())
    }

    @Test
    fun `setShips updates player after adding all ships`() {
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

        val playerBoard = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(
                id = 123,
                gameId = 456,
                board = playerBoard
        ))

        playerService.setShips(456, 123)

        val captor = argumentCaptor<Player>()
        verify(playerRegistry).updatePlayer(eq(456), captor.capture())

        assertThat(captor.firstValue).isEqualTo(Player(
                id = 123,
                board = boardWithBattleship
        ))
    }

    @Test
    fun `setShips returns player`() {
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

        val playerBoard = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(
                id = 123,
                gameId = 456,
                board = playerBoard
        ))

        val player = playerService.setShips(456, 123)

        assertThat(player).isEqualTo(Player(
                id = 123,
                board = boardWithBattleship
        ))
    }
    // endregion

    // region randomlySetShips
    @Test
    fun `randomlySetShips gets the player`() {
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(0, 0, Board()))
        playerService.randomlySetShips(123, 456)

        verify(playerRegistry).getPlayer(123, 456)
    }

    @Test
    fun `randomlySetShips adds all ships randomly`() {
        val boardWithCruiser = Board()
        val boardWithSubmarine = Board()
        val boardWithAircraftCarrier = Board()
        val boardWithDestroyer = Board()
        val boardWithBattleship = Board()

        whenever(boardService.addShipRandomly(any(), eq(CRUISER), any())).thenReturn(boardWithCruiser)
        whenever(boardService.addShipRandomly(any(), eq(SUBMARINE), any())).thenReturn(boardWithSubmarine)
        whenever(boardService.addShipRandomly(any(), eq(AIRCRAFT_CARRIER), any())).thenReturn(boardWithAircraftCarrier)
        whenever(boardService.addShipRandomly(any(), eq(DESTROYER), any())).thenReturn(boardWithDestroyer)
        whenever(boardService.addShipRandomly(any(), eq(BATTLESHIP), any())).thenReturn(boardWithBattleship)

        val playerBoard = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(
                id = 123,
                gameId = 456,
                board = playerBoard
        ))

        playerService.randomlySetShips(456, 123)

        verify(boardService).addShipRandomly(same(playerBoard), eq(CRUISER), any())
        verify(boardService).addShipRandomly(same(boardWithCruiser), eq(SUBMARINE), any())
        verify(boardService).addShipRandomly(same(boardWithSubmarine), eq(AIRCRAFT_CARRIER), any())
        verify(boardService).addShipRandomly(same(boardWithAircraftCarrier), eq(DESTROYER), any())
        verify(boardService).addShipRandomly(same(boardWithDestroyer), eq(BATTLESHIP), any())
    }

    @Test
    fun `randomlySetShips updates player after adding all ships`() {
        val boardWithCruiser = Board()
        val boardWithSubmarine = Board()
        val boardWithAircraftCarrier = Board()
        val boardWithDestroyer = Board()
        val boardWithBattleship = Board()

        whenever(boardService.addShipRandomly(any(), eq(CRUISER), any())).thenReturn(boardWithCruiser)
        whenever(boardService.addShipRandomly(any(), eq(SUBMARINE), any())).thenReturn(boardWithSubmarine)
        whenever(boardService.addShipRandomly(any(), eq(AIRCRAFT_CARRIER), any())).thenReturn(boardWithAircraftCarrier)
        whenever(boardService.addShipRandomly(any(), eq(DESTROYER), any())).thenReturn(boardWithDestroyer)
        whenever(boardService.addShipRandomly(any(), eq(BATTLESHIP), any())).thenReturn(boardWithBattleship)

        val playerBoard = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(
                id = 123,
                gameId = 456,
                board = playerBoard
        ))

        playerService.randomlySetShips(456, 123)

        val captor = argumentCaptor<Player>()
        verify(playerRegistry).updatePlayer(eq(456), captor.capture())

        assertThat(captor.firstValue).isEqualTo(Player(
                id = 123,
                board = boardWithBattleship
        ))
    }

    @Test
    fun `randomlySetShips returns player`() {
        val boardWithCruiser = Board()
        val boardWithSubmarine = Board()
        val boardWithAircraftCarrier = Board()
        val boardWithDestroyer = Board()
        val boardWithBattleship = Board()

        whenever(boardService.addShipRandomly(any(), eq(CRUISER), any())).thenReturn(boardWithCruiser)
        whenever(boardService.addShipRandomly(any(), eq(SUBMARINE), any())).thenReturn(boardWithSubmarine)
        whenever(boardService.addShipRandomly(any(), eq(AIRCRAFT_CARRIER), any())).thenReturn(boardWithAircraftCarrier)
        whenever(boardService.addShipRandomly(any(), eq(DESTROYER), any())).thenReturn(boardWithDestroyer)
        whenever(boardService.addShipRandomly(any(), eq(BATTLESHIP), any())).thenReturn(boardWithBattleship)

        val playerBoard = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(PlayerEntity(
                id = 123,
                gameId = 456,
                board = playerBoard
        ))

        val player = playerService.randomlySetShips(456, 123)

        assertThat(player).isEqualTo(Player(
                id = 123,
                board = boardWithBattleship
        ))
    }
    // endregion

    // region hitBoard
    @Test
    fun `hitBoard gets the player`() {
        playerService.hitBoard(123, 432, Coordinate(0, 0))

        verify(playerRegistry).getPlayer(123, 432)
    }

    @Test
    fun `hitBoard hits a board`() {
        val board = Board(gridOf(2))
        whenever(playerRegistry.getPlayer(any(), any()))
                .thenReturn(PlayerEntity(123, -1, board))
        whenever(boardService.hitTile(any(), any())).thenReturn(BoardHitResponse(HitResult.Miss(), Board()))

        val coordinate = Coordinate(1, 2)

        playerService.hitBoard(-1, -1, coordinate)

        verify(boardService).hitTile(board, coordinate)
    }

    @Test
    fun `hitBoard updates the player`() {
        whenever(playerRegistry.getPlayer(any(), any()))
                .thenReturn(PlayerEntity(123, -1, Board()))
        val hitBoard = Board(gridOf(3))
        whenever(boardService.hitTile(any(), any())).thenReturn(BoardHitResponse(HitResult.Miss(), hitBoard))

        val coordinate = Coordinate(1, 2)

        playerService.hitBoard(456, 123, coordinate)

        val captor = argumentCaptor<Player>()
        verify(playerRegistry).updatePlayer(eq(456), captor.capture())
        assertThat(captor.firstValue).isEqualTo(Player(id = 123, board = hitBoard))
    }

    @Test
    fun `hitBoard returns hit board response`() {
        whenever(playerRegistry.getPlayer(any(), any()))
                .thenReturn(PlayerEntity(3, -1, Board()))
        val board = Board(gridOf(1))
        val response = BoardHitResponse(HitResult.Sunk(Ship.BATTLESHIP), board)
        whenever(boardService.hitTile(any(), any())).thenReturn(response)

        val result = playerService.hitBoard(3, 2, Coordinate(0, 0))

        assertThat(result).isEqualTo(response)
    }
    // endregion

    // region isDefeated
    @Test
    fun `isDefeated gets the player`() {
        playerService.isDefeated(1, 2)

        verify(playerRegistry).getPlayer(1, 2)
    }

    @Test
    fun `isDefeated returns true if all ships are sunk`() {
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(
                PlayerEntity(id = 1, gameId = 1, board = Board(
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
        )

        assertThat(playerService.isDefeated(0, 0)).isTrue()
    }

    @Test
    fun `isDefeated returns false if not all ships are sunk`() {
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(
                PlayerEntity(id = 1, gameId = 1, board = Board(
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
        )

        assertThat(playerService.isDefeated(0, 0)).isFalse()
    }
    // endregion

    // region getPlayer
    @Test
    fun `getPlayer gets player from registry`() {
        playerService.getPlayer(2, 3)

        verify(playerRegistry).getPlayer(2, 3)
    }

    @Test
    fun `getPlayer returns player`() {
        val board = Board(gridOf(3))
        val player = PlayerEntity(id = 123, gameId = 0, board = board)
        whenever(playerRegistry.getPlayer(any(), any())).thenReturn(player)

        val result = playerService.getPlayer(0, 0)

        assertThat(result).isEqualTo(Player(id = 123, board = board))
    }
    // endregion
}