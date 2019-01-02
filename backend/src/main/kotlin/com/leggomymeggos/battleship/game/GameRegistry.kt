package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player
import org.springframework.stereotype.Component

@Component
class GameRegistry {
    lateinit var game: Game

    fun setWinner(winnerId: Int) {
        val winner = when (winnerId) {
            game.computerPlayer.id -> game.computerPlayer
            else -> game.humanPlayer
        }
        game = game.copy(winner = winner)
    }

    fun getPlayer(playerId: Int): Player {
        return when (playerId) {
            game.computerPlayer.id -> game.computerPlayer
            else -> game.humanPlayer
        }
    }

    fun getGame(gameId: Int): Game {
        return game
    }

    fun changeTurn() {
        val nextActivePlayer = getDefendingPlayer(game.activePlayerId).id

        game = game.copy(activePlayerId = nextActivePlayer)
    }

    fun updatePlayer(player: Player) {
        game = when (player.id) {
            game.computerPlayer.id -> game.copy(computerPlayer = player)
            else -> game.copy(humanPlayer = player)
        }
    }

    fun getDefendingPlayer(attackingPlayerId: Int): Player {
        return when (attackingPlayerId) {
            game.computerPlayer.id -> game.humanPlayer
            else -> game.computerPlayer
        }
    }
}