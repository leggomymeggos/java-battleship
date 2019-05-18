package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.agent.Player
import com.leggomymeggos.battleship.agent.PlayerService
import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.BoardHitResponse
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.HitResult
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(difficulty: Difficulty = Difficulty.EASY): Game {
        val newGame = GameEntity()
        val humanPlayer = playerService.initPlayer(newGame.id)
                .run {
                    playerService.randomlySetShips(newGame.id, this.id)
                }

        val computerPlayer = playerService.initPlayer(newGame.id)
                .run {
                    playerService.randomlySetShips(newGame.id, this.id)
                }

        val game = newGame.copy(
                playerIds = listOf(humanPlayer.id, computerPlayer.id),
                activePlayerId = humanPlayer.id,
                difficulty = difficulty
        )
        gameRegistry.register(game)

        return Game(
                id = game.id,
                players = listOf(humanPlayer, computerPlayer),
                activePlayerId = game.activePlayerId,
                winnerId = game.winnerId,
                difficulty = game.difficulty
        )
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): BoardHitResponse {
        val game = gameRegistry.getGame(gameId)
        if (game.winnerId != -1) {
            val board = playerService.getPlayer(gameId, game.winnerId).board
            return BoardHitResponse(HitResult.GAME_OVER, board)
        }

        val defendingPlayerId = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)

        val attackResponse = defendingPlayerId
                .run {
                    playerService.hitBoard(gameId, this, coordinate)
                }

        if (playerService.isDefeated(gameId, defendingPlayerId)) {
            gameRegistry.setWinner(gameId, attackingPlayerId)
        } else {
            gameRegistry.changeTurn(gameId)
        }

        return attackResponse
    }

    fun getWinner(gameId: Int): Player? {
        val winnerId = gameRegistry.getGame(gameId).winnerId
        if (winnerId == -1) {
            return null
        }

        return playerService.getPlayer(gameId, winnerId)
    }

    fun getActivePlayerId(gameId: Int): Int {
        return gameRegistry.getGame(gameId).activePlayerId
    }

    fun getDefendingBoard(gameId: Int, attackingPlayerId: Int): Board {
        val playerId = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)
        return playerService.getPlayer(gameId, playerId).board
    }

    fun getDifficulty(gameId: Int): Difficulty {
        return gameRegistry.getGame(gameId).difficulty
    }
}