package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.*
import com.leggomymeggos.battleship.game.Difficulty
import com.leggomymeggos.battleship.game.GameService
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ComputerAgentServiceTest {

    private val gameService = mock<GameService> {
        on { getDefendingBoard(any(), any()) } doReturn Board()
        on { getDifficulty(any()) } doReturn Difficulty.EASY
    }
    private val computerAgentBrain = mock<ComputerAgentBrain>()
    private val subject = ComputerAgentService(gameService, computerAgentBrain)

    // region takeTurn
    @Test
    fun `takeTurn requests defending board`() {
        subject.takeTurn(123, 456)

        verify(gameService).getDefendingBoard(123, 456)
    }

    @Test
    fun `takeTurn requests game difficulty`() {
        subject.takeTurn(123, 0)
        verify(gameService).getDifficulty(123)
    }

    @Test
    fun `takeTurn determines coordinate to hit from defending board, considering difficulty`() {
        val board = Board(gridOf(2))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)
        whenever(gameService.getDifficulty(any())).thenReturn(Difficulty.HARD)

        subject.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(board, Difficulty.HARD)
    }

    @Test
    fun `takeTurn strips ships from defending board before determining coordinate`() {
        val board = Board(listOf(
                listOf(Tile(ship = Ship.AIRCRAFT_CARRIER), Tile()),
                listOf(Tile(), Tile(ship = Ship.DESTROYER)),
                listOf(Tile(ship = Ship.BATTLESHIP), Tile())
        ))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)

        subject.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(eq(Board(listOf(
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile())
        ))), any())
    }

    @Test
    fun `takeTurn preserves if the tile was hit`() {
        val board = Board(listOf(
                listOf(Tile(ship = Ship.AIRCRAFT_CARRIER, hit = true), Tile()),
                listOf(Tile(), Tile(ship = Ship.DESTROYER)),
                listOf(Tile(ship = Ship.BATTLESHIP), Tile(hit = true))
        ))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)

        subject.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(eq(Board(listOf(
                listOf(Tile(hit = true), Tile()),
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile(hit = true))
        ))), any())
    }

    @Test
    fun `takeTurn attacks with the determined coordinate`() {
        val coordinate = Coordinate(1, 2)
        whenever(computerAgentBrain.determineFiringCoordinate(any(), any())).thenReturn(coordinate)

        subject.takeTurn(123, 456)

        verify(gameService).attack(123, 456, coordinate)
    }

    @Test
    fun `takeTurn returns the attacked board`() {
        whenever(computerAgentBrain.determineFiringCoordinate(any(), any())).thenReturn(Coordinate(0, 0))
        val response = BoardHitResponse(HitResult.Hit, Board(gridOf(10)))
        whenever(gameService.attack(any(), any(), any())).thenReturn(response)

        val result = subject.takeTurn(0, 0)

        assertThat(result).isEqualTo(response)
    }
    // endregion
}