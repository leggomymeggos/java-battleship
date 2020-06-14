package com.leggomymeggos.battleship.game

data class Game(
        val id: Int,
        val playerIds: List<Int>,
        val activePlayerId: Int,
        val winnerId: Int = -1,
        val difficulty: Difficulty = Difficulty.EASY
)