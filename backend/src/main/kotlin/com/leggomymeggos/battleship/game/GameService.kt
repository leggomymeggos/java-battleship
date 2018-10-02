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
        val hitBoard = playerService.hitBoard(game.player.board, coordinate)

        gameRegistry.game = game.copy(
                player = game.player.copy(
                        board = hitBoard
                )
        )
        return hitBoard
    }
}