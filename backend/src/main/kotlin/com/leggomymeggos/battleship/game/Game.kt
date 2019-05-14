package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.agent.Player
import java.util.*

data class Game(
        val id: Int = Random().nextInt(Int.MAX_VALUE),
        val players: List<Player> = listOf(),
        val activePlayerId: Int = -1,
        val winnerId: Int = -1,
        val difficulty: Difficulty = Difficulty.EASY
)