package com.leggomymeggos.battleship.board

data class BoardHitResponse(val result: HitResult, val board: Board)

enum class HitResult {
    GAME_OVER,
    MISS,
    HIT,
    SUNK
}
