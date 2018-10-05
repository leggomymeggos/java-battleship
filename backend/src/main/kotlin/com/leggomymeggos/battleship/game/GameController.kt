package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.player.Player
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
    fun hitBoard(@PathVariable(name = "playerId") defendingPlayerId: Int,
                 @RequestBody coordinate: Coordinate,
                 @RequestParam(name = "attackerId") attackingPlayerId: Int): Board {
        return gameService.hitBoard(defendingPlayerId, coordinate, attackingPlayerId)
    }

    @RequestMapping(
            value = ["/{gameId}/winner"],
            method = [RequestMethod.GET]
    )
    fun fetchWinner(): Player? {
        return gameService.getWinner()
    }
}
