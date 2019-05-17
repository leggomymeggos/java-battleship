package com.leggomymeggos.battleship.game

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameRegistryTest {
    lateinit var gameRegistry: GameRegistry

    @Before
    fun setup() {
        gameRegistry = GameRegistry()
    }

    // region register
    @Test
    fun `register keeps track of a game`() {
        val game = GameEntity(123)
        gameRegistry.register(game)

        assertThat(gameRegistry.getGame(123)).isEqualTo(game)
    }

    @Test
    fun `register can register multiple games`() {
        val firstGame = GameEntity(123)
        val secondGame = GameEntity(456)

        gameRegistry.register(firstGame)
        gameRegistry.register(secondGame)

        assertThat(gameRegistry.getGame(123)).isEqualTo(firstGame)
        assertThat(gameRegistry.getGame(456)).isEqualTo(secondGame)
    }
    // endregion

    // region setWinner
    @Test
    fun `setWinner sets winner on correct game based on id`() {
        gameRegistry.register(GameEntity(id = 123, playerIds = listOf(3, 2)))

        gameRegistry.setWinner(123, 2)
        assertThat(gameRegistry.getGame(123).winnerId).isEqualTo(2)

        gameRegistry.setWinner(123, 3)
        assertThat(gameRegistry.getGame(123).winnerId).isEqualTo(3)
    }

    @Test
    fun `setWinner clears active player`() {
        gameRegistry.register(GameEntity(id = 123, playerIds = listOf(3, 2), activePlayerId = 3))

        gameRegistry.setWinner(123, 2)
        assertThat(gameRegistry.getGame(123).activePlayerId).isEqualTo(-1)
    }

    @Test
    fun `setWinner does not set winner for a player that does not exist in the game`() {
        gameRegistry.register(GameEntity(id = 123, playerIds = listOf(3, 2)))

        gameRegistry.setWinner(123, 52)
        assertThat(gameRegistry.getGame(123).winnerId).isEqualTo(-1)
    }
    // endregion

    @Test
    fun `changeTurn toggles active player on the game with given id`() {
        val gameId = 493
        val player1Id = 123
        val player2Id = 456

        gameRegistry.register(GameEntity(
                id = gameId,
                playerIds = listOf(player1Id, player2Id),
                activePlayerId = player1Id
        ))

        gameRegistry.changeTurn(gameId)
        assertThat(gameRegistry.getGame(gameId).activePlayerId).isEqualTo(player2Id)

        gameRegistry.changeTurn(gameId)
        assertThat(gameRegistry.getGame(gameId).activePlayerId).isEqualTo(player1Id)
    }

    @Test
    fun `getDefendingPlayer returns defending playerId on the game with given id`() {
        val gameId = 2
        val player1Id = 123
        val player2Id = 456

        gameRegistry.register(GameEntity(id = gameId, playerIds = listOf(player1Id, player2Id)))

        var defendingPlayer = gameRegistry.getDefendingPlayer(gameId, player2Id)
        assertThat(defendingPlayer).isEqualTo(player1Id)

        defendingPlayer = gameRegistry.getDefendingPlayer(gameId, player1Id)
        assertThat(defendingPlayer).isEqualTo(player2Id)
    }
}