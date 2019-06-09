package com.leggomymeggos.battleship

import com.leggomymeggos.battleship.board.Orientation
import com.leggomymeggos.battleship.board.PlacedShip
import com.leggomymeggos.battleship.board.Ship

fun createPlacedShip(ship: Ship, orientation: Orientation = Orientation.HORIZONTAL): PlacedShip = PlacedShip(ship, orientation)