package com.leggomymeggos.battleship.board

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BoardRegistryTest {

    private val subject = BoardRegistry()

    // region getBoard
    @Test
    fun `getBoard returns Success when there is a registered board for the requested id`() {
        val entity = BoardEntity(id = 1, playerId = 2, gameId = 3, shipPlacements = setOf(), hitCoordinates = setOf())

        subject.register(entity)
        val result = subject.getBoard(1)

        assertThat(result).isEqualTo(BoardResult.Success(entity))
    }

    @Test
    fun `getBoard returns NoBoardFound when there is a registered board for the requested id`() {
        val result = subject.getBoard(1)
        assertThat(result).isEqualTo(BoardResult.NoBoardFound)
    }
    // endregion

    // region getBoardForGameAndPlayer
    @Test
    fun `getBoardForGameAndPlayer returns Success when there is a registered board for the requested ids`() {
        val entity = BoardEntity(id = 1, playerId = 2, gameId = 3, shipPlacements = setOf(), hitCoordinates = setOf())

        subject.register(entity)
        val result = subject.getBoardForGameAndPlayer(3, 2)

        assertThat(result).isEqualTo(BoardResult.Success(entity))
    }

    @Test
    fun `getBoardForGameAndPlayer returns NoBoardFound when there is a registered board for the requested ids`() {
        val result = subject.getBoardForGameAndPlayer(2, 3)
        assertThat(result).isEqualTo(BoardResult.NoBoardFound)
    }
    // endregion

    @Test
    fun `register registers the board`() {
        val board1 = BoardEntity(
                id = 1,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )
        val board2 = BoardEntity(
                id = 5,
                playerId = 6,
                gameId = 7,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )

        subject.register(board1)
        subject.register(board2)

        assertThat(subject.getBoard(1)).isEqualTo(BoardResult.Success(board1))
        assertThat(subject.getBoard(5)).isEqualTo(BoardResult.Success(board2))
    }

    @Test
    fun `getBoard returns NoBoardFound when no board has been registered with given id`() {
        assertThat(subject.getBoard(1)).isEqualTo(BoardResult.NoBoardFound)
    }

    // region saveShipPlacement
    @Test
    fun `saveShipPlacement saves ship placement on board`() {
        val board = BoardEntity(
                id = 1,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )

        subject.register(board)

        subject.saveShipPlacement(1, ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3))))

        val boardResult = subject.getBoard(1)
        boardResult as BoardResult.Success
        assertThat(boardResult.board.shipPlacements).isEqualTo(setOf(ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3)))))
    }

    @Test
    fun `saveShipPlacement adds ship placements`() {
        val board = BoardEntity(
                id = 33,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(
                        ShipPlacement(Ship.DESTROYER, listOf(Coordinate(2, 2), Coordinate(2, 3)))
                ),
                hitCoordinates = setOf()
        )

        subject.register(board)

        subject.saveShipPlacement(33, ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3))))

        val boardResult = subject.getBoard(33)
        boardResult as BoardResult.Success
        assertThat(boardResult.board.shipPlacements).isEqualTo(setOf(
                ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3))),
                ShipPlacement(Ship.DESTROYER, listOf(Coordinate(2, 2), Coordinate(2, 3)))
        ))
    }

    @Test
    fun `saveShipPlacement replaces previously placed ships`() {
        val board = BoardEntity(
                id = 33,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(
                        ShipPlacement(Ship.CRUISER, listOf(Coordinate(2, 2), Coordinate(2, 3)))
                ),
                hitCoordinates = setOf()
        )

        subject.register(board)

        subject.saveShipPlacement(33, ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3))))

        val boardResult = subject.getBoard(33)
        boardResult as BoardResult.Success
        assertThat(boardResult.board.shipPlacements).isEqualTo(setOf(
                ShipPlacement(Ship.CRUISER, listOf(Coordinate(1, 2), Coordinate(1, 3)))
        ))
    }
    // endregion

    // region saveHitCoordinates
    @Test
    fun `saveHitCoordinates saves hit coordinates on board`() {
        val board = BoardEntity(
                id = 33,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf()
        )

        subject.register(board)

        subject.saveHitCoordinates(33, setOf(Coordinate(1, 2), Coordinate(1, 3)))

        val boardResult = subject.getBoard(33)
        boardResult as BoardResult.Success
        assertThat(boardResult.board.hitCoordinates).isEqualTo(setOf(Coordinate(1, 2), Coordinate(1, 3)))
    }

    @Test
    fun `saveHitCoordinates replaces previously hit coordinates`() {
        val board = BoardEntity(
                id = 33,
                playerId = 2,
                gameId = 3,
                shipPlacements = setOf(),
                hitCoordinates = setOf(Coordinate(1, 2), Coordinate(1, 3))
        )

        subject.register(board)

        subject.saveHitCoordinates(33, setOf(Coordinate(9, 8), Coordinate(7, 6)))

        val boardResult = subject.getBoard(33)
        boardResult as BoardResult.Success
        assertThat(boardResult.board.hitCoordinates).isEqualTo(setOf(Coordinate(9, 8), Coordinate(7, 6)))
    }
    // endregion
}