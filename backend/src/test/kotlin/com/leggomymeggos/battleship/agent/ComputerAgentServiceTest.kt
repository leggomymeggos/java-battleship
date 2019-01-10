package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.*
import com.leggomymeggos.battleship.game.GameService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ComputerAgentServiceTest {
    private lateinit var computerAgentService: ComputerAgentService
    private lateinit var gameService: GameService
    private lateinit var computerAgentBrain: ComputerAgentBrain

    @Before
    fun setup() {
        gameService = mock()
        computerAgentBrain = mock()
        computerAgentService = ComputerAgentService(gameService, computerAgentBrain)

        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(Board())
    }

    // region takeTurn
    @Test
    fun `takeTurn requests defending board`() {
        computerAgentService.takeTurn(123, 456)

        verify(gameService).getDefendingBoard(123, 456)
    }

    @Test
    fun `takeTurn determines coordinate to hit from defending board`() {
        val board = Board(gridOf(2))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)

        computerAgentService.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(board)
    }

    @Test
    fun `takeTurn strips ships from defending board before determining coordinate`() {
        val board = Board(listOf(
                listOf(Tile(ship = Ship.AIRCRAFT_CARRIER), Tile()),
                listOf(Tile(), Tile(ship = Ship.DESTROYER)),
                listOf(Tile(ship = Ship.BATTLESHIP), Tile())
        ))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)

        computerAgentService.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(Board(listOf(
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile())
        )))
    }

    @Test
    fun `takeTurn preserves if the tile was hit`() {
        val board = Board(listOf(
                listOf(Tile(ship = Ship.AIRCRAFT_CARRIER, hit = true), Tile()),
                listOf(Tile(), Tile(ship = Ship.DESTROYER)),
                listOf(Tile(ship = Ship.BATTLESHIP), Tile(hit = true))
        ))
        whenever(gameService.getDefendingBoard(any(), any())).thenReturn(board)

        computerAgentService.takeTurn(0, 0)

        verify(computerAgentBrain).determineFiringCoordinate(Board(listOf(
                listOf(Tile(hit = true), Tile()),
                listOf(Tile(), Tile()),
                listOf(Tile(), Tile(hit = true))
        )))
    }

    @Test
    fun `takeTurn attacks with the determined coordinate`() {
        val coordinate = Coordinate(1, 2)
        whenever(computerAgentBrain.determineFiringCoordinate(any())).thenReturn(coordinate)

        computerAgentService.takeTurn(123, 456)

        verify(gameService).attack(123, 456, coordinate)
    }

    @Test
    fun `takeTurn returns the attacked board`() {
        whenever(computerAgentBrain.determineFiringCoordinate(any())).thenReturn(Coordinate(0, 0))
        val board = Board(gridOf(10))
        whenever(gameService.attack(any(), any(), any())).thenReturn(board)

        val result = computerAgentService.takeTurn(0, 0)

        assertThat(result).isEqualTo(board)
    }
    // endregion
}