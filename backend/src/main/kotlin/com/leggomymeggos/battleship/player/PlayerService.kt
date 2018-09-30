package com.leggomymeggos.battleship.player

import com.leggomymeggos.battleship.board.BoardService
import org.springframework.stereotype.Service

@Service
class PlayerService(val boardService: BoardService) {
    fun initPlayer(): Player {
        val board = boardService.initBoard()

        return Player(board)
    }
}