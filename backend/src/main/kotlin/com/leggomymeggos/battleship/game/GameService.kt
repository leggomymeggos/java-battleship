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
        val defendingPlayer = gameRegistry.getDefendingPlayer(attackingPlayerId)
                .run {
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

    fun getWinner(): Player? {
        return gameRegistry.game.winner
    }

    fun getActivePlayerId(): Int {
        return gameRegistry.game.activePlayerId
    }
}