package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.player.Player
import java.util.*

data class Game(
        val id: Int = Random().nextInt(Int.MAX_VALUE),
        val humanPlayer: Player = Player(),
        val computerPlayer: Player = Player(),
        val activePlayerId: Int = -1,
        val winner: Player? = null
)