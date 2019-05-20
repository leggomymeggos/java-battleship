package com.leggomymeggos.battleship.board

data class BoardHitResponse(val result: HitResult, val board: Board)

sealed class HitResult {
    data class Sunk(val shipName: Ship) : HitResult() {
        val hitType: HitType = HitType.SUNK
    }

    class Miss : HitResult() {
        val hitType: HitType = HitType.MISS
    }

    class Hit : HitResult() {
        val hitType: HitType = HitType.HIT
    }

    object GameOver : HitResult()
}

enum class HitType {
    SUNK,
    HIT,
    MISS
}
