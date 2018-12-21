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

        val game = Game(humanPlayer, computerPlayer)
        gameRegistry.game = game

        return game
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): Board {
        val currentGame = gameRegistry.getGame(gameId)

        val defendingPlayer = when (attackingPlayerId) {
            currentGame.computerPlayer.id -> currentGame.humanPlayer
            else -> currentGame.computerPlayer
        }.run {
            playerService.hitBoard(this, coordinate)
        }

        gameRegistry.updatePlayer(defendingPlayer)

        if (playerService.isDefeated(defendingPlayer)) {
            gameRegistry.setWinner(attackingPlayerId)
        }

        return defendingPlayer.board
    }

    fun getWinner(): Player? {
        return gameRegistry.game.winner
    }
}