package com.leggomymeggos.battleship.player

import com.leggomymeggos.battleship.board.BoardService
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.Direction
import com.leggomymeggos.battleship.board.Ship.*
import org.springframework.stereotype.Service

@Service
class PlayerService(val boardService: BoardService) {
    fun initPlayer(): Player {
        val board = boardService.initBoard()

        return Player(board)
    }

    fun setShips(player: Player): Player {

        val boardWithCruiser = boardService.addShip(player.board, CRUISER, Coordinate(0, 0), Direction.HORIZONTAL)
        val boardWithSubmarine = boardService.addShip(boardWithCruiser, SUBMARINE, Coordinate(2, 6), Direction.HORIZONTAL)
        val boardWithAircraftCarrier = boardService.addShip(boardWithSubmarine, AIRCRAFT_CARRIER, Coordinate(5, 3), Direction.HORIZONTAL)
        val boardWithDestroyer = boardService.addShip(boardWithAircraftCarrier, DESTROYER, Coordinate(0, 9), Direction.VERTICAL)
        val boardWithBattleship = boardService.addShip(boardWithDestroyer, BATTLESHIP, Coordinate(9, 9), Direction.VERTICAL)

        return Player(boardWithBattleship)
    }

    fun hitBoard(player: Player, coordinate: Coordinate): Player {
        val updatedBoard = boardService.hitTile(player.board, coordinate)
        return player.copy(board = updatedBoard)
    }
}