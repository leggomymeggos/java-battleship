package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board

data class Player(
        val id: Int = -1,
        val board: Board = Board()
)