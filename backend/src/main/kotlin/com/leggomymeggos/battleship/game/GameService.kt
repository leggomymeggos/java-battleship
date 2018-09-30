package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.PlayerService
import org.springframework.stereotype.Service

@Service
class GameService(val playerService: PlayerService) {
    fun new(): Game {
        val player = playerService.initPlayer()
        return Game(player)
    }
}