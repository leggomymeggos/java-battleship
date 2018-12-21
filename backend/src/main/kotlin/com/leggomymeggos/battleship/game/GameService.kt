package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(): Game {
        val humanPlayer = playerService.initPlayer()
                .copy(id = 1).run {
                    playerService.randomlySetShips(this)
                }

        val computerPlayer = playerService.initPlayer()
                .copy(id = 2).run {
                    playerService.randomlySetShips(this)
                }

        val game = Game(
                humanPlayer = humanPlayer,
                computerPlayer = computerPlayer,
                activePlayerId = humanPlayer.id
        )
        gameRegistry.game = game

        return game
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): Board {
        val currentGame = gameRegistry.getGame(gameId)

        val defendingPlayer = currentGame.determineDefendingPlayer(attackingPlayerId).run {
            playerService.hitBoard(this, coordinate).run {
                gameRegistry.updatePlayer(this)
                this
            }
        }

        if (playerService.isDefeated(defendingPlayer)) {
            gameRegistry.setWinner(attackingPlayerId)
        } else {
            gameRegistry.changeTurn()
        }

        return defendingPlayer.board
    }

    private fun Game.determineDefendingPlayer(attackingPlayerId: Int): Player {
        return when (attackingPlayerId) {
            computerPlayer.id -> humanPlayer
            else -> computerPlayer
        }
    }

    fun getWinner(): Player? {
        return gameRegistry.game.winner
    }
}