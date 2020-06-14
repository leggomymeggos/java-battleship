package com.leggomymeggos.battleship.board

import org.springframework.stereotype.Service

@Service
class BoardPresenter(
        private val boardService: BoardService
) {
    fun presentBoard(boardId: Int): Board {
        return boardService.getBoard(boardId).let { entity ->
            val grid = gridOf(BoardService.MAX_BOARD_DIMENSION).toMutableList()

            val gridWithShips = grid.mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, columnTile ->
                    columnTile
                            .addShipName(entity, rowIndex, colIndex)
                            .addHitData(entity, rowIndex, colIndex)
                }
            }

            val sunkenShips = entity.shipPlacements.filter { entity.hitCoordinates.containsAll(it.coordinates) }.map { it.shipName }

            Board(grid = gridWithShips, sunkenShips = sunkenShips.toSet())
        }
    }

    private fun Tile.addHitData(entity: BoardEntity, rowIndex: Int, colIndex: Int): Tile {
        val hitCoordinate = entity.hitCoordinates.firstOrNull { it == Coordinate(rowIndex, colIndex) }
        return if (hitCoordinate != null) {
            copy(hit = true)
        } else this
    }

    private fun Tile.addShipName(entity: BoardEntity, rowIndex: Int, colIndex: Int): Tile {
        val occupiedCoordinate = entity.shipPlacements.firstOrNull { it.coordinates.contains(Coordinate(rowIndex, colIndex)) }
        return if (occupiedCoordinate != null) {
            copy(ship = occupiedCoordinate.shipName)
        } else this
    }
}