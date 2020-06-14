package com.leggomymeggos.battleship.board

data class BoardHitResponse(val result: HitResult, val board: Board?)

sealed class HitResult {
    data class Sunk(val ship: Ship) : HitResult() {
        val hitType: HitType = HitType.SUNK
    }

    object Miss : HitResult() {
        val hitType: HitType = HitType.MISS
    }

    object Hit : HitResult() {
        val hitType: HitType = HitType.HIT
    }

    object GameOver : HitResult()
    data class Failure(val message: String): HitResult()
}

enum class HitType {
    SUNK,
    HIT,
    MISS
}
