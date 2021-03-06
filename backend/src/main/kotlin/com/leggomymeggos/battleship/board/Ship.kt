package com.leggomymeggos.battleship.board

enum class Ship(val size: Int) {
    AIRCRAFT_CARRIER( 5),
    BATTLESHIP(4),
    CRUISER(3),
    SUBMARINE(3),
    DESTROYER(2)
}