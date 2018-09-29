package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.Board
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        val game = Game(Board())
        whenever(gameService.new()).thenReturn(game)

        val actual = controller.newGame()

        assertThat(actual).isEqualTo(game)
    }
}