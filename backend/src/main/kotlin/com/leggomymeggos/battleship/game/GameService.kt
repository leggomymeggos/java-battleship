package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(): Game {
        val player = playerService.initPlayer()
        val player2 = playerService.initPlayer()

        val playerWithShips = playerService.randomlySetShips(player)
        val player2WithShips = playerService.randomlySetShips(player2)

        val game = Game(playerWithShips, player2WithShips)
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