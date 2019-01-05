package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player
import org.springframework.stereotype.Component

@Component
class GameRegistry {
    val games = mutableMapOf<Int, Game>()

    fun setWinner(gameId: Int, winnerId: Int) {
        val game = getGame(gameId)
        val winner = getPlayer(gameId, winnerId)
        games[gameId] = game.copy(winner = winner)
    }

    fun getPlayer(gameId: Int, playerId: Int): Player {
        val game = getGame(gameId)
        return game.players.first { it.id == playerId }
    }

    fun getGame(gameId: Int): Game {
        return games[gameId] ?: TODO()
    }

    fun changeTurn(gameId: Int) {
        val game = getGame(gameId)
        val nextActivePlayer = getDefendingPlayer(gameId, game.activePlayerId).id

        games[gameId] = game.copy(activePlayerId = nextActivePlayer)
    }

    fun updatePlayer(gameId: Int, player: Player) {
        val game = getGame(gameId)

        val players = game.players.toMutableList()
        players.removeIf { it.id == player.id }
        players.add(player)

        games[gameId] = game.copy(players = players)
    }

    fun getDefendingPlayer(gameId: Int, attackingPlayerId: Int): Player {
        val game = getGame(gameId)

        return game.players.filterNot { it.id == attackingPlayerId }.first()
    }

    fun register(game: Game) {
        games[game.id] = game
    }
}