package com.leggomymeggos.battleship.board

import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BoardPresenterTest {

    private val boardService = mock<BoardService> {
        on { getBoard(any()) } doReturn BoardEntity(0, 0, 0, setOf(), setOf())
    }
    private val subject = BoardPresenter(boardService)

    @Test
    fun `presentBoard gets board from board service`() {
        subject.presentBoard(382)

        verify(boardService).getBoard(382)
    }

    @Test
    fun `presentBoard returns fullBoardInfo with ships placed`() {
        whenever(boardService.getBoard(any())).doReturn(BoardEntity(
                id = 0,
                playerId = 0,
                gameId = 0,
                shipPlacements = setOf(
                        ShipPlacement(shipName = Ship.BATTLESHIP, coordinates = listOf(
                                Coordinate(row = 1, column = 5),
                                Coordinate(row = 1, column = 6),
                                Coordinate(row = 1, column = 7),
                                Coordinate(row = 1, column = 8)
                        )),
                        ShipPlacement(shipName = Ship.CRUISER, coordinates = listOf(
                                Coordinate(row = 0, column = 0),
                                Coordinate(row = 1, column = 0),
                                Coordinate(row = 2, column = 0)
                        )),
                        ShipPlacement(shipName = Ship.DESTROYER, coordinates = listOf(
                                Coordinate(row = 9, column = 9),
                                Coordinate(row = 9, column = 8)
                        ))
                ),
                hitCoordinates = setOf()
        ))

        val board = subject.presentBoard(123)

        assertThat(board.grid).containsExactly(
                listOf(Tile(ship = Ship.CRUISER), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(ship = Ship.CRUISER), Tile(), Tile(), Tile(), Tile(), Tile(ship = Ship.BATTLESHIP), Tile(ship = Ship.BATTLESHIP), Tile(ship = Ship.BATTLESHIP), Tile(ship = Ship.BATTLESHIP), Tile()),
                listOf(Tile(ship = Ship.CRUISER), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(ship = Ship.DESTROYER), Tile(ship = Ship.DESTROYER))
        )
    }

    @Test
    fun `presentBoard maps hit coordinates`() {
        whenever(boardService.getBoard(any())).doReturn(BoardEntity(
                id = 0,
                playerId = 0,
                gameId = 0,
                shipPlacements = setOf(),
                hitCoordinates = setOf(
                        Coordinate(row = 0, column = 0),
                        Coordinate(row = 1, column = 6),
                        Coordinate(row = 8, column = 2),
                        Coordinate(row = 9, column = 8)
                )
        ))

        val board = subject.presentBoard(123)

        assertThat(board.grid).containsExactly(
                listOf(Tile(hit = true), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(hit = true), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(hit = true), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(hit = true), Tile())
        )
    }

    @Test
    fun `presentBoard maps hit coordinates with ships`() {
        whenever(boardService.getBoard(any())).doReturn(BoardEntity(
                id = 0,
                playerId = 0,
                gameId = 0,
                shipPlacements = setOf(
                        ShipPlacement(shipName = Ship.BATTLESHIP, coordinates = listOf(
                                Coordinate(row = 1, column = 5),
                                Coordinate(row = 1, column = 6),
                                Coordinate(row = 1, column = 7),
                                Coordinate(row = 1, column = 8)
                        )),
                        ShipPlacement(shipName = Ship.CRUISER, coordinates = listOf(
                                Coordinate(row = 0, column = 0),
                                Coordinate(row = 1, column = 0),
                                Coordinate(row = 2, column = 0)
                        )),
                        ShipPlacement(shipName = Ship.DESTROYER, coordinates = listOf(
                                Coordinate(row = 9, column = 9),
                                Coordinate(row = 9, column = 8)
                        ))
                ),
                hitCoordinates = setOf(
                        Coordinate(row = 0, column = 0),
                        Coordinate(row = 1, column = 6),
                        Coordinate(row = 7, column = 2),
                        Coordinate(row = 9, column = 8)
                )
        ))

        val board = subject.presentBoard(123)

        assertThat(board.grid).containsExactly(
                listOf(Tile(ship = Ship.CRUISER, hit = true), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(ship = Ship.CRUISER), Tile(), Tile(), Tile(), Tile(), Tile(ship = Ship.BATTLESHIP), Tile(ship = Ship.BATTLESHIP, hit = true), Tile(ship = Ship.BATTLESHIP), Tile(ship = Ship.BATTLESHIP), Tile()),
                listOf(Tile(ship = Ship.CRUISER), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(hit = true), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(ship = Ship.DESTROYER, hit = true), Tile(ship = Ship.DESTROYER))
        )
    }

    @Test
    fun `presentBoard identifies sunken ships`() {
        whenever(boardService.getBoard(any())).doReturn(BoardEntity(
                id = 0,
                playerId = 0,
                gameId = 0,
                shipPlacements = setOf(
                        ShipPlacement(shipName = Ship.BATTLESHIP, coordinates = listOf(
                                Coordinate(row = 1, column = 5),
                                Coordinate(row = 1, column = 6),
                                Coordinate(row = 1, column = 7),
                                Coordinate(row = 1, column = 8)
                        )),
                        ShipPlacement(shipName = Ship.CRUISER, coordinates = listOf(
                                Coordinate(row = 0, column = 0),
                                Coordinate(row = 1, column = 0),
                                Coordinate(row = 2, column = 0)
                        )),
                        ShipPlacement(shipName = Ship.DESTROYER, coordinates = listOf(
                                Coordinate(row = 9, column = 9),
                                Coordinate(row = 9, column = 8)
                        ))
                ),
                hitCoordinates = setOf(
                        Coordinate(row = 0, column = 0),
                        Coordinate(row = 1, column = 0),
                        Coordinate(row = 2, column = 0),
                        Coordinate(row = 1, column = 6),
                        Coordinate(row = 7, column = 2),
                        Coordinate(row = 9, column = 8),
                        Coordinate(row = 9, column = 9)
                )
        ))

        val board = subject.presentBoard(123)

        assertThat(board.sunkenShips).containsExactlyInAnyOrder(Ship.CRUISER, Ship.DESTROYER)
    }
}