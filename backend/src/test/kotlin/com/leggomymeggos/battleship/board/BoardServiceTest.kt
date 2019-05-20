package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.Orientation.HORIZONTAL
import com.leggomymeggos.battleship.board.Orientation.VERTICAL
import com.leggomymeggos.battleship.board.Ship.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BoardServiceTest {

    lateinit var boardService: BoardService

    @Before
    fun setup() {
        boardService = BoardService()
    }

    @Test
    fun `initBoard returns a fresh board`() {
        val board = boardService.initBoard()

        assertThat(board.grid).containsExactly(
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile()),
                listOf(Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile(), Tile())
        )
    }

    // region addShip
    @Test
    fun `addShip adds a ship to the board`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(0, 0), HORIZONTAL)

        assertThat(boardWithShip.grid[0][0].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[0][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[0][2].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip adds ship starting at given coordinate`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(1, 2), HORIZONTAL)

        assertThat(boardWithShip.grid[2][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][3].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip considers orientation`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(2, 1), VERTICAL)

        assertThat(boardWithShip.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[3][2].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip handles coordinate as the ending coordinate (horizontal)`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(3, 1), HORIZONTAL)

        assertThat(boardWithCruiser.grid[1][3].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][1].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip handles coordinate as the ending coordinate (vertical)`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(1, 3), VERTICAL)

        assertThat(boardWithCruiser.grid[3][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[2][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][1].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip does not add a ship on top of another ship`() {
        val grid = gridOf(5)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(1, 1), HORIZONTAL)

        val boardMaybeWithMoreShips = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(0, 1), HORIZONTAL)
        assertThat(boardMaybeWithMoreShips.grid[1][0].ship).isNull()
        assertThat(boardMaybeWithMoreShips.grid[1][1].ship).isEqualTo(CRUISER)
        assertThat(boardMaybeWithMoreShips.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardMaybeWithMoreShips.grid[1][3].ship).isEqualTo(CRUISER)

        val boardWithMoreShipsAgain = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(2, 1), HORIZONTAL)
        assertThat(boardWithMoreShipsAgain.grid[1][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][3].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][4].ship).isNull()

        val boardWithVerticalShip = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(2, 0), VERTICAL)
        assertThat(boardWithVerticalShip.grid[0][2].ship).isNull()
        assertThat(boardWithVerticalShip.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithVerticalShip.grid[0][2].ship).isNull()
    }
    // endregion

    // region hitTile
    @Test
    fun `hitTile updates tile for given coordinate`() {
        val board = boardService.hitTile(Board(gridOf(5)), Coordinate(2, 3)).board

        assertThat(board.grid[3][2].hit).isTrue()
    }

    @Test
    fun `hitTile does not overwrite ships`() {
        val board = Board(gridOf(5))
        val boardWithShip = boardService.addShip(board, SUBMARINE, Coordinate(0, 0), HORIZONTAL)

        val hitBoard = boardService.hitTile(boardWithShip, Coordinate(0, 0)).board

        assertThat(hitBoard.grid[0][0].ship).isEqualTo(SUBMARINE)
    }

    @Test
    fun `hitTile can update sunken ships`() {
        val boardWithShip = boardService.addShip(Board(gridOf(5)), DESTROYER, Coordinate(0, 0), HORIZONTAL)

        val hitBoard = boardService.hitTile(boardWithShip, Coordinate(0, 0)).board

        assertThat(hitBoard.sunkenShips).isEmpty()

        val sunkBoard = boardService.hitTile(hitBoard, Coordinate(1, 0)).board
        assertThat(sunkBoard.sunkenShips).containsExactly(DESTROYER)
    }

    @Test
    fun `hitTile returns attack result when it's a hit`() {
        val board = Board(gridOf(5))
        val boardWithShip = boardService.addShip(board, SUBMARINE, Coordinate(0, 0), HORIZONTAL)

        val result = boardService.hitTile(boardWithShip, Coordinate(0, 0)).result

        assertThat(result).isEqualToComparingFieldByField(HitResult.Hit())
    }

    @Test
    fun `hitTile returns attack result when it's a miss`() {
        val board = Board(gridOf(5))
        val boardWithShip = boardService.addShip(board, SUBMARINE, Coordinate(0, 0), HORIZONTAL)

        val result = boardService.hitTile(boardWithShip, Coordinate(3, 2)).result

        assertThat(result).isEqualToComparingFieldByField(HitResult.Miss())
    }

    @Test
    fun `hitTile returns attack result when a ship is sunk`() {
        val board = Board(gridOf(5))
        val boardWithShip = boardService.addShip(board, DESTROYER, Coordinate(0, 0), HORIZONTAL)

        val result = boardService.hitTile(boardWithShip, Coordinate(0, 0)).run {
            boardService.hitTile(this.board, Coordinate(1, 0))
        }.result

        assertThat(result).isEqualTo(HitResult.Sunk(Ship.DESTROYER))
    }
    // endregion

    // region randomValidCoordinate
    @Test
    fun `randomValidCoordinate does not return coordinates containing a ship for vertically placed ships`() {
        val boardWithShip = boardService.addShip(Board(gridOf(4)), DESTROYER, Coordinate(0, 0), VERTICAL)

        val randomCoordinate = boardService.randomValidCoordinate(boardWithShip, CRUISER, VERTICAL)

        val invalidCoordinates = listOf(
                Coordinate(0, 0),
                Coordinate(0, 1),
                Coordinate(0, 2),
                Coordinate(0, 3)
        )

        assertThat(invalidCoordinates).doesNotContain(randomCoordinate)
    }

    @Test
    fun `randomValidCoordinate does not return coordinates containing a ship for horizontally placed ships`() {
        val boardWithShip = boardService.addShip(
                Board(gridOf(4)),
                DESTROYER,
                Coordinate(0, 0),
                HORIZONTAL)

        val randomCoordinate = boardService.randomValidCoordinate(boardWithShip, CRUISER, HORIZONTAL)

        val invalidCoordinates = listOf(
                Coordinate(0, 0),
                Coordinate(1, 0),
                Coordinate(2, 0),
                Coordinate(3, 0)
        )

        assertThat(invalidCoordinates).doesNotContain(randomCoordinate)
    }

    @Test
    fun `randomValidCoordinate does not return invalid placement coordinates for vertically placed ships`() {
        val randomCoordinate = boardService.randomValidCoordinate(Board(gridOf(3)), CRUISER, VERTICAL)

        val invalidCoordinates = listOf(
                Coordinate(0, 1),
                Coordinate(1, 1),
                Coordinate(2, 1)
        )

        assertThat(invalidCoordinates).doesNotContain(randomCoordinate)
    }

    @Test
    fun `randomValidCoordinate does not return invalid placement coordinates for horizontally placed ships`() {
        val randomCoordinate = boardService.randomValidCoordinate(Board(gridOf(4)), BATTLESHIP, HORIZONTAL)

        val invalidCoordinates = listOf(
                Coordinate(1, 0),
                Coordinate(1, 1),
                Coordinate(1, 2),
                Coordinate(1, 3),
                Coordinate(2, 1),
                Coordinate(2, 1),
                Coordinate(2, 2),
                Coordinate(2, 3)
        )

        assertThat(invalidCoordinates).doesNotContain(randomCoordinate)
    }
    // endregion

    // region addShipRandomly
    @Test
    fun `addShipRandomly always adds all ships`() {
        val randomBoard = boardService.addShipRandomly(Board(gridOf(10)), DESTROYER, Orientation.values().random()).run {
            boardService.addShipRandomly(this, CRUISER, Orientation.values().random()).run {
                boardService.addShipRandomly(this, SUBMARINE, Orientation.values().random()).run {
                    boardService.addShipRandomly(this, BATTLESHIP, Orientation.values().random()).run {
                        boardService.addShipRandomly(this, AIRCRAFT_CARRIER, Orientation.values().random())
                    }
                }
            }
        }

        assertThat(randomBoard.grid.flatten().mapNotNull { it.ship }.toSet()).containsExactlyInAnyOrder(
                DESTROYER, CRUISER, SUBMARINE, BATTLESHIP, AIRCRAFT_CARRIER
        )
    }
    // endregion
}