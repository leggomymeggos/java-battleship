package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.agent.Player

data class Game(
        val id: Int = -1,
        val players: List<Player> = listOf(),
        val activePlayerId: Int = -1,
        val winnerId: Int = -1,
        val difficulty: Difficulty = Difficulty.EASY
)