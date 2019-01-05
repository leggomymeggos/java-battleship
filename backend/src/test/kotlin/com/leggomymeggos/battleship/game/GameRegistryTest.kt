package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.player.Player
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
        val game = Game(123)
        gameRegistry.register(game)

        assertThat(gameRegistry.getGame(123)).isEqualTo(game)
    }

    @Test
    fun `register can register multiple games`() {
        val firstGame = Game(123)
        val secondGame = Game(456)

        gameRegistry.register(firstGame)
        gameRegistry.register(secondGame)

        assertThat(gameRegistry.getGame(123)).isEqualTo(firstGame)
        assertThat(gameRegistry.getGame(456)).isEqualTo(secondGame)
    }
    // endregion

    @Test
    fun `setWinner sets winner on correct game based on id`() {
        val gameId = 101
        val humanPlayer = Player(id = 123)
        val computerPlayer = Player(id = 456)
        gameRegistry.register(Game(id = gameId, players = listOf(humanPlayer, computerPlayer)))

        gameRegistry.setWinner(gameId, 123)
        assertThat(gameRegistry.getGame(gameId).winner).isEqualTo(humanPlayer)

        gameRegistry.setWinner(gameId, 456)
        assertThat(gameRegistry.getGame(gameId).winner).isEqualTo(computerPlayer)
    }

    @Test
    fun `getPlayer returns player from game with given id`() {
        val gameId = 493
        val humanPlayer = Player(id = 444)
        val computerPlayer = Player(id = 222)
        gameRegistry.register(Game(id = gameId, players = listOf(humanPlayer, computerPlayer)))

        assertThat(gameRegistry.getPlayer(gameId, 444)).isEqualTo(humanPlayer)
        assertThat(gameRegistry.getPlayer(gameId, 222)).isEqualTo(computerPlayer)
    }

    @Test
    fun `updatePlayer updates player from game with id with the provided player`() {
        val gameId = 493
        val humanPlayer = Player(id = 123, board = Board(gridOf(2)))
        val computerPlayer = Player(id = 456)
        gameRegistry.register(Game(id = gameId, players = listOf(humanPlayer, computerPlayer)))

        val updatedPlayer = Player(id = 123, board = Board(gridOf(1)))
        gameRegistry.updatePlayer(gameId, updatedPlayer)

        assertThat(gameRegistry.getGame(gameId).players).containsExactlyInAnyOrder(computerPlayer, updatedPlayer)
    }

    @Test
    fun `changeTurn toggles active player on the game with given id`() {
        val gameId = 493
        val humanPlayer = Player(id = 123)
        val computerPlayer = Player(id = 456)

        gameRegistry.register(Game(
                id = gameId,
                players = listOf(humanPlayer, computerPlayer),
                activePlayerId = humanPlayer.id
        ))

        gameRegistry.changeTurn(gameId)
        assertThat(gameRegistry.getGame(gameId).activePlayerId).isEqualTo(computerPlayer.id)

        gameRegistry.changeTurn(gameId)
        assertThat(gameRegistry.getGame(gameId).activePlayerId).isEqualTo(humanPlayer.id)
    }

    @Test
    fun `getDefendingPlayer returns defending player on the game with given id`() {
        val gameId = 2
        val humanPlayer = Player(id = 123)
        val computerPlayer = Player(id = 456)

        gameRegistry.register(Game(id = gameId, players = listOf(humanPlayer, computerPlayer)))

        var defendingPlayer = gameRegistry.getDefendingPlayer(gameId, computerPlayer.id)
        assertThat(defendingPlayer).isEqualTo(humanPlayer)

        defendingPlayer = gameRegistry.getDefendingPlayer(gameId, humanPlayer.id)
        assertThat(defendingPlayer).isEqualTo(computerPlayer)
    }
}