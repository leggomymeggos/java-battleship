package com.leggomymeggos.battleship.game

import org.springframework.stereotype.Component

@Component
class GameRegistry {
    private val games = mutableMapOf<Int, GameEntity>()

    fun setWinner(gameId: Int, winnerId: Int) {
        val game = getGame(gameId = gameId)

        if (game.playerIds.contains(winnerId)) {
            games[gameId] = game.copy(winnerId = winnerId)
        }
    }

    fun getGame(gameId: Int): GameEntity {
        return games[gameId] ?: TODO()
    }

    fun changeTurn(gameId: Int) {
        val game = getGame(gameId)
        val nextActivePlayer = getDefendingPlayer(gameId, game.activePlayerId)

        games[gameId] = game.copy(activePlayerId = nextActivePlayer)
    }

    fun getDefendingPlayer(gameId: Int, attackingPlayerId: Int): Int {
        val game = getGame(gameId)

        return game.playerIds.filterNot { it == attackingPlayerId }.first()
    }

    fun register(game: GameEntity) {
        games[game.id] = game
    }
}
