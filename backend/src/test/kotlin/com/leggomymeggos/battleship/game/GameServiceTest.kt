package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.player.Player
import com.leggomymeggos.battleship.player.PlayerService
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class GameServiceTest {

    private lateinit var playerService: PlayerService
    private lateinit var gameRegistry: GameRegistry
    private lateinit var gameService: GameService

    @Before
    fun setup() {
        playerService = mock()
        gameRegistry = mock()
        gameService = GameService(playerService, gameRegistry)

        whenever(playerService.initPlayer()).thenReturn(Player())
        whenever(playerService.randomlySetShips(any())).thenReturn(Player())
        whenever(playerService.hitBoard(any(), any())).thenReturn(Player())

        whenever(gameRegistry.getGame(any())).thenReturn(Game())
     }

    // region new
    @Test
    fun `new requests two players from playerService`() {
        gameService.new()

        verify(playerService, times(2)).initPlayer()
    }

    @Test
    fun `new randomly sets ships`() {
        val player = Player(board = Board()).copy(id = 1)
        val player2 = Player(board = Board(gridOf(1))).copy(id = 2)
        whenever(playerService.initPlayer())
                .thenReturn(player)
                .thenReturn(player2)

        gameService.new()

        verify(playerService).randomlySetShips(player)
        verify(playerService).randomlySetShips(player2)
    }

    @Test
    fun `new adds game to game registry`() {
        val player = Player(board = Board())
        val player2 = Player(board = Board(gridOf(1)))
        whenever(playerService.randomlySetShips(any()))
                .thenReturn(player)
                .thenReturn(player2)

        gameService.new()

        val argumentCaptor = argumentCaptor<Game>()
        verify(gameRegistry).game = argumentCaptor.capture()

        assertThat(argumentCaptor.firstValue).isEqualTo(Game(humanPlayer = player, computerPlayer = player2))
    }

    @Test
    fun `new returns a game with ships`() {
        val player = Player(board = Board())
        val player2 = Player(board = Board(gridOf(1)))
        whenever(playerService.randomlySetShips(any()))
                .thenReturn(player)
                .thenReturn(player2)

        val game = gameService.new()

        assertThat(game).isEqualTo(Game(humanPlayer = player, computerPlayer = player2))
    }
    // endregion

    // region attack
    @Test
    fun `attack retrieves given game`() {
        gameService.attack(143, 0, Coordinate(0, 0))

        verify(gameRegistry).getGame(143)
    }

    @Test
    fun `attack hits board of defending player`() {
        val attackingPlayerId = 2
        val defendingPlayer = Player(id = 1)
        val attackingPlayer = Player(id = attackingPlayerId)
        val game = Game(defendingPlayer, attackingPlayer)

        whenever(gameRegistry.getGame(any())).thenReturn(game)

        val coordinate = Coordinate(1, 2)
        gameService.attack(0, attackingPlayerId, coordinate)

        verify(playerService).hitBoard(defendingPlayer, coordinate)
    }

    @Test
    fun `attack updates and returns board for defending player`() {
        val expectedPlayer = Player(board = Board(gridOf(4)), id = 789)
        whenever(playerService.hitBoard(any(), any())).thenReturn(expectedPlayer)

        val attack = gameService.attack(123, 0, Coordinate(1, 1))

        verify(gameRegistry).updatePlayer(expectedPlayer)
        assertThat(attack).isEqualTo(expectedPlayer.board)
    }

    @Test
    fun `attack checks winning player`() {
        val player = Player(board = Board(gridOf(3)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(player)

        gameService.attack(0, 0, Coordinate(0, 0))

        verify(playerService).isDefeated(player)
    }

    @Test
    fun `attack sets winning player`() {
        whenever(playerService.isDefeated(any())).thenReturn(true)

        gameService.attack(0, 12, Coordinate(0, 0))

        verify(gameRegistry).setWinner(12)
    }
    // endregion

    @Test
    fun `getWinner returns winner from game registry`() {
        val winner = Player(board = Board(gridOf(2)))
        whenever(gameRegistry.game).thenReturn(Game(winner = winner))

        assertThat(gameService.getWinner()).isEqualTo(winner)

        verify(gameRegistry).game
    }
}