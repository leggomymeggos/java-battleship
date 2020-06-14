package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.IdGenerator
import org.springframework.stereotype.Service

@Service
class PlayerService(private val playerRegistry: PlayerRegistry) {
    fun initPlayer(): Int {

        val player = PlayerEntity(
                id = IdGenerator.random()
        )
        playerRegistry.register(player)

        return player.id
    }
}