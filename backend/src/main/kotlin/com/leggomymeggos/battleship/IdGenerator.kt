package com.leggomymeggos.battleship

import java.util.*

object IdGenerator {
    fun random(): Int = Random().nextInt(Int.MAX_VALUE)
}