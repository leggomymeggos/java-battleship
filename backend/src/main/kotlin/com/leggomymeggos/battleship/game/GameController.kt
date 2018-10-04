package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
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
            value = ["/{gameId}/players/{playerId}/hit"],
            method = [RequestMethod.PUT]
    )
    fun hitBoard(@RequestBody coordinate: Coordinate): Board {
        return gameService.hitBoard(coordinate)
    }


    @RequestMapping(
            value = ["/{gameId}/winner"],
            method = [RequestMethod.GET]
    )
    fun fetchWinner(): Boolean {
        return gameService.getWinner()
    }
}
