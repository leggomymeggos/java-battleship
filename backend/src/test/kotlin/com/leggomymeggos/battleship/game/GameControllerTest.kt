package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.Coordinate
import com.leggomymeggos.battleship.board.gridOf
import com.leggomymeggos.battleship.player.Player
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class GameControllerTest {

    private lateinit var controller: GameController
    private lateinit var gameService: GameService
    private lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        gameService = mock()
        controller = GameController(gameService)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    // region newGame
    @Test
    fun `newGame mapping`() {
        mockMvc.perform(get("/games/new"))
                .andExpect(status().isOk)
    }

    @Test
    fun `newGame requests a new game`() {
        controller.newGame()

        verify(gameService).new()
    }

    @Test
    fun `newGame returns a new game`() {
        val game = Game(Player(Board()))
        whenever(gameService.new()).thenReturn(game)

        val actual = controller.newGame()

        assertThat(actual).isEqualTo(game)
    }
    // endregion

    // region hitBoard
    @Test
    fun `hitBoard mapping`() {
        mockMvc.perform(put("/games/0/players/0/hit")
                .content("{\"x\": 123, \"y\": 456}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `hitBoard attempts to hit the game board`() {
        val coordinate = Coordinate(9, 10)
        controller.hitBoard(coordinate)

        verify(gameService).hitBoard(coordinate)
    }

    @Test
    fun `hitBoard returns hit game board`() {
        val board = Board(gridOf(1))
        whenever(gameService.hitBoard(any())).thenReturn(board)

        val actual = controller.hitBoard(Coordinate(123, 456))

        assertThat(actual).isEqualTo(board)
    }
    // endregion

    // region fetchWinner
    @Test
    fun `fetchWinner mapping`() {
        mockMvc.perform(get("games/0/winner"))
                .andExpect(status().isOk)
    }

    @Test
    fun `fetchWinner requests winner`() {
        controller.fetchWinner()

        verify(gameService).getWinner()
    }

    @Test
    fun `fetchWinner returns fetched winner`() {
        whenever(gameService.getWinner()).thenReturn(true)

        assertThat(controller.fetchWinner()).isTrue()
    }
    // endregion
}