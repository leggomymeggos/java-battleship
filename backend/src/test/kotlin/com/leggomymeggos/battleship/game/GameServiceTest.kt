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
        whenever(playerService.setShips(any())).thenReturn(Player())
        whenever(playerService.hitBoard(any(), any())).thenReturn(Player())

        whenever(gameRegistry.getPlayer(any())).thenReturn(Player())
     }

    // region new
    @Test
    fun `new requests two players from playerService`() {
        gameService.new()

        verify(playerService, times(2)).initPlayer()
    }

    @Test
    fun `new sets ships`() {
        val player = Player(board = Board())
        val player2 = Player(board = Board(gridOf(1)))
        whenever(playerService.initPlayer())
                .thenReturn(player)
                .thenReturn(player2)

        gameService.new()

        verify(playerService).setShips(player)
        verify(playerService).setShips(player2)
    }

    @Test
    fun `new adds game to game registry`() {
        val player = Player(board = Board())
        val player2 = Player(board = Board(gridOf(1)))
        whenever(playerService.setShips(any()))
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
        whenever(playerService.setShips(any()))
                .thenReturn(player)
                .thenReturn(player2)

        val game = gameService.new()

        assertThat(game).isEqualTo(Game(humanPlayer = player, computerPlayer = player2))
    }
    // endregion

    // region hitBoard
    @Test
    fun `hit board retrieves given player`() {
        gameService.hitBoard(143, Coordinate(0, 0), 0)

        verify(gameRegistry).getPlayer(143)
    }

    @Test
    fun `hit board hits board for given player`() {
        val player = Player(board = Board(gridOf(3)))
        whenever(gameRegistry.getPlayer(any())).thenReturn(player)

        val coordinate = Coordinate(1, 2)
        gameService.hitBoard(0, coordinate, 0)

        verify(playerService).hitBoard(player, coordinate)
    }

    @Test
    fun `hitBoard updates board for player`() {
        val expectedPlayer = Player(board = Board(gridOf(4)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(expectedPlayer)

        val hitBoard = gameService.hitBoard(123, Coordinate(1, 1), 0)

        verify(gameRegistry).updatePlayer(123, expectedPlayer)
        assertThat(hitBoard).isEqualTo(expectedPlayer.board)
    }

    @Test
    fun `hitBoard checks winning player`() {
        val player = Player(board = Board(gridOf(3)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(player)

        gameService.hitBoard(0, Coordinate(0, 0), 0)

        verify(playerService).isDefeated(player)
    }

    @Test
    fun `hitBoard sets winning player`() {
        whenever(playerService.isDefeated(any())).thenReturn(true)

        gameService.hitBoard(0, Coordinate(0, 0), 12)

        verify(gameRegistry).setWinner(12)
    }
    // endregion

    @Test
    fun `getWinner returns winner from game registry`() {
        val winner = Player(board = Board(gridOf(2)))
        whenever(gameRegistry.game).thenReturn(Game(winner = winner))

        assertThat(gameService.getWinner()).isEqualTo(winner)

        verify(gameRegistry, times(2)).game
    }
}