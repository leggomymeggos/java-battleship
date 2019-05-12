package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.agent.Player
import com.leggomymeggos.battleship.agent.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(difficulty: Difficulty = Difficulty.EASY): Game {
        val humanPlayer = playerService.initPlayer()
                .run {
                    playerService.randomlySetShips(this)
                }

        val computerPlayer = playerService.initPlayer()
                .run {
                    playerService.randomlySetShips(this)
                }

        val game = Game(
                players = listOf(humanPlayer, computerPlayer),
                activePlayerId = humanPlayer.id,
                difficulty = difficulty
        )
        gameRegistry.register(game)

        return game
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): Board {
        val defendingPlayer = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)

        if (gameRegistry.getGame(gameId).winner != null) {
            return defendingPlayer.board
        }

        val attackedPlayer = defendingPlayer
                .run {
                    playerService.hitBoard(this, coordinate).run {
                        gameRegistry.updatePlayer(gameId, this)
                        this
                    }
                }

        if (playerService.isDefeated(attackedPlayer)) {
            gameRegistry.setWinner(gameId, attackingPlayerId)
        } else {
            gameRegistry.changeTurn(gameId)
        }

        return attackedPlayer.board
    }

    fun getWinner(gameId: Int): Player? {
        return gameRegistry.getGame(gameId).winner
    }

    fun getActivePlayerId(gameId: Int): Int {
        return gameRegistry.getGame(gameId).activePlayerId
    }

    fun getDefendingBoard(gameId: Int, attackingPlayerId: Int): Board {
        return gameRegistry.getDefendingPlayer(gameId, attackingPlayerId).board
    }
}