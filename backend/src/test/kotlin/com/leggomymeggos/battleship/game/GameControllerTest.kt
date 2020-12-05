package com.leggomymeggos.battleship.game

import com.leggomymeggos.battleship.board.*
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class GameControllerTest {

    private val gameService = mock<GameService>()
    private val subject = GameController(gameService)
    private val mockMvc = standaloneSetup(subject).build()

    // region newGame
    @Test
    fun `newGame mapping`() {
        mockMvc.perform(get("/api/games/new"))
                .andExpect(status().isOk)
    }

    @Test
    fun `newGame requests a new game`() {
        subject.newGame()

        verify(gameService).new()
    }

    @Test
    fun `newGame returns a new game`() {
        val game = Game(id = 0, playerIds = listOf(1, 2), activePlayerId = 1)
        whenever(gameService.new()).thenReturn(game)

        val actual = subject.newGame()

        assertThat(actual).isEqualTo(game)
    }
    // endregion

    // region attack
    @Test
    fun `attackBoard mapping`() {
        mockMvc.perform(put("/api/games/0/attack?attackerId=98")
                .content("{\"column\": 123, \"row\": 456}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful)
    }

    @Test
    fun `attackBoard attempts to hit the game board`() {
        val coordinate = Coordinate(9, 10)
        subject.attackBoard(123, 456, coordinate)

        verify(gameService).attack(123, 456, coordinate)
    }

    @Test
    fun `attackBoard returns hit game board`() {
        val response = BoardHitResponse(HitResult.Sunk(Ship.SUBMARINE), Board(gridOf(1)))
        whenever(gameService.attack(any(), any(), any())).thenReturn(response)

        val actual = subject.attackBoard(0, 0, Coordinate(123, 456))

        assertThat(actual).isEqualTo(response)
    }
    // endregion

    // region fetchWinner
    @Test
    fun `fetchWinner mapping`() {
        mockMvc.perform(get("/api/games/0/winner"))
                .andExpect(status().isOk)
    }

    @Test
    fun `fetchWinner requests winner`() {
        subject.fetchWinner(123)

        verify(gameService).getWinner(123)
    }

    @Test
    fun `fetchWinner returns fetched winner`() {
        whenever(gameService.getWinner(201)).thenReturn(GameOverStatus.Winner(123))

        assertThat(subject.fetchWinner(201)).isEqualTo(GameOverStatus.Winner(123))
    }
    // endregion

    // region fetchActivePlayer
    @Test
    fun `fetchActivePlayer mapping`() {
        mockMvc.perform(get("/api/games/0/players/active"))
                .andExpect(status().isOk)
    }

    @Test
    fun `fetchActivePlayer requests active player`() {
        subject.fetchActivePlayer(10)

        verify(gameService).getActivePlayerId(10)
    }

    @Test
    fun `fetchActivePlayer returns fetched winner`() {

        whenever(gameService.getActivePlayerId(44)).thenReturn(123)

        assertThat(subject.fetchActivePlayer(44)).isEqualTo(123)
    }
    // endregion
}