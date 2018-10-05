package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player
import org.springframework.stereotype.Component

@Component
class GameRegistry {
    lateinit var game: Game

    fun setWinner(winnerId: Int) {
        val winner = if (game.computerPlayer.id == winnerId) {
            game.computerPlayer
        } else {
            game.humanPlayer
        }
        game = game.copy(winner = winner)
    }

    fun getPlayer(playerId: Int): Player {
        return if (game.computerPlayer.id == playerId) {
            game.computerPlayer
        } else {
            game.humanPlayer
        }
    }

    fun updatePlayer(playerId: Int, player: Player) {
        game = if (game.computerPlayer.id == playerId) {
            game.copy(computerPlayer = player)
        } else {
            game.copy(humanPlayer = player)
        }
    }
}