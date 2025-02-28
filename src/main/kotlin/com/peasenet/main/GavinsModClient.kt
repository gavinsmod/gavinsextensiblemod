/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.peasenet.main

import com.peasenet.mixinterface.IClientPlayerEntity
import com.peasenet.mixinterface.IMinecraftClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

/**
 * @author GT3CH1
 * @version 04-11-2023
 * The main part of the mod that handles checking mods.
 */
class GavinsModClient : ClientModInitializer {
    override fun onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized")
        ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick {
            if (player == null) return@StartTick
            for (m in Mods.mods) {
                m.checkKeybinding()
                if (m.isActive || m.isDeactivating) m.onTick()
            }
        })
    }


    companion object {
        /**
         * Gets the minecraft client.
         * @see IMinecraftClient
         * @return The minecraft client.
         */
        val minecraftClient: IMinecraftClient
            get() = MinecraftClient.getInstance() as IMinecraftClient

        /**
         * Gets the minecraft client player.
         * @see IClientPlayerEntity
         * @return The minecraft client player.
         */
        val player: IClientPlayerEntity?
            get() = MinecraftClient.getInstance().player as IClientPlayerEntity?
    }
}