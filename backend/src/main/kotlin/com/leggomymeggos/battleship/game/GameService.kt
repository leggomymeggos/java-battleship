package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.player.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService, val gameRegistry: GameRegistry) {

    fun new(): Game {
        val player = playerService.initPlayer()
        val gamePlayer = playerService.setShips(player)

        val game = Game(gamePlayer)
        gameRegistry.game = game

        return game
    }

    fun hitBoard(coordinate: Coordinate): Board {
        val game = gameRegistry.game
        val updatedPlayer = playerService.hitBoard(game.player, coordinate)

        gameRegistry.game = game.copy(
                player = updatedPlayer
        )

        if (playerService.isDefeated(updatedPlayer)) {
            gameRegistry.setWinner()
        }
        return updatedPlayer.board
    }

    fun getWinner(): Boolean {
        return gameRegistry.game.winner
    }
}