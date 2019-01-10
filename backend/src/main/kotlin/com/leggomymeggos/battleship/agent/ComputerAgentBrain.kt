package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import org.springframework.stereotype.Service

@Service
class ComputerAgentBrain {
    fun determineFiringCoordinate(board: Board): Coordinate {
        val validCoordinates = board.grid.mapIndexed { colIndex, col ->
            col.mapIndexedNotNull { index, tile ->
                if (tile.hit) {
                    null
                } else {
                    Coordinate(index, colIndex)
                }
            }
        }.flatten()

        return validCoordinates.random()
    }
}
