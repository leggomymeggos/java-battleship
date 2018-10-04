package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.Tile
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

        whenever(playerService.initPlayer()).thenReturn(Player(Board()))
        whenever(playerService.setShips(any())).thenReturn(Player(Board()))
    }

    // region new
    @Test
    fun `new requests player from playerService`() {
        gameService.new()

        verify(playerService).initPlayer()
    }

    @Test
    fun `new sets ships`() {
        val player = Player(Board())
        whenever(playerService.initPlayer()).thenReturn(player)

        gameService.new()

        verify(playerService).setShips(player)
    }

    @Test
    fun `new adds game to game registry`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.setShips(any())).thenReturn(player)

        gameService.new()

        val argumentCaptor = argumentCaptor<Game>()
        verify(gameRegistry).game = argumentCaptor.capture()

        assertThat(argumentCaptor.firstValue.player).isEqualTo(player)
    }

    @Test
    fun `new returns a game with ships`() {
        val player = Player(board = Board(listOf(listOf(Tile()))))
        whenever(playerService.setShips(any())).thenReturn(player)

        val game = gameService.new()
        assertThat(game.player).isEqualTo(player)
    }
    // endregion

    // region hitBoard
    @Test
    fun `hit board hits board for player`() {
        whenever(playerService.hitBoard(any(), any())).thenReturn(Player(Board()))

        val player = Player(Board(gridOf(3)))
        whenever(gameRegistry.game).thenReturn(Game(player))

        val coordinate = Coordinate(1, 2)
        gameService.hitBoard(coordinate)

        verify(playerService).hitBoard(player, coordinate)
    }

    @Test
    fun `hitBoard sets hit board on registered game and returns it`() {
        val game = Game(Player(Board(gridOf(3))))
        whenever(gameRegistry.game).thenReturn(game)

        val expectedPlayer = Player(Board(gridOf(4)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(expectedPlayer)

        val hitBoard = gameService.hitBoard(Coordinate(1, 1))

        verify(gameRegistry).game = game.copy(player = expectedPlayer)
        assertThat(hitBoard).isEqualTo(expectedPlayer.board)
    }

    @Test
    fun `hitBoard checks winning player`() {
        whenever(gameRegistry.game).thenReturn(Game(Player(Board())))

        val player = Player(Board(gridOf(3)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(player)

        gameService.hitBoard(Coordinate(0, 0))

        verify(playerService).isDefeated(player)
    }

    @Test
    fun `hitBoard sets winning player`() {
        val game = Game(Player(Board(gridOf())))
        whenever(gameRegistry.game).thenReturn(game)

        val player = Player(Board(gridOf(1)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(player)
        whenever(playerService.isDefeated(any())).thenReturn(true)

        gameService.hitBoard(Coordinate(0, 0))

        verify(gameRegistry).setWinner()
    }
    // endregion

    @Test
    fun `getWinner returns winner from game registry`() {
        whenever(gameRegistry.game).thenReturn(Game(winner = true, player = Player(Board())))

        assertThat(gameService.getWinner()).isTrue()

        whenever(gameRegistry.game).thenReturn(Game(winner = false, player = Player(Board())))

        assertThat(gameService.getWinner()).isFalse()

        verify(gameRegistry, times(2)).game
    }
}