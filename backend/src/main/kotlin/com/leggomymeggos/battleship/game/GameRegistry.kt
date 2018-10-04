package com.leggomymeggos.battleship.game

import org.springframework.stereotype.Component

@Component
class GameRegistry {
    fun setWinner() {
        game = game.copy(winner = true)
    }

    lateinit var game: Game
}