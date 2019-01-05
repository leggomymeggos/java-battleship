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
                players = listOf(humanPlayer, computerPlayer),
                activePlayerId = humanPlayer.id
        )
        gameRegistry.register(game)

        return game
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): Board {
        val defendingPlayer = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)
                .run {
                    playerService.hitBoard(this, coordinate).run {
                        gameRegistry.updatePlayer(gameId, this)
                        this
                    }
                }

        if (playerService.isDefeated(defendingPlayer)) {
            gameRegistry.setWinner(gameId, attackingPlayerId)
        } else {
            gameRegistry.changeTurn(gameId)
        }

        return defendingPlayer.board
    }

    fun getWinner(gameId: Int): Player? {
        return gameRegistry.getGame(gameId).winner
    }

    fun getActivePlayerId(gameId: Int): Int {
        return gameRegistry.getGame(gameId).activePlayerId
    }
}