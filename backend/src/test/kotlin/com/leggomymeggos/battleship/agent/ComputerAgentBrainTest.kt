package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.game.Difficulty
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ComputerAgentBrainTest {
    private val coordinateDecider = mock<CoordinateDecider>()
    private val subject = ComputerAgentBrain(coordinateDecider)

    // region determineFiringCoordinate - Difficulty.EASY
    @Test
    fun `determineFiringCoordinate - difficulty EASY - gets random valid coordinate`() {
        val board = Board(gridOf(1))
        subject.determineFiringCoordinate(board, Difficulty.EASY)

        verify(coordinateDecider).randomValidCoordinate(board)
    }

    @Test
    fun `determineFiringCoordinate -difficulty EASY - returns random valid coordinate`() {
        val coordinate = Coordinate(10, 20)
        whenever(coordinateDecider.randomValidCoordinate(any())).thenReturn(coordinate)

        val result = subject.determineFiringCoordinate(Board(), Difficulty.EASY)

        assertThat(result).isSameAs(coordinate)
    }
    // endregion
}