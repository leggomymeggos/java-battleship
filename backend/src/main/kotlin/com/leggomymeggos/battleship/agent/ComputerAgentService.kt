package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.BoardHitResponse
import com.leggomymeggos.battleship.board.Tile
import com.leggomymeggos.battleship.game.GameService
import org.springframework.stereotype.Service

@Service
class ComputerAgentService(
        val gameService: GameService,
        val computerAgentBrain: ComputerAgentBrain
) {
    fun takeTurn(gameId: Int, attackerId: Int): BoardHitResponse {
        val defendingBoard = gameService.getDefendingBoard(gameId, attackerId).run {
            copy(grid = this.grid.map { row ->
                row.map { Tile(hit = it.hit) }
            })
        }
        val difficulty = gameService.getDifficulty(gameId)
        val coordinate = computerAgentBrain.determineFiringCoordinate(defendingBoard, difficulty)
        return gameService.attack(gameId, attackerId, coordinate)
    }
}