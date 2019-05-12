package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import org.springframework.stereotype.Service

@Service
class PlayerRegistry {
    val players = mutableMapOf<String, PlayerEntity>()

    fun register(gameId: Int, player: Player) {
        players[gameId.toString() + player.id.toString()] = PlayerEntity(player.id, gameId, player.board)
    }

    fun getPlayer(gameId: Int, playerId: Int): PlayerEntity {
        val playerEntity = players[gameId.toString() + playerId.toString()]
        return playerEntity ?: TODO()
    }

    fun updatePlayer(gameId: Int, player: Player) {
        players[gameId.toString() + player.id.toString()] =
                getPlayer(gameId, player.id).copy(board = player.board)
    }
}

data class PlayerEntity(
        val id: Int,
        val gameId: Int,
        val board: Board
)