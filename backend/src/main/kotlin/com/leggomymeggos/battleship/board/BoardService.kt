package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.tile.Tile
import org.springframework.stereotype.Service

@Service
class BoardService {

    fun initBoard(): Board {
        val tiles = mutableGrid()

        for (row in 0..9) {
            val innerTiles = mutableListOf<Tile>()
            for (col in 0..9) {
                innerTiles.add(Tile())
            }
            tiles.add(innerTiles)
        }

        return Board(tiles)
    }

    fun addShip(board: Board, ship: Ship, coordinate: Coordinate, direction: Direction): Board {
        val newGrid = board.grid.toMutableList().apply {
            when (direction) {
                Direction.HORIZONTAL -> {
                    val normalizedCoordinate = coordinate.normalizeForHorizontalPlacement(this, ship)
                    if (isValidHorizontalPlacement(normalizedCoordinate, ship)) {
                        addShipHorizontally(normalizedCoordinate, ship)
                    }
                }
                Direction.VERTICAL -> {
                    val normalizedCoordinate = coordinate.normalizeForVerticalPlacement(this, ship)
                    if (isValidVerticalPlacement(normalizedCoordinate, ship)) {
                        addShipVertically(normalizedCoordinate, ship)
                    }
                }
            }
        }

        return Board(newGrid)
    }

    private fun Grid.isValidHorizontalPlacement(coordinate: Coordinate, ship: Ship): Boolean {
        val tilesContainingShip = this[coordinate.y]
                .subList(coordinate.x, ship.size + coordinate.x)
                .filter { it.ship != null }
        return tilesContainingShip.isEmpty()
    }

    private fun Grid.isValidVerticalPlacement(coordinate: Coordinate, ship: Ship): Boolean {
        val tilesContainingShip = this.map { it[coordinate.x] }
                .subList(coordinate.y, ship.size + coordinate.y)
                .filter { it.ship != null }
        return tilesContainingShip.isEmpty()
    }

    private fun MutableGrid.addShipHorizontally(coordinate: Coordinate, ship: Ship) {
        for (i in coordinate.x until ship.size + coordinate.x) {
            val shipPlacement = this[coordinate.y].toMutableList()
            shipPlacement[i] = Tile(ship)
            this[coordinate.y] = shipPlacement
        }
    }

    private fun MutableGrid.addShipVertically(coordinate: Coordinate, ship: Ship) {
        for (i in coordinate.y until ship.size + coordinate.y) {
            val shipPlacement = this[i].toMutableList()
            shipPlacement[coordinate.x] = Tile(ship)
            this[i] = shipPlacement
        }
    }

    private fun Coordinate.normalizeForHorizontalPlacement(grid: List<List<Tile>>, ship: Ship): Coordinate {
        val xCoordinate = when {
            ship.size + this.x > grid.size -> (this.x + 1) - ship.size
            else -> this.x
        }

        return this.copy(x = xCoordinate)
    }

    private fun Coordinate.normalizeForVerticalPlacement(grid: List<List<Tile>>, ship: Ship): Coordinate {
        val yCoordinate = when {
            ship.size + this.y > grid.size -> (this.y +1) - ship.size
            else -> this.y
        }

        return this.copy(y = yCoordinate)
    }
}
