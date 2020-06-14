package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.IdGenerator
import org.springframework.stereotype.Service

@Service
class BoardService(
        private val boardRegistry: BoardRegistry
) {

    companion object {
        const val MAX_BOARD_DIMENSION = 10
    }

    fun getBoard(boardId: Int): BoardEntity {
        val boardResult = boardRegistry.getBoard(boardId)
        check(boardResult !is BoardResult.NoBoardFound) { "No board found for id $boardId" }

        return (boardResult as BoardResult.Success).board
    }

    fun getBoardIdForGameAndPlayer(gameId: Int, playerId: Int): Int {
        val boardResult = boardRegistry.getBoardForGameAndPlayer(gameId, playerId)
        check(boardResult !is BoardResult.NoBoardFound) { "No board found for gameId $gameId and playerId $playerId" }

        return (boardResult as BoardResult.Success).board.id
    }

    fun initBoard(playerId: Int, gameId: Int): Int {
        val id = IdGenerator.random()
        boardRegistry.register(BoardEntity(
                id = id,
                playerId = playerId,
                gameId = gameId,
                shipPlacements = emptySet(),
                hitCoordinates = emptySet()
        ))
        return id
    }

    fun placeShip(boardId: Int, ship: Ship, orientation: Orientation, startingCoordinate: Coordinate): Placement {
        val boardResult = boardRegistry.getBoard(boardId)
        if (boardResult is BoardResult.NoBoardFound) {
            return Placement.Failure("No board found for id: $boardId")
        }
        val board = (boardResult as BoardResult.Success).board

        val coordinates = mutableListOf<Coordinate>()
        while (coordinates.size < ship.size) {
            val coordinate = when (orientation) {
                Orientation.VERTICAL -> {
                    getNextVerticalCoordinate(startingCoordinate, ship.size, coordinates.size)
                }
                Orientation.HORIZONTAL -> {
                    getNextHorizontalCoordinate(startingCoordinate, ship.size, coordinates.size)
                }
            }
            coordinates.add(coordinate)
        }

        if (board.shipPlacements.flatMap { it.coordinates }.any { coordinates.contains(it) }) {
            return Placement.Failure("Invalid ship placement. Please try again.")
        }

        val shipPlacement = ShipPlacement(
                shipName = ship,
                coordinates = coordinates
        )
        boardRegistry.saveShipPlacement(boardId, shipPlacement)


        return Placement.Success
    }

    fun hitCoordinate(boardId: Int, coordinate: Coordinate): HitResult {
        val boardResult = boardRegistry.getBoard(boardId)
        if (boardResult is BoardResult.NoBoardFound) {
            return HitResult.Failure("No board found for id: $boardId")
        }
        val board = (boardResult as BoardResult.Success).board

        val previouslySunkenShips = board.shipPlacements.filter {
            board.hitCoordinates.containsAll(it.coordinates)
        }
        val shipsStillInPlay = board.shipPlacements - previouslySunkenShips

        val hitCoordinates = board.hitCoordinates.toMutableSet()
        hitCoordinates.add(coordinate)
        boardRegistry.saveHitCoordinates(boardId, hitCoordinates)

        val sunkenShip = sunkenShipOrNull(shipsStillInPlay, hitCoordinates)

        return when {
            shipsStillInPlay.size == 1 && sunkenShip != null -> HitResult.GameOver
            sunkenShip != null -> HitResult.Sunk(sunkenShip.shipName)
            wasShipHit(shipsStillInPlay, coordinate) -> HitResult.Hit
            else -> HitResult.Miss
        }
    }

    fun placeShipRandomly(boardId: Int, ship: Ship, orientation: Orientation): Placement {
        val coordinate = randomUnoccupiedCoordinate(boardId)
        val placement = placeShip(boardId, ship, orientation, coordinate)

        if (placement is Placement.Failure) {
            placeShipRandomly(boardId, ship, orientation)
        }

        return Placement.Success
    }

    fun randomlySetShips(boardId: Int) {
        Ship.values().forEach {
            placeShipRandomly(boardId, it, Orientation.values().random())
        }
    }

    private fun randomUnoccupiedCoordinate(boardId: Int): Coordinate {
        val fullBoardCoordinates = mutableListOf<Coordinate>()
        for (row in 0 until MAX_BOARD_DIMENSION) {
            for (col in 0 until MAX_BOARD_DIMENSION) {
                fullBoardCoordinates.add(Coordinate(row = row, column = col))
            }
        }

        val boardResult = boardRegistry.getBoard(boardId)
        boardResult as BoardResult.Success

        val occupiedCoordinates = boardResult.board.shipPlacements.flatMap { it.coordinates }
        return (fullBoardCoordinates - occupiedCoordinates).random()
    }

    private fun getNextHorizontalCoordinate(startingCoordinate: Coordinate, shipSize: Int, placedCoordinatesSize: Int): Coordinate {
        val column = getNextCoordinatePosition(startingCoordinate.column, shipSize, placedCoordinatesSize)
        return Coordinate(column = column, row = startingCoordinate.row)
    }

    private fun getNextVerticalCoordinate(startingCoordinate: Coordinate, shipSize: Int, placedCoordinatesSize: Int): Coordinate {
        val row = getNextCoordinatePosition(startingCoordinate.row, shipSize, placedCoordinatesSize)
        return Coordinate(row = row, column = startingCoordinate.column)
    }

    private fun sunkenShipOrNull(shipsStillInPlay: Set<ShipPlacement>, hitCoordinates: MutableSet<Coordinate>) =
            shipsStillInPlay.firstOrNull { hitCoordinates.containsAll(it.coordinates) }

    private fun wasShipHit(shipsStillInPlay: Set<ShipPlacement>, coordinate: Coordinate) =
            shipsStillInPlay.any { it.coordinates.contains(coordinate) }

    private fun getNextCoordinatePosition(startingCoordinate: Int, shipSize: Int, placedCoordinatesSize: Int): Int {
        return if (startingCoordinate + shipSize >= MAX_BOARD_DIMENSION) {
            startingCoordinate - placedCoordinatesSize
        } else placedCoordinatesSize + startingCoordinate
    }
}

sealed class Placement {
    object Success : Placement()
    data class Failure(val message: String) : Placement()
}
