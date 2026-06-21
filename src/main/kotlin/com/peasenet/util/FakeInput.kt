package com.peasenet.util

import com.peasenet.main.GavinsModClient
import com.peasenet.main.Mods
import net.minecraft.client.player.ClientInput

/**
 *
 * @author GT3CH1
 * @version 06-21-2026
 * @since 06-21-2026 
 */
class FakeInput {
    companion object {
        private var swapped = false
        private var storedInput: ClientInput? = null
        fun swap() {
            if (!Mods.isActive(ChatCommand.FreeCam))
                return;
            if (swapped)
                return;
            swapped = true;
            val player = GavinsModClient.minecraftClient.getPlayer()
            storedInput = player.input
            player.input.tick()
            player.input = ClientInput()
        }

        fun unswap() {
            if (!swapped || storedInput == null)
                return;
            swapped = false;
            val player = GavinsModClient.minecraftClient.getPlayer()
            player.input = storedInput!!
            storedInput = null
        }


    }
}