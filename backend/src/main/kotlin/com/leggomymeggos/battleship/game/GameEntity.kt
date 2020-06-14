package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.IdGenerator

data class GameEntity(
        val id: Int = IdGenerator.random(),
        val playerIds: List<Int> = listOf(),
        val activePlayerId: Int = -1,
        val winnerId: Int = -1,
        val difficulty: Difficulty = Difficulty.EASY
)

sealed class GameOverStatus {
    object NoWinner: GameOverStatus()
    data class Winner(val winnerId: Int): GameOverStatus()
}