package com.leggomymeggos.battleship.agent

import org.springframework.stereotype.Service

@Service
class PlayerRegistry {
    private val _players = mutableSetOf<PlayerEntity>()

    val players: Set<PlayerEntity>
        get() = _players.toSet()

    fun register(player: PlayerEntity) {
        _players.add(player)
    }
}

data class PlayerEntity(
        val id: Int
)