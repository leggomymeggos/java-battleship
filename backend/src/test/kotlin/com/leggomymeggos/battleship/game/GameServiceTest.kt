package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.agent.PlayerService
import com.leggomymeggos.battleship.board.*
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class GameServiceTest {

    private val playerService = mock<PlayerService> {
        on { initPlayer() } doReturn 10
    }

    private val gameRegistry = mock<GameRegistry> {
        on { getGame(any()) } doReturn GameEntity()
    }
    private val boardService = mock<BoardService> {
        on { initBoard(any(), any()) } doReturn 0
        on { getBoard(any()) } doReturn BoardEntity(0, 0, 0, setOf(), setOf())
        on { hitCoordinate(any(), any()) } doReturn HitResult.Miss
        on { getBoardIdForGameAndPlayer(any(), any()) } doReturn 0
        on { hitCoordinate(any(), any()) } doReturn HitResult.Miss
    }
    private val boardPresenter = mock<BoardPresenter> {
        on { presentBoard(any()) } doReturn Board()
    }

    private val subject = GameService(
            playerService = playerService,
            boardService = boardService,
            boardPresenter = boardPresenter,
            gameRegistry = gameRegistry
    )

    // region new
    @Test
    fun `new requests two players from playerService`() {
        subject.new()

        verify(playerService, times(2)).initPlayer()
    }

    @Test
    fun `new creates a board for each player`() {
        whenever(playerService.initPlayer())
                .thenReturn(1)
                .thenReturn(2)

        subject.new()

        verify(boardService).initBoard(eq(1), any())
        verify(boardService).initBoard(eq(2), any())
    }

    @Test
    fun `new randomly sets ships on each board`() {
        whenever(playerService.initPlayer())
                .thenReturn(1)
                .thenReturn(2)

        whenever(boardService.initBoard(any(), any()))
                .thenReturn(11)
                .thenReturn(22)

        subject.new()

        verify(boardService).randomlySetShips(11)
        verify(boardService).randomlySetShips(22)
    }

    @Test
    fun `new adds game to game registry`() {
        whenever(playerService.initPlayer())
                .thenReturn(1)
                .thenReturn(2)

        subject.new()

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.playerIds).isEqualTo(listOf(1, 2))
        assertThat(game.activePlayerId).isEqualTo(1)
    }

    @Test
    fun `new adds game with easy difficulty`() {
        subject.new(Difficulty.EASY)

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.difficulty).isEqualTo(Difficulty.EASY)
    }

    @Test
    fun `new adds game with hard difficulty`() {
        subject.new(Difficulty.HARD)

        val argumentCaptor = argumentCaptor<GameEntity>()
        verify(gameRegistry).register(argumentCaptor.capture())

        val game = argumentCaptor.firstValue

        assertThat(game.difficulty).isEqualTo(Difficulty.HARD)
    }

    @Test
    fun `new returns a game with player information and the first player active`() {
        whenever(playerService.initPlayer())
                .thenReturn(1)
                .thenReturn(3)

        val game = subject.new()

        assertThat(game.playerIds).isEqualTo(listOf(1, 3))
        assertThat(game.activePlayerId).isEqualTo(1)
    }

    // endregion

    // region attack
    @Test
    fun `attack determines defending player id`() {
        subject.attack(143, 123, Coordinate(0, 0))

        verify(gameRegistry).getDefendingPlayer(143, 123)
    }

    @Test
    fun `attack requests the active game`() {
        subject.attack(809, 0, Coordinate(0, 0))

        verify(gameRegistry).getGame(809)
    }

    @Test
    fun `attack returns gameOver response without a board if the active game has a winner`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(
                id = 1,
                playerIds = listOf(2, 3),
                activePlayerId = 2,
                winnerId = 3
        ))
        val response = subject.attack(123, 2, Coordinate(0, 0))

        assertThat(response).isEqualTo(BoardHitResponse(HitResult.GameOver, null))
        verify(gameRegistry, never()).changeTurn(any())
        verify(boardService, never()).hitCoordinate(any(), any())
        verify(gameRegistry, never()).setWinner(any(), any())
    }

    @Test
    fun `attack hits board of defending player`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)

        val coordinate = Coordinate(1, 2)
        subject.attack(123, 2, coordinate)

        verify(gameRegistry).getDefendingPlayer(123, 2)
        verify(boardService).getBoardIdForGameAndPlayer(123, 3)
        verify(boardService).hitCoordinate(5, coordinate)
    }

    @Test
    fun `attack sets attacking player as the winner if the hit coordinate ends the game`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.GameOver)

        val attackingPlayerId = 2
        val gameId = 123

        val coordinate = Coordinate(1, 2)
        subject.attack(gameId, attackingPlayerId, coordinate)

        verify(gameRegistry).setWinner(gameId, attackingPlayerId)
    }

    @Test
    fun `attack returns gameOver with the hit board if the hit coordinate ends the game`() {
        val hitBoard = Board(gridOf(4))

        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.GameOver)
        whenever(boardPresenter.presentBoard(any())).doReturn(hitBoard)

        val attackingPlayerId = 2
        val gameId = 123

        val coordinate = Coordinate(1, 2)
        val response = subject.attack(gameId, attackingPlayerId, coordinate)

        verify(boardPresenter).presentBoard(5)
        assertThat(response).isEqualTo(BoardHitResponse(HitResult.GameOver, hitBoard))
    }

    @Test
    fun `attack changes the turn when the hit coordinate is a miss`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.Miss)

        val gameId = 123

        val coordinate = Coordinate(1, 2)
        subject.attack(gameId, 2, coordinate)

        verify(gameRegistry, never()).setWinner(any(), any())
        verify(gameRegistry).changeTurn(gameId)
    }

    @Test
    fun `attack returns the board hit result with the presented board when the hit coordinate is a miss`() {
        val hitBoard = Board(gridOf(4))

        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.Miss)
        whenever(boardPresenter.presentBoard(any())).doReturn(hitBoard)

        val gameId = 123
        val coordinate = Coordinate(1, 2)

        val response = subject.attack(gameId, 2, coordinate)

        verify(boardPresenter).presentBoard(5)
        assertThat(response).isEqualTo(BoardHitResponse(HitResult.Miss, hitBoard))
    }

    @Test
    fun `attack changes the turn when the hit coordinate is a hit`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.Hit)

        val gameId = 123

        val coordinate = Coordinate(1, 2)
        subject.attack(gameId, 2, coordinate)

        verify(gameRegistry, never()).setWinner(any(), any())
        verify(gameRegistry).changeTurn(gameId)
    }


    @Test
    fun `attack returns the board hit result with the presented board when the hit coordinate is a hit`() {
        val hitBoard = Board(gridOf(4))

        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(3)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(5)
        whenever(boardService.hitCoordinate(any(), any())).thenReturn(HitResult.Hit)
        whenever(boardPresenter.presentBoard(any())).doReturn(hitBoard)

        val gameId = 123
        val coordinate = Coordinate(1, 2)

        val response = subject.attack(gameId, 2, coordinate)

        verify(boardPresenter).presentBoard(5)
        assertThat(response).isEqualTo(BoardHitResponse(HitResult.Hit, hitBoard))
    }
    // endregion

    // region getWinner
    @Test
    fun `getWinner gets the requested game`() {
        subject.getWinner(123)

        verify(gameRegistry).getGame(123)
    }

    @Test
    fun `getWinner returns Winner if the game has a winnerId`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = 123))

        val response = subject.getWinner(589)

        assertThat(response).isEqualTo(GameOverStatus.Winner(123))
    }

    @Test
    fun `getWinner returns NoWinner if the game does not have a winnerId`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(winnerId = -1))

        val response = subject.getWinner(589)

        assertThat(response).isEqualTo(GameOverStatus.NoWinner)
    }
    // endregion

    // region getActivePlayerId
    @Test
    fun `getActivePlayerId requests active game`() {
        subject.getActivePlayerId(456)

        verify(gameRegistry).getGame(456)
    }

    @Test
    fun `getActivePlayerId returns active player id`() {
        val activePlayerId = 789
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(activePlayerId = activePlayerId))

        assertThat(subject.getActivePlayerId(0)).isEqualTo(activePlayerId)
    }
    // endregion

    // region getDefendingBoard
    @Test
    fun `getDefendingBoard gets the defending player id`() {
        subject.getDefendingBoard(123, 890)

        verify(gameRegistry).getDefendingPlayer(123, 890)
    }

    @Test
    fun `getDefendingBoard gets and presents the board for the defending player`() {
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(40)
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(20)

        subject.getDefendingBoard(12, 90)

        verify(boardService).getBoardIdForGameAndPlayer(12, 20)
        verify(boardPresenter).presentBoard(40)
    }

    @Test
    fun `getDefendingBoard returns the defending board`() {
        whenever(gameRegistry.getDefendingPlayer(any(), any())).thenReturn(10)
        whenever(boardService.getBoardIdForGameAndPlayer(any(), any())).thenReturn(40)
        val board = Board(gridOf(10))
        whenever(boardPresenter.presentBoard(any())).thenReturn(board)

        val result = subject.getDefendingBoard(0, 0)
        assertThat(result).isEqualTo(board)
    }
    // endregion

    // region getDifficulty
    @Test
    fun `getDifficulty gets game`() {
        subject.getDifficulty(123)

        verify(gameRegistry).getGame(123)
    }

    @Test
    fun `getDifficulty returns game difficulty`() {
        whenever(gameRegistry.getGame(any())).thenReturn(GameEntity(difficulty = Difficulty.HARD))

        val difficulty = subject.getDifficulty(0)

        assertThat(difficulty).isEqualTo(Difficulty.HARD)
    }
    // endregion
}