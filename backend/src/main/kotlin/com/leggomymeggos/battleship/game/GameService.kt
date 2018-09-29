package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.BoardService
import org.springframework.stereotype.Service

@Service
class GameService(val boardService: BoardService) {
    fun new(): Game {
        val board = boardService.initBoard()
        return Game(board)
    }
}