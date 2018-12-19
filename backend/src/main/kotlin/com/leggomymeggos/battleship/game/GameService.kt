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

    fun hitBoard(defendingPlayerId: Int, coordinate: Coordinate, attackingPlayerId: Int): Board {
        val player = gameRegistry.getPlayer(defendingPlayerId)

        val updatedPlayer = playerService.hitBoard(player, coordinate)

        gameRegistry.updatePlayer(defendingPlayerId, updatedPlayer)

        if (playerService.isDefeated(updatedPlayer)) {
            gameRegistry.setWinner(attackingPlayerId)
        }
        return updatedPlayer.board
    }

    fun getWinner(): Player? {
        return gameRegistry.game.winner
    }
}