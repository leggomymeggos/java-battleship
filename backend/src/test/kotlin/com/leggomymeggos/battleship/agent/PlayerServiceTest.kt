package com.leggomymeggos.battleship.agent

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlayerServiceTest {
    private val playerRegistry = mock<PlayerRegistry>()
    private val subject = PlayerService(playerRegistry)

    @Test
    fun `initPlayer registers a new player and returns the new player id`() {
        val result = subject.initPlayer()

        argumentCaptor<PlayerEntity>().let {
            verify(playerRegistry).register(it.capture())
             assertThat(result).isEqualTo(it.firstValue.id)
        }
    }
}