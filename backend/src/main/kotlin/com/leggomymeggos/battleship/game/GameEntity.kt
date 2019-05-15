package com.leggomymeggos.battleship.game

import java.util.*

data class GameEntity(
        val id: Int = Random().nextInt(Int.MAX_VALUE),
        val playerIds: List<Int> = listOf(),
        val activePlayerId: Int = -1,
        val winnerId: Int = -1,
        val difficulty: Difficulty = Difficulty.EASY
)
