package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player

data class Game(
        val humanPlayer: Player = Player(),
        val computerPlayer: Player = Player(),
        val activePlayerId: Int = -1,
        val winner: Player? = null
)