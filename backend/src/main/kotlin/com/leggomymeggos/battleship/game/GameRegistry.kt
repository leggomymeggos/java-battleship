package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player
import org.springframework.stereotype.Component

@Component
class GameRegistry {
    val games = mutableMapOf<Int, Game>()

    fun setWinner(gameId: Int, winnerId: Int) {
        val game = getGame(gameId)
        val winner = when (winnerId) {
            game.computerPlayer.id -> game.computerPlayer
            else -> game.humanPlayer
        }
        games[gameId] = game.copy(winner = winner)
    }

    fun getPlayer(gameId: Int, playerId: Int): Player {
        val game = getGame(gameId)
        return when (playerId) {
            game.computerPlayer.id -> game.computerPlayer
            else -> game.humanPlayer
        }
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
        games[gameId] = when (player.id) {
            game.computerPlayer.id -> game.copy(computerPlayer = player)
            else -> game.copy(humanPlayer = player)
        }
    }

    fun getDefendingPlayer(gameId: Int, attackingPlayerId: Int): Player {
        val game = getGame(gameId)
        return when (attackingPlayerId) {
            game.computerPlayer.id -> game.humanPlayer
            else -> game.computerPlayer
        }
    }

    fun register(game: Game) {
        games[game.id] = game
    }
}