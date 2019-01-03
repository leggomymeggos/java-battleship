package com.leggomymeggos.battleship.player

import com.leggomymeggos.battleship.board.BoardService
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.Direction
import com.leggomymeggos.battleship.board.Ship.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(val boardService: BoardService) {
    fun initPlayer(): Player {
        val board = boardService.initBoard()

        return Player(id = Random().nextInt(Int.MAX_VALUE), board = board)
    }

    fun setShips(player: Player): Player {
        val board = boardService.addShip(player.board, CRUISER, Coordinate(0, 0), Direction.HORIZONTAL).run {
            boardService.addShip(this, SUBMARINE, Coordinate(2, 6), Direction.HORIZONTAL).run {
                boardService.addShip(this, AIRCRAFT_CARRIER, Coordinate(5, 3), Direction.HORIZONTAL).run {
                    boardService.addShip(this, DESTROYER, Coordinate(0, 9), Direction.VERTICAL).run {
                        boardService.addShip(this, BATTLESHIP, Coordinate(9, 9), Direction.VERTICAL)
                    }
                }
            }
        }

        return player.copy(board = board)
    }

    fun hitBoard(player: Player, coordinate: Coordinate): Player {
        val updatedBoard = boardService.hitTile(player.board, coordinate)
        return player.copy(board = updatedBoard)
    }

    fun isDefeated(player: Player): Boolean {
        val shipsOnBoard = player.board.grid.flatten().asSequence().filter { it.ship != null }.toSet()

        return shipsOnBoard.size == player.board.sunkenShips.size
    }

    fun randomlySetShips(player: Player): Player {
        val board = boardService.addShipRandomly(player.board, CRUISER, Direction.values().random()).run {
            boardService.addShipRandomly(this, SUBMARINE, Direction.values().random()).run {
                boardService.addShipRandomly(this, AIRCRAFT_CARRIER, Direction.values().random()).run {
                    boardService.addShipRandomly(this, DESTROYER, Direction.values().random()).run {
                        boardService.addShipRandomly(this, BATTLESHIP, Direction.values().random())
                    }
                }
            }
        }

        return player.copy(board = board)
    }
}