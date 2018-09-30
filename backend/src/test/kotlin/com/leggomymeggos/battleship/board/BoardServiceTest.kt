package com.leggomymeggos.battleship.board

import com.leggomymeggos.battleship.board.Ship.CRUISER
import com.leggomymeggos.battleship.board.Ship.SUBMARINE
import com.leggomymeggos.battleship.board.tile.Tile
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
        val grid = createGridWithSize(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(0, 0), Direction.HORIZONTAL)

        assertThat(boardWithShip.grid[0][0].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[0][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[0][2].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip adds ship starting at given coordinate`() {
        val grid = createGridWithSize(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(1, 2), Direction.HORIZONTAL)

        assertThat(boardWithShip.grid[2][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][3].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip considers direction`() {
        val grid = createGridWithSize(4)
        val board = Board(grid)

        val boardWithShip = boardService.addShip(board, CRUISER, Coordinate(2, 1), Direction.VERTICAL)

        assertThat(boardWithShip.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[2][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithShip.grid[3][2].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip does not add a ship on top of another ship`() {
        val grid = createGridWithSize(5)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(1, 1), Direction.HORIZONTAL)

        val boardMaybeWithMoreShips = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(0, 1), Direction.HORIZONTAL)
        assertThat(boardMaybeWithMoreShips.grid[1][0].ship).isNull()
        assertThat(boardMaybeWithMoreShips.grid[1][1].ship).isEqualTo(CRUISER)
        assertThat(boardMaybeWithMoreShips.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardMaybeWithMoreShips.grid[1][3].ship).isEqualTo(CRUISER)

        val boardWithMoreShipsAgain = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(2, 1), Direction.HORIZONTAL)
        assertThat(boardWithMoreShipsAgain.grid[1][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][3].ship).isEqualTo(CRUISER)
        assertThat(boardWithMoreShipsAgain.grid[1][4].ship).isNull()

        val boardWithVerticalShip = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(2, 0), Direction.VERTICAL)
        assertThat(boardWithVerticalShip.grid[0][2].ship).isNull()
        assertThat(boardWithVerticalShip.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithVerticalShip.grid[0][2].ship).isNull()
    }

    @Test
    fun `addShip handles coordinate as the ending coordinate (horizontal)`() {
        val grid = createGridWithSize(4)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(3, 1), Direction.HORIZONTAL)

        assertThat(boardWithCruiser.grid[1][3].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][2].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][1].ship).isEqualTo(CRUISER)
    }

    @Test
    fun `addShip handles coordinate as the ending coordinate (vertical)`() {
        val grid = createGridWithSize(4)
        val board = Board(grid)

        val boardWithCruiser = boardService.addShip(board, CRUISER, Coordinate(1, 3), Direction.VERTICAL)

        assertThat(boardWithCruiser.grid[3][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[2][1].ship).isEqualTo(CRUISER)
        assertThat(boardWithCruiser.grid[1][1].ship).isEqualTo(CRUISER)
    }
    // endregion

    private fun createGridWithSize(size: Int): List<List<Tile>> {
        val tiles = mutableListOf<MutableList<Tile>>()

        for (row in 0 until size) {
            val innerTiles = mutableListOf<Tile>()
            for (col in 0 until size) {
                innerTiles.add(Tile())
            }
            tiles.add(innerTiles)
        }

        return tiles
    }
}