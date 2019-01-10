package com.leggomymeggos.battleship.agent

import com.leggomymeggos.battleship.board.Board
import com.leggomymeggos.battleship.board.gridOf
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ComputerAgentControllerTest {

    private lateinit var controller: ComputerAgentController
    private lateinit var computerAgentService: ComputerAgentService
    private lateinit var mockMvc: MockMvc

    @Before
    fun setup() {
        computerAgentService = mock()
        controller = ComputerAgentController(computerAgentService)
        mockMvc = standaloneSetup(controller).build()
    }

    // region attack
    @Test
    fun `attack mapping`() {
        mockMvc.perform(get("/games/0/enemy-attack?attackerId=0"))
                .andExpect(status().isOk)
    }

    @Test
    fun `attack takes the computer's turn`() {
        controller.attack(123, 456)

        verify(computerAgentService).takeTurn(123, 456)
    }

    @Test
    fun `attack returns the new board`() {
        val board = Board(grid = gridOf(2))
        whenever(computerAgentService.takeTurn(any(), any())).thenReturn(board)

        val result = controller.attack(0, 0)

        assertThat(result).isEqualTo(board)
    }
    // endregion
}