package com.leggomymeggos.battleship.board

import org.springframework.stereotype.Component

@Component
class BoardRegistry {
    private val boards = mutableMapOf<Int, BoardEntity>()

    fun getBoard(id: Int): BoardResult {
        return if (boards[id] != null) {
            BoardResult.Success(boards[id]!!)
        } else BoardResult.NoBoardFound
    }

    fun getBoardForGameAndPlayer(gameId: Int, playerId: Int): BoardResult {
        val board = boards.values.firstOrNull { it.gameId == gameId && it.playerId == playerId }
        return if (board != null) {
            BoardResult.Success(board)
        } else BoardResult.NoBoardFound
    }

    fun register(board: BoardEntity) {
        boards[board.id] = board
    }

    fun saveShipPlacement(boardId: Int, shipPlacement: ShipPlacement) {
        val board = boards[boardId]!!
        val placements = board.shipPlacements.toMutableSet()

        placements.removeIf { it.shipName == shipPlacement.shipName }
        placements.add(shipPlacement)

        boards[boardId] = board.copy(shipPlacements = placements)
    }

    fun saveHitCoordinates(boardId: Int, hitCoordinates: Set<Coordinate>) {
        val board = boards[boardId]!!

        boards[boardId] = board.copy(hitCoordinates = hitCoordinates)
    }
}

data class BoardEntity(
        val id: Int,
        val playerId: Int,
        val gameId: Int,
        val shipPlacements: Set<ShipPlacement>,
        val hitCoordinates: Set<Coordinate>
)

sealed class BoardResult {
    data class Success(val board: BoardEntity) : BoardResult()
    object NoBoardFound : BoardResult()
}