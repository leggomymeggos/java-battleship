package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.BoardService
import org.springframework.stereotype.Service

@Service
class GameService(val boardService: BoardService) {
    fun new(): Board {
        return boardService.initBoard()
    }
}