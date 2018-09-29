package com.leggomymeggos.battleship.game

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(val gameService: GameService) {

    @RequestMapping(
            value = ["/games/new"],
            method = [RequestMethod.GET]
    )
    @CrossOrigin
    fun newGame(): Game {
        return gameService.new()
    }
}
