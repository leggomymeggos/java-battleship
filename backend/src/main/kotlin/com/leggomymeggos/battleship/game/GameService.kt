package com.leggomymeggos.battleship.game

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
}