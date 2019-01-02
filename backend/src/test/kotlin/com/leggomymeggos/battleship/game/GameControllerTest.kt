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
        val game = Game(Player())
        whenever(gameService.new()).thenReturn(game)

        val actual = controller.newGame()

        assertThat(actual).isEqualTo(game)
    }
    // endregion

    // region attack
    @Test
    fun `attackBoard mapping`() {
        mockMvc.perform(put("/games/0/attack?attackerId=98")
                .content("{\"x\": 123, \"y\": 456}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `attackBoard attempts to hit the game board`() {
        val coordinate = Coordinate(9, 10)
        controller.attackBoard(123, 456, coordinate)

        verify(gameService).attack(123, 456, coordinate)
    }

    @Test
    fun `attackBoard returns hit game board`() {
        val board = Board(gridOf(1))
        whenever(gameService.attack(any(), any(), any())).thenReturn(board)

        val actual = controller.attackBoard(0, 0, Coordinate(123, 456))

        assertThat(actual).isEqualTo(board)
    }
    // endregion

    // region fetchWinner
    @Test
    fun `fetchWinner mapping`() {
        mockMvc.perform(get("/games/0/winner"))
                .andExpect(status().isOk)
    }

    @Test
    fun `fetchWinner requests winner`() {
        controller.fetchWinner()

        verify(gameService).getWinner()
    }

    @Test
    fun `fetchWinner returns fetched winner`() {
        val player = Player(id = 123)
        whenever(gameService.getWinner()).thenReturn(player)

        assertThat(controller.fetchWinner()).isEqualTo(player)
    }
    // endregion

    // region fetchActivePlayer
    @Test
    fun `fetchActivePlayer mapping`() {
        mockMvc.perform(get("/games/0/players/active"))
                .andExpect(status().isOk)
    }

    @Test
    fun `fetchActivePlayer requests active player`() {
        controller.fetchActivePlayer()

        verify(gameService).getActivePlayerId()
    }

    @Test
    fun `fetchActivePlayer returns fetched winner`() {

        whenever(gameService.getActivePlayerId()).thenReturn(123)

        assertThat(controller.fetchActivePlayer()).isEqualTo(123)
    }
    // endregion
}