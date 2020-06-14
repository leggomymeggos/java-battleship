package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.board.gridOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CoordinateDeciderTest {
    private val subject = CoordinateDecider()

    // region randomValidCoordinate
    @Test
    fun `randomValidCoordinate picks a valid coordinate`() {
        val coordinate = subject.randomValidCoordinate(Board(gridOf(2)))

        val validCoordinates = listOf(
                Coordinate(0, 0),
                Coordinate(1, 0),
                Coordinate(0, 1),
                Coordinate(1, 1)
        )

        assertThat(validCoordinates).containsAnyOf(coordinate)
    }

    @Test
    fun `randomValidCoordinate does not pick a coordinate that has already been hit`() {
        val board = Board(listOf(
                listOf(Tile(hit = true), Tile()),
                listOf(Tile(), Tile(hit = true)),
                listOf(Tile(), Tile(hit = true))
        ))
        val coordinate = subject.randomValidCoordinate(board)

        val invalidCoordinates = listOf(
                Coordinate(0, 0),
                Coordinate(1, 1),
                Coordinate(1, 2)
        )

        assertThat(invalidCoordinates).doesNotContain(coordinate)
    }

    @Test
    fun `randomValidCoordinate doesn't pick the same coordinate twice in a row`() {
        val board = Board(gridOf(10))
        val coordinate1 = subject.randomValidCoordinate(board)
        val coordinate2 = subject.randomValidCoordinate(board)

        assertThat(coordinate1).isNotEqualTo(coordinate2)
    }
    // endregion
}