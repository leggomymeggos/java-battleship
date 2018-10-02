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

    @Test
    fun `hit board hits board for player`() {
        val playerBoard = Board(gridOf(3))
        whenever(gameRegistry.game).thenReturn(Game(Player(playerBoard)))
        whenever(playerService.hitBoard(any(), any())).thenReturn(Board())

        val coordinate = Coordinate(1, 2)
        gameService.hitBoard(coordinate)

        verify(playerService).hitBoard(playerBoard, coordinate)
    }

    @Test
    fun `hitBoard sets hit board on registered game and returns it`() {
        val game = Game(Player(Board(gridOf(3))))
        whenever(gameRegistry.game).thenReturn(game)

        val expectedBoard = Board(gridOf(4))
        whenever(playerService.hitBoard(any(), any())).thenReturn(expectedBoard)

        val hitBoard = gameService.hitBoard(Coordinate(1, 1))

        verify(gameRegistry).game = game.copy(player = game.player.copy(board = expectedBoard))
        assertThat(hitBoard).isEqualTo(expectedBoard)
    }
}