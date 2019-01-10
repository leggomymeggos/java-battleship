package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.game.GameService
import org.springframework.stereotype.Service

@Service
class ComputerAgentService(
        val gameService: GameService,
        val computerAgentBrain: ComputerAgentBrain
) {
    fun takeTurn(gameId: Int, attackerId: Int): Board {
        val defendingBoard = gameService.getDefendingBoard(gameId, attackerId).run {
            copy(grid = this.grid.map { row ->
                row.map { Tile(hit = it.hit) }
            })
        }
        val coordinate = computerAgentBrain.determineFiringCoordinate(defendingBoard)
        return gameService.attack(gameId, attackerId, coordinate)
    }
}