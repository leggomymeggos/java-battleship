package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.agent.PlayerService
import com.leggomymeggos.battleship.board.*
import org.springframework.stereotype.Service

@Service
class GameService(
        private val playerService: PlayerService,
        private val boardService: BoardService,
        private val boardPresenter: BoardPresenter,
        private val gameRegistry: GameRegistry
) {

    fun new(difficulty: Difficulty = Difficulty.EASY): Game {
        val newGame = GameEntity()
        val humanPlayerId = playerService.initPlayer()
        val computerPlayerId = playerService.initPlayer()

        boardService.initBoard(humanPlayerId, newGame.id).let { boardId ->
            boardService.randomlySetShips(boardId)
        }

        boardService.initBoard(computerPlayerId, newGame.id).let { boardId ->
            boardService.randomlySetShips(boardId)
        }

        val game = newGame.copy(
                playerIds = listOf(humanPlayerId, computerPlayerId),
                activePlayerId = humanPlayerId,
                difficulty = difficulty
        )
        gameRegistry.register(game)

        return Game(
                id = game.id,
                playerIds = listOf(humanPlayerId, computerPlayerId),
                activePlayerId = game.activePlayerId,
                winnerId = game.winnerId,
                difficulty = game.difficulty
        )
    }

    fun attack(gameId: Int, attackingPlayerId: Int, coordinate: Coordinate): BoardHitResponse {
        val game = gameRegistry.getGame(gameId)

        if (game.winnerId != -1) {
            return BoardHitResponse(HitResult.GameOver, null)
        }

        val defendingPlayerId = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)
        val boardId = boardService.getBoardIdForGameAndPlayer(gameId, defendingPlayerId)

        val attackResponse = defendingPlayerId
                .run {
                    boardService.hitCoordinate(boardId, coordinate)
                }

        if (attackResponse is HitResult.GameOver) {
            gameRegistry.setWinner(gameId, attackingPlayerId)
        } else {
            gameRegistry.changeTurn(gameId)
        }
        val board = boardPresenter.presentBoard(boardId)

        return BoardHitResponse(attackResponse, board)
    }

    fun getWinner(gameId: Int): GameOverStatus {
        val winnerId = gameRegistry.getGame(gameId).winnerId
        if (winnerId == -1) {
            return GameOverStatus.NoWinner
        }

        return GameOverStatus.Winner(winnerId)
    }

    fun getActivePlayerId(gameId: Int): Int {
        return gameRegistry.getGame(gameId).activePlayerId
    }

    fun getDefendingBoard(gameId: Int, attackingPlayerId: Int): Board {
        val playerId = gameRegistry.getDefendingPlayer(gameId, attackingPlayerId)
        return getBoardForGameAndPlayer(gameId, playerId)
    }

    fun getDifficulty(gameId: Int): Difficulty {
        return gameRegistry.getGame(gameId).difficulty
    }

    fun getBoardForGameAndPlayer(gameId: Int, playerId: Int): Board {
        val boardId = boardService.getBoardIdForGameAndPlayer(gameId, playerId)
        return boardPresenter.presentBoard(boardId)
    }
}