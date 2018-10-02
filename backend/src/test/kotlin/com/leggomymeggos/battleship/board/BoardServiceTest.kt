package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.Direction.*
import com.leggomymeggos.battleship.board.Ship.CRUISER
import com.leggomymeggos.battleship.board.Ship.SUBMARINE
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
    fun `addShip considers direction`() {
        val grid = gridOf(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(2, 1), VERTICAL)

        assertThat(boardWithShip.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[3][2].ship).isEqualTo(CRUISER)
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
    // endregion

    @Test
    fun `hitTile updates tile for given coordinate`() {
        val board = boardService.hitTile(Board(gridOf(5)), Coordinate(2, 3))

        assertThat(board.grid[3][2].hit).isTrue()
    }

    @Test
    fun `hitTile does not overwrite ships`() {
        val board = Board(gridOf(5))
        val boardWithShip = boardService.addShip(board, SUBMARINE, Coordinate(0, 0), HORIZONTAL)

        val hitBoard = boardService.hitTile(boardWithShip, Coordinate(0, 0))

        assertThat(hitBoard.grid[0][0].ship).isEqualTo(SUBMARINE)
    }
}