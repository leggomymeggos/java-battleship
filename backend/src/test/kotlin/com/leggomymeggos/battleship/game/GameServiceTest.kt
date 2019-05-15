package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.agent.Player
import com.leggomymeggos.battleship.agent.PlayerService
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

        whenever(playerService.initPlayer(any())).thenReturn(Player())
        whenever(playerService.randomlySetShips(any(), any())).thenReturn(Player())
        whenever(playerService.hitBoard(any(), any(), any())).thenReturn(Player())
        whenever(playerService.getPlayer(any(), any())).thenReturn(Player())

        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(-1)

        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity())
    }

    // region new
    @Test
    fun `new requests two players from playerService`() {
        gameService.new()

        verify(playerService, times(2)).initPlayer(any())
    }

    @Test
    fun `new randomly sets ships`() {
        val player = Player(board = Board()).copy(id = 1)
        val player2 = Player(board = Board(gridOf(1))).copy(id = 2)
        whenever(playerService.initPlayer(any()))
                .thenReturn(player)
                .thenReturn(player2)

        gameService.new()

        verify(playerService).randomlySetShips(any(), eq(player.id))
        verify(playerService).randomlySetShips(any(), eq(player2.id))
    }

    @Test
    fun `new adds game to game registry`() {
        val player = Player(id = 123, board = Board())
        val player2 = Player(id = 456, board = Board(gridOf(1)))

        whenever(playerService.randomlySetShips(any(), any()))
                .thenReturn(player)
                .thenReturn(player2)

        gameService.new()

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.playerIds).isEqualTo(listOf(123, 456))
        assertThat(game.activePlayerId).isEqualTo(123)
    }

    @Test
    fun `new adds game with easy difficulty`() {
        gameService.new(Difficulty.EASY)

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.difficulty).isEqualTo(Difficulty.EASY)
    }

    @Test
    fun `new adds game with hard difficulty`() {
        gameService.new(Difficulty.HARD)

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.difficulty).isEqualTo(Difficulty.HARD)
    }

    @Test
    fun `new adds game with the same id as the players`() {
        val game = gameService.new()

        val captor = argumentCaptor<Int>()
        verify(playerService, times(2)).initPlayer(captor.capture())

        assertThat(game.id).isEqualTo(captor.firstValue)
        assertThat(game.id).isEqualTo(captor.secondValue)
    }

    @Test
    fun `new returns a game with ships and the humanPlayer active`() {
        val player = Player(id = 1, board = Board())
        val player2 = Player(id = 3, board = Board(gridOf(1)))
        whenever(playerService.randomlySetShips(any(), any()))
                .thenReturn(player)
                .thenReturn(player2)

        val game = gameService.new()

        assertThat(game.players).isEqualTo(listOf(player, player2))
        assertThat(game.activePlayerId).isEqualTo(1)
    }
    // endregion

    // region attack
    @Test
    fun `attack determines defending player id`() {
        gameService.attack(143, 123, Coordinate(0, 0))

        verify(gameRegistry).getDefendingPlayer(143, 123)
    }

    @Test
    fun `attack requests the active game`() {
        gameService.attack(809, 0, Coordinate(0, 0))

        verify(gameRegistry).getGame(809)
    }

    @Test
    fun `attack hits board of defending player`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)

        val coordinate = Coordinate(1, 2)
        gameService.attack(123, 2, coordinate)

        verify(playerService).hitBoard(123, 3, coordinate)
    }

    @Test
    fun `attack checks winning player`() {
        val player = Player(board = Board(gridOf(3)), id = 890)
        whenever(playerService.hitBoard(any(), any(), any())).thenReturn(player)

        gameService.attack(123, 456, Coordinate(0, 0))

        verify(playerService).isDefeated(123, 890)
    }

    @Test
    fun `attack sets winning player`() {
        whenever(playerService.isDefeated(any(), any())).thenReturn(true)

        gameService.attack(100, 12, Coordinate(0, 0))

        verify(gameRegistry).setWinner(100, 12)
    }

    @Test
    fun `attack updates activePlayerId if there is no winner yet`() {
        whenever(playerService.isDefeated(any(), any())).thenReturn(false)

        gameService.attack(1233, 12, Coordinate(0, 0))

        verify(gameRegistry).changeTurn(1233)
    }

    @Test
    fun `attack returns defending board`() {
        val expectedPlayer = Player(board = Board(gridOf(4)), id = 789)
        whenever(playerService.hitBoard(any(), any(), any())).thenReturn(expectedPlayer)

        val newBoard = gameService.attack(123, 0, Coordinate(1, 1))

        assertThat(newBoard).isEqualTo(expectedPlayer.board)
    }

    @Test
    fun `attack does nothing if the game is already won`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 9))

        gameService.attack(123, 545, Coordinate(0, 0))

        verify(playerService, never()).hitBoard(any(), any(), any())
        verify(playerService, never()).isDefeated(any(), any())
        verify(gameRegistry, never()).changeTurn(any())
        verify(gameRegistry, never()).setWinner(any(), any())
    }

    @Test
    fun `attack looks up the winner if the game is already won`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 8))

        gameService.attack(123, 545, Coordinate(0, 0))

        verify(playerService).getPlayer(123, 8)
    }

    @Test
    fun `attack returns the winner's unchanged board if the game is already won`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 8))
        val board = Board(gridOf(4))
        whenever(playerService.getPlayer(any(), any())).thenReturn(Player(board = board))

        val result = gameService.attack(123, 545, Coordinate(0, 0))

        assertThat(result).isSameAs(board)
    }
    // endregion

    // region getWinner
    @Test
    fun `getWinner requests active game`() {
        gameService.getWinner(123)

        verify(gameRegistry).getGame(123)
    }

    @Test
    fun `getWinner requests player`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 123))

        gameService.getWinner(489)

        verify(playerService).getPlayer(489, 123)
    }

    @Test
    fun `getWinner returns winner`() {
        val winner = Player(board = Board(gridOf(2)))
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 234))
        whenever(playerService.getPlayer(any(), any())).thenReturn(winner)

        assertThat(gameService.getWinner(0)).isEqualTo(winner)
    }

    @Test
    fun `getWinner returns null when there is no winner in the game`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = -1))

        val winner = gameService.getWinner(0)

        verify(playerService, never()).getPlayer(any(), any())
        assertThat(winner).isNull()
    }
    // endregion

    // region getActivePlayerId
    @Test
    fun `getActivePlayerId requests active game`() {
        gameService.getActivePlayerId(456)

        verify(gameRegistry).getGame(456)
    }

    @Test
    fun `getActivePlayerId returns active player id`() {
        val activePlayerId = 789
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(activePlayerId = activePlayerId))

        assertThat(gameService.getActivePlayerId(0)).isEqualTo(activePlayerId)
    }
    // endregion

    // region getDefendingBoard
    @Test
    fun `getDefendingBoard gets the defending player id`() {
        gameService.getDefendingBoard(123, 890)

        verify(gameRegistry).getDefendingPlayer(123, 890)
    }

    @Test
    fun `getDefendingBoard gets the defending player`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(20)

        gameService.getDefendingBoard(12, 90)

        verify(playerService).getPlayer(12, 20)
    }

    @Test
    fun `getDefendingBoard returns the defending board`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(10)
        whenever(playerService.getPlayer(any(), any())).thenReturn(Player(board = Board(gridOf(2))))

        val board = gameService.getDefendingBoard(0, 0)
        assertThat(board).isEqualTo(Board(gridOf(2)))
    }
    // endregion
}