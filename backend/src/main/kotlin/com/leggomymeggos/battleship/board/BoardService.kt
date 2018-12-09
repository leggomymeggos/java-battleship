package com.leggomymeggos.battleship.board

import org.springframework.stereotype.Service

@Service
class BoardService {
    fun initBoard(): Board {
        return Board(gridOf(10))
    }

    fun addShip(board: Board, ship: Ship, coordinate: Coordinate, direction: Direction): Board {
        val newGrid = board.grid.toMutableList().apply {
            when (direction) {
                Direction.HORIZONTAL -> {
                    val normalizedCoordinate = coordinate.normalizeForHorizontalPlacement(this, ship)
                    if (containsNoShipsInHorizontalPlacement(normalizedCoordinate, ship)) {
                        addShipHorizontally(normalizedCoordinate, ship)
                    } else {
                        println("invalid horizontal placement for $ship at $normalizedCoordinate for board ${board.grid.map { it.map { col -> col.ship } }}")
                    }
                }
                Direction.VERTICAL -> {
                    val normalizedCoordinate = coordinate.normalizeForVerticalPlacement(this, ship)
                    if (containsNoShipsInVerticalPlacement(normalizedCoordinate, ship)) {
                        addShipVertically(normalizedCoordinate, ship)
                    } else {
                        println("invalid vertical placement for $ship at $normalizedCoordinate for board ${board.grid.map { it.map { col -> col.ship } }}")
                    }
                }
            }
        }

        return Board(newGrid)
    }

    fun hitTile(board: Board, coordinate: Coordinate): Board {
        val hitBoard = board.copy(grid = board.grid.hitCoordinate(coordinate))

        val ship = hitBoard.grid[coordinate.y][coordinate.x].ship

        if (ship?.isSunk(hitBoard.grid) == true) {
            hitBoard.sunkenShips.add(ship)
        }

        return hitBoard
    }

    fun addShipRandomly(board: Board, ship: Ship, direction: Direction): Board {
        val coordinate = randomValidCoordinate(board, ship, direction)
        return addShip(board, ship, coordinate, direction)
    }

    tailrec fun randomValidCoordinate(board: Board, ship: Ship, direction: Direction): Coordinate {
        val randomCoordinate = board.grid
                .mapIndexed { yIndex, row ->
                    row.mapIndexed { xIndex, _ ->
                        Coordinate(xIndex, yIndex)
                    }.select {
                        when (direction) {
                            Direction.HORIZONTAL -> it.isValidHorizontalCoordinate(board.grid, ship)
                            Direction.VERTICAL -> it.isValidVerticalCoordinate(board.grid, ship)
                        }
                    }
                }.flatten().random()

        val normalizedCoordinate = when (direction) {
            Direction.HORIZONTAL -> randomCoordinate.normalizeForHorizontalPlacement(board.grid, ship)
            Direction.VERTICAL -> randomCoordinate.normalizeForVerticalPlacement(board.grid, ship)
        }

        val isValidCoordinate = when (direction) {
            Direction.HORIZONTAL -> board.grid.containsNoShipsInHorizontalPlacement(normalizedCoordinate, ship)
            Direction.VERTICAL -> board.grid.containsNoShipsInVerticalPlacement(normalizedCoordinate, ship)
        }

        return when {
            isValidCoordinate -> randomCoordinate
            else -> randomValidCoordinate(board, ship, direction)
        }
    }

    private fun Grid.containsNoShipsInHorizontalPlacement(coordinate: Coordinate, ship: Ship): Boolean {
        val tilesContainingShip = this[coordinate.y]
                .subList(coordinate.x, ship.size + coordinate.x)
                .filter { it.ship != null }
        return tilesContainingShip.isEmpty()
    }

    private fun Grid.containsNoShipsInVerticalPlacement(coordinate: Coordinate, ship: Ship): Boolean {
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

    private fun Coordinate.isValidHorizontalCoordinate(grid: List<List<Tile>>, ship: Ship): Boolean {
        val xCoordinate = when {
            ship.size + this.x > grid.size -> (this.x + 1) - ship.size
            else -> this.x
        }
        return xCoordinate >= 0
    }

    private fun Coordinate.normalizeForHorizontalPlacement(grid: List<List<Tile>>, ship: Ship): Coordinate {
        val xCoordinate = when {
            ship.size + this.x > grid.size -> (this.x + 1) - ship.size
            else -> this.x
        }

        return this.copy(x = xCoordinate)
    }

    private fun Coordinate.isValidVerticalCoordinate(grid: List<List<Tile>>, ship: Ship): Boolean {
        val yCoordinate = when {
            ship.size + this.y > grid.size -> (this.y + 1) - ship.size
            else -> this.y
        }
        return yCoordinate >= 0
    }

    private fun Coordinate.normalizeForVerticalPlacement(grid: List<List<Tile>>, ship: Ship): Coordinate {
        val yCoordinate = when {
            ship.size + this.y > grid.size -> (this.y + 1) - ship.size
            else -> this.y
        }

        return this.copy(y = yCoordinate)
    }

    private fun Grid.hitCoordinate(coordinate: Coordinate): Grid {
        return toMutableList().apply {
            this[coordinate.y] = this[coordinate.y].withTileHit(coordinate.x)
        }
    }

    private fun List<Tile>.withTileHit(index: Int): List<Tile> {
        return toMutableList().apply {
            this[index] = this[index].copy(hit = true)
        }
    }

    private fun Ship.isSunk(grid: Grid): Boolean {
        val hitTilesWithShip = grid.flatten().asSequence()
                .filter { it.ship == this }
                .filter { it.hit }
                .toList()

        return hitTilesWithShip.size == this.size
    }
}


inline fun <T> Iterable<T>.select(predicate: (T) -> Boolean): List<T> {
    return this.filter(predicate)
}