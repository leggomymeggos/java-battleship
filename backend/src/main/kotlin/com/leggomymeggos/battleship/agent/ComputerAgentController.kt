package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/games"])
@CrossOrigin
class ComputerAgentController(val computerAgentService: ComputerAgentService) {

    @RequestMapping(
            value = ["/{gameId}/opponent-attack"],
            method = [RequestMethod.GET]
    )
    fun attack(@PathVariable(name = "gameId") gameId: Int,
               @RequestParam(name = "attackerId") attackingPlayerId: Int): Board {
        return computerAgentService.takeTurn(gameId, attackingPlayerId)
    }
}