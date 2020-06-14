package com.leggomymeggos.battleship.agent

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PlayerRegistryTest {
    private val subject = PlayerRegistry()

    @Test
    fun `register keeps track of a player`() {
        subject.register(PlayerEntity(id = 123))

        assertThat(subject.players.find { it.id == 123 }).isEqualTo(PlayerEntity(id = 123))
    }
}