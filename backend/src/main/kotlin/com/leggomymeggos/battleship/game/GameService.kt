package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.agent.Player
import com.leggomymeggos.battleship.agent.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(difficulty: Difficulty = Difficulty.EASY): Game {
        val newGame = Game()
        val humanPlayer = playerService.initPlayer(newGame.id)
                .run {
                    playerService.randomlySetShips(newGame.id, this.id)
                }

        val computerPlayer = playerService.initPlayer(newGame.id)
                .run {
                    playerService.randomlySetShips(newGame.id, this.id)
                }

        val game = newGame.copy(
                players = listOf(humanPlayer, computerPlayer),
                activePlayerId = humanPlayer.id,
                difficulty = difficulty
        )
        gameRegistry.register(game)

        return game
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): Board {
        val defendingPlayer = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)

        if (gameRegistry.getGame(gameId).winnerId != -1) {
            return defendingPlayer.board
        }

        val attackedPlayer = defendingPlayer
                .run {
                    playerService.hitBoard(gameId, this.id, coordinate).run {
                        gameRegistry.updatePlayer(gameId, this)
                        this
                    }
                }

        if (playerService.isDefeated(gameId, attackedPlayer.id)) {
            gameRegistry.setWinner(gameId, attackingPlayerId)
        } else {
            gameRegistry.changeTurn(gameId)
        }

        return attackedPlayer.board
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
        return gameRegistry.getDefendingPlayer(gameId, attackingPlayerId).board
    }
}