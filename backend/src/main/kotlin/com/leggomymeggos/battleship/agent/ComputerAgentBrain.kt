package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.game.Difficulty
import org.springframework.stereotype.Service

@Service
class ComputerAgentBrain(private val coordinateDecider: CoordinateDecider) {
    fun determineFiringCoordinate(board: Board, difficulty: Difficulty): Coordinate {
        return coordinateDecider.randomValidCoordinate(board)
    }
}
