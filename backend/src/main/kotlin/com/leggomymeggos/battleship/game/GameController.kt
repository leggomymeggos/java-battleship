package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.agent.Player
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/games"])
@CrossOrigin
class GameController(val gameService: GameService) {

    @RequestMapping(
            value = ["/new"],
            method = [RequestMethod.GET]
    )
    fun newGame(): Game {
        return gameService.new()
    }

    @RequestMapping(
            value = ["/{gameId}/attack"],
            method = [RequestMethod.PUT]
    )
    fun attackBoard(@PathVariable(name = "gameId") gameId: Int,
                    @RequestParam(name = "attackerId") attackingPlayerId: Int,
                    @RequestBody coordinate: Coordinate): Board {
        return gameService.attack(gameId, attackingPlayerId, coordinate)
    }

    @RequestMapping(
            value = ["/{gameId}/winner"],
            method = [RequestMethod.GET]
    )
    fun fetchWinner(@PathVariable(name = "gameId") gameId: Int): Player? {
        return gameService.getWinner(gameId)
    }

    @RequestMapping(
            value = ["/{gameId}/players/active"],
            method = [RequestMethod.GET]
    )
    fun fetchActivePlayer(@PathVariable(name = "gameId") gameId: Int): Int {
        return gameService.getActivePlayerId(gameId)
    }
}
