package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.BoardHitResponse
import com.leggomymeggos.battleship.board.BoardService
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.Orientation
import com.leggomymeggos.battleship.board.Ship.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(private val boardService: BoardService,
                    private val playerRegistry: PlayerRegistry
) {
    fun initPlayer(gameId: Int): Player {
        val board = boardService.initBoard()
        val player = Player(id = Random().nextInt(Int.MAX_VALUE), board = board)

        playerRegistry.register(gameId, player)

        return player
    }

    fun setShips(gameId: Int, playerId: Int): Player {
        val playerEntity = playerRegistry.getPlayer(gameId, playerId)

        val board = boardService.addShip(playerEntity.board, CRUISER, Coordinate(0, 0), Orientation.HORIZONTAL).run {
            boardService.addShip(this, SUBMARINE, Coordinate(2, 6), Orientation.HORIZONTAL).run {
                boardService.addShip(this, AIRCRAFT_CARRIER, Coordinate(5, 3), Orientation.HORIZONTAL).run {
                    boardService.addShip(this, DESTROYER, Coordinate(0, 9), Orientation.VERTICAL).run {
                        boardService.addShip(this, BATTLESHIP, Coordinate(9, 9), Orientation.VERTICAL)
                    }
                }
            }
        }

        val player = Player(
                playerEntity.id,
                board
        )
        playerRegistry.updatePlayer(gameId, player)

        return player
    }

    fun hitBoard(gameId: Int, playerId: Int, coordinate: Coordinate): BoardHitResponse {
        val playerEntity = playerRegistry.getPlayer(gameId, playerId)
        val response = boardService.hitTile(playerEntity.board, coordinate)

        val player = Player(playerEntity.id, response.board)
        playerRegistry.updatePlayer(gameId, player)

        return response
    }

    fun isDefeated(gameId: Int, playerId: Int): Boolean {
        val player = playerRegistry.getPlayer(gameId, playerId)
        val shipsOnBoard = player.board.grid.flatten().asSequence().filter { it.ship != null }.toSet()

        return shipsOnBoard.size == player.board.sunkenShips.size
    }

    fun randomlySetShips(gameId: Int, playerId: Int): Player {
        val playerEntity = playerRegistry.getPlayer(gameId, playerId)

        val board = boardService.addShipRandomly(playerEntity.board, CRUISER, Orientation.values().random()).run {
            boardService.addShipRandomly(this, SUBMARINE, Orientation.values().random()).run {
                boardService.addShipRandomly(this, AIRCRAFT_CARRIER, Orientation.values().random()).run {
                    boardService.addShipRandomly(this, DESTROYER, Orientation.values().random()).run {
                        boardService.addShipRandomly(this, BATTLESHIP, Orientation.values().random())
                    }
                }
            }
        }

        val player = Player(
                playerEntity.id,
                board
        )
        playerRegistry.updatePlayer(gameId, player)

        return player
    }

    fun getPlayer(gameId: Int, playerId: Int): Player {
        val playerEntity = playerRegistry.getPlayer(gameId, playerId)
        return Player(id = playerEntity.id, board = playerEntity.board)
    }
}