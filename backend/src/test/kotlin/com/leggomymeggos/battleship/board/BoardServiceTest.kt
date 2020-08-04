package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.Orientation.HORIZONTAL
import com.leggomymeggos.battleship.board.Orientation.VERTICAL
import com.leggomymeggos.battleship.board.Ship.*
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BoardServiceTest {

    private val boardRegistry = mock<BoardRegistry> {
        on { getBoard(any()) } doReturn BoardResult.Success(BoardEntity(0, 0, 0, emptySet(), emptySet()))
        on { getBoardForGameAndPlayer(any(), any()) } doReturn BoardResult.Success(BoardEntity(0, 0, 0, emptySet(), emptySet()))
    }
    private val subject = BoardService(
            boardRegistry = boardRegistry
    )

    // region getBoard
    @Test
    fun `getBoard gets the board`() {
        subject.getBoard(123)

        verify(boardRegistry).getBoard(123)
    }

    @Test
    fun `getBoard returns the board when the boardResult is Success`() {
        val entity = BoardEntity(
                id = 1,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )
        whenever(boardRegistry.getBoard(any())).doReturn(BoardResult.Success(entity))

        val response = subject.getBoard(11)

        assertThat(response).isEqualTo(entity)
    }

    @Test
    fun `getBoard throws an exception when boardResult is NoBoardFound`() {
        whenever(boardRegistry.getBoard(any())).doReturn(BoardResult.NoBoardFound)

        try {
            subject.getBoard(33)
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("No board found for id 33")
        }
    }
    // endregion

    // region getBoardIdForPlayerAndGame
    @Test
    fun `getBoardIdForPlayerAndGame delegates to boardRegistry`() {
        subject.getBoardIdForGameAndPlayer(1, 2)

        verify(boardRegistry).getBoardForGameAndPlayer(1, 2)
    }

    @Test
    fun `getBoardIdForPlayerAndGame returns boardId when the boardResult is Success`() {
        val entity = BoardEntity(
                id = 1,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )
        whenever(boardRegistry.getBoardForGameAndPlayer(any(), any())).doReturn(BoardResult.Success(entity))

        val response = subject.getBoardIdForGameAndPlayer(2, 3)
        assertThat(response).isEqualTo(1)
    }

    @Test
    fun `getBoardIdForPlayerAndGame throws an exception when the boardResult is NoBoardFound`() {
        whenever(boardRegistry.getBoardForGameAndPlayer(any(), any())).doReturn(BoardResult.NoBoardFound)

        try {
            subject.getBoardIdForGameAndPlayer(33, 44)
        } catch (e: Exception) {
            assertThat(e.message).isEqualTo("No board found for gameId 33 and playerId 44")
        }
    }
    // endregion

    @Test
    fun `initBoard saves a new board`() {
        subject.initBoard(playerId = 1, gameId = 2)

        argumentCaptor<BoardEntity>().let {
            verify(boardRegistry).register(it.capture())

            val board = it.firstValue
            assertThat(board.gameId).isEqualTo(2)
            assertThat(board.playerId).isEqualTo(1)
            assertThat(board.shipPlacements).isEmpty()
            assertThat(board.hitCoordinates).isEmpty()
        }
    }

    // region placeShip
    @Test
    fun `placeShip gets board from boardRegistry`() {
        subject.placeShip(1, CRUISER, HORIZONTAL, Coordinate(0, 0))

        verify(boardRegistry).getBoard(1)
    }

    @Test
    fun `placeShip saves ship placement to board`() {
        val result = subject.placeShip(1, CRUISER, HORIZONTAL, Coordinate(0, 0))

        verify(boardRegistry).saveShipPlacement(1,
                ShipPlacement(
                        CRUISER,
                        listOf(
                                Coordinate(column = 0, row = 0),
                                Coordinate(column = 1, row = 0),
                                Coordinate(column = 2, row = 0)
                        )
                ))

        assertThat(result).isEqualTo(Placement.Success)
    }

    @Test
    fun `placeShip saves ship placement starting at given coordinate`() {
        val result = subject.placeShip(3, CRUISER, HORIZONTAL, Coordinate(1, 2))

        verify(boardRegistry).saveShipPlacement(3,
                ShipPlacement(
                        CRUISER,
                        listOf(
                                Coordinate(column = 2, row = 1),
                                Coordinate(column = 3, row = 1),
                                Coordinate(column = 4, row = 1)
                        )
                ))

        assertThat(result).isEqualTo(Placement.Success)
    }

    @Test
    fun `placeShip can place ships vertically`() {
        val result = subject.placeShip(3, CRUISER, VERTICAL, Coordinate(1, 2))

        verify(boardRegistry).saveShipPlacement(3,
                ShipPlacement(
                        CRUISER,
                        listOf(
                                Coordinate(column = 2, row = 1),
                                Coordinate(column = 2, row = 2),
                                Coordinate(column = 2, row = 3)
                        )
                ))

        assertThat(result).isEqualTo(Placement.Success)
    }

    @Test
    fun `placeShip when ship would go off the board, uses startingCoordinate as ending coordinate - horizontal placement`() {
        val result = subject.placeShip(3, CRUISER, HORIZONTAL, Coordinate(1, 9))

        verify(boardRegistry).saveShipPlacement(3,
                ShipPlacement(
                        CRUISER,
                        listOf(
                                Coordinate(column = 9, row = 1),
                                Coordinate(column = 8, row = 1),
                                Coordinate(column = 7, row = 1)
                        )
                ))

        assertThat(result).isEqualTo(Placement.Success)
    }

    @Test
    fun `placeShip when ship would go off the board, uses startingCoordinate as ending coordinate - vertical placement`() {
        val result = subject.placeShip(3, CRUISER, VERTICAL, Coordinate(8, 1))

        verify(boardRegistry).saveShipPlacement(3,
                ShipPlacement(
                        CRUISER,
                        listOf(
                                Coordinate(column = 1, row = 8),
                                Coordinate(column = 1, row = 7),
                                Coordinate(column = 1, row = 6)
                        )
                ))

        assertThat(result).isEqualTo(Placement.Success)
    }

    @Test
    fun `placeShip does not add a horizontal ship on top of a vertical ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 2, row = 7),
                                Coordinate(column = 2, row = 6)
                        ))
                ),
                setOf())
        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.placeShip(3, CRUISER, HORIZONTAL, Coordinate(8, 1))

        verify(boardRegistry, never()).saveShipPlacement(any(), any())
        assertThat(result).isEqualTo(Placement.Failure("Invalid ship placement. Please try again."))
    }

    @Test
    fun `placeShip does not add a horizontal ship on top of a horizontal ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 1, row = 8),
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 3, row = 8)
                        ))
                ),
                setOf())
        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.placeShip(3, CRUISER, HORIZONTAL, Coordinate(8, 1))

        verify(boardRegistry, never()).saveShipPlacement(any(), any())
        assertThat(result).isEqualTo(Placement.Failure("Invalid ship placement. Please try again."))
    }

    @Test
    fun `placeShip does not add a vertical ship on top of a horizontal ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 1, row = 8),
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 3, row = 8)
                        ))
                ),
                setOf())
        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.placeShip(3, CRUISER, VERTICAL, Coordinate(6, 3))

        verify(boardRegistry, never()).saveShipPlacement(any(), any())
        assertThat(result).isEqualTo(Placement.Failure("Invalid ship placement. Please try again."))
    }

    @Test
    fun `placeShip does not add a vertical ship on top of a vertical ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 1, row = 8),
                                Coordinate(column = 1, row = 7),
                                Coordinate(column = 1, row = 6)
                        ))
                ),
                setOf())
        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.placeShip(3, CRUISER, VERTICAL, Coordinate(4, 1))

        verify(boardRegistry, never()).saveShipPlacement(any(), any())
        assertThat(result).isEqualTo(Placement.Failure("Invalid ship placement. Please try again."))
    }
    // endregion

    // region hitCoordinate
    @Test
    fun `hitCoordinate gets the registered board`() {
        subject.hitCoordinate(boardId = 1, coordinate = Coordinate(row = 1, column = 2))

        verify(boardRegistry).getBoard(1)
    }

    @Test
    fun `hitCoordinate saves the hit coordinate`() {
        subject.hitCoordinate(boardId = 1, coordinate = Coordinate(row = 1, column = 2))

        verify(boardRegistry).saveHitCoordinates(1, setOf(Coordinate(row = 1, column = 2)))
    }

    @Test
    fun `hitCoordinate adds hit coordinate to board`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(),
                setOf(
                        Coordinate(1, 2),
                        Coordinate(1, 3)
                )
        )

        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))
        subject.hitCoordinate(boardId = 1, coordinate = Coordinate(row = 4, column = 4))

        verify(boardRegistry).saveHitCoordinates(1, setOf(
                Coordinate(row = 1, column = 2),
                Coordinate(row = 1, column = 3),
                Coordinate(row = 4, column = 4)
        ))
    }

    @Test
    fun `hitCoordinate returns Miss when the coordinate does not hit a ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 2, row = 7),
                                Coordinate(column = 2, row = 6)
                        ))
                ),
                setOf())

        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.hitCoordinate(boardId = 1, coordinate = Coordinate(row = 4, column = 4))

        assertThat(result).isEqualTo(HitResult.Miss)
    }

    @Test
    fun `hitCoordinate returns Hit when the coordinate hits a ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 2, row = 7),
                                Coordinate(column = 2, row = 6)
                        ))
                ),
                setOf())

        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.hitCoordinate(boardId = 1, coordinate = Coordinate(column = 2, row = 6))

        assertThat(result).isEqualTo(HitResult.Hit)
    }

    @Test
    fun `hitCoordinate returns ship sunk when the coordinate sinks a ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 2, row = 7),
                                Coordinate(column = 2, row = 6)
                        )),
                        ShipPlacement(BATTLESHIP, listOf(
                                Coordinate(column = 8, row = 4),
                                Coordinate(column = 7, row = 4),
                                Coordinate(column = 6, row = 4),
                                Coordinate(column = 5, row = 4)
                        )),
                        ShipPlacement(DESTROYER, listOf(
                                Coordinate(column = 1, row = 5),
                                Coordinate(column = 1, row = 6)
                        ))
                ),
                setOf(
//                        DESTROYER has one hit and a length of 2
                        Coordinate(column = 1, row = 5),

//                        BATTLESHIP has already been sunk
                        Coordinate(column = 8, row = 4),
                        Coordinate(column = 7, row = 4),
                        Coordinate(column = 6, row = 4),
                        Coordinate(column = 5, row = 4)
                ))

        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.hitCoordinate(boardId = 1, coordinate = Coordinate(column = 1, row = 6))

        assertThat(result).isEqualTo(HitResult.Sunk(DESTROYER))
    }

    @Test
    fun `hitCoordinate returns game over when the coordinate sinks the last ship`() {
        val boardEntity = BoardEntity(
                1, 2, 3,
                setOf(
                        ShipPlacement(SUBMARINE, listOf(
                                Coordinate(column = 2, row = 8),
                                Coordinate(column = 2, row = 7),
                                Coordinate(column = 2, row = 6)
                        )),
                        ShipPlacement(DESTROYER, listOf(
                                Coordinate(column = 1, row = 5),
                                Coordinate(column = 1, row = 6)
                        ))
                ),
                setOf(
                        Coordinate(column = 2, row = 8),
                        Coordinate(column = 2, row = 7),
                        Coordinate(column = 2, row = 6),
                        Coordinate(column = 1, row = 5)
                )
        )

        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(boardEntity))

        val result = subject.hitCoordinate(boardId = 1, coordinate = Coordinate(column = 1, row = 6))

        assertThat(result).isEqualTo(HitResult.GameOver)
    }
    // endregion

    // region placeShipRandomly
    @Test
    fun `placeShipRandomly gets registered board`() {
        subject.placeShipRandomly(12, DESTROYER, VERTICAL)

        verify(boardRegistry, atLeastOnce()).getBoard(12)
    }

    @Test
    fun `placeShipRandomly adds requested ship without errors`() {
        run100Times {
            val result = subject.placeShipRandomly(1, CRUISER, HORIZONTAL)

            argumentCaptor<ShipPlacement>().let { captor ->
                verify(boardRegistry, atMost(100)).saveShipPlacement(eq(1), captor.capture())

                val shipPlacement = captor.firstValue
                assertThat(shipPlacement.shipName).isEqualTo(CRUISER)
                assertThat(shipPlacement.coordinates).hasSize(CRUISER.size)

                val rowCoordinates = shipPlacement.coordinates.map { it.row }
                assertThat(rowCoordinates.count { it == rowCoordinates[0] }).isEqualTo(CRUISER.size)

                val columnCoordinates = shipPlacement.coordinates.map { it.column }.sorted()
                val expectedCoords = mutableListOf<Int>()
                for (i in 0 until CRUISER.size) {
                    expectedCoords.add(columnCoordinates[0] + i)
                }

                assertThat(columnCoordinates).isEqualTo(expectedCoords)
            }

            assertThat(result).isEqualTo(Placement.Success)
        }
    }

    @Test
    fun `placeShipRandomly does not place overlapping ships`() {
        val occupiedCoordinates = listOf(
                Coordinate(row = 4, column = 1),
                Coordinate(row = 5, column = 1),
                Coordinate(row = 6, column = 1),
                Coordinate(row = 7, column = 1),
                Coordinate(row = 8, column = 1)
        )
        whenever(boardRegistry.getBoard(any())).thenReturn(BoardResult.Success(BoardEntity(
                id = 123,
                playerId = 345,
                gameId = 678,
                hitCoordinates = emptySet(),
                shipPlacements = setOf(
                        ShipPlacement(AIRCRAFT_CARRIER, occupiedCoordinates)
                )
        )))

        run100Times {
            subject.placeShipRandomly(1, CRUISER, VERTICAL)

            argumentCaptor<ShipPlacement>().let { captor ->
                verify(boardRegistry, atMost(100)).saveShipPlacement(eq(1), captor.capture())

                val shipPlacement = captor.firstValue
                assertThat(shipPlacement.coordinates).doesNotContainAnyElementsOf(occupiedCoordinates)
            }
        }
    }
    // endregion

    @Test
    fun `randomlySetShips always sets all ships`() {
        run100Times {
            subject.randomlySetShips(12)

            argumentCaptor<ShipPlacement>().let { captor ->
                verify(boardRegistry, times(5)).saveShipPlacement(any(), captor.capture())

                assertThat(captor.allValues.map { it.shipName }).containsExactlyInAnyOrder(
                        CRUISER, AIRCRAFT_CARRIER, DESTROYER, BATTLESHIP, SUBMARINE
                )
            }

            clearInvocations(boardRegistry)
        }
    }

    private fun run100Times(assertions: () -> Unit) {
        for (i in 1..100) assertions()
    }
}