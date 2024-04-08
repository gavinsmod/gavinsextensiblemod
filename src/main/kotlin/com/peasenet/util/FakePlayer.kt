/*
 * MIT License
 *
 * Copyright (c) 2022-2024.
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
package com.peasenet.util

import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.main.GavinsModClient.Companion.player
import net.minecraft.client.network.OtherClientPlayerEntity

/**
 * A fake player entity that can be used to render a player model in the getWorld().
 * @author gt3ch1
 * @version 03-02-2023
 */
class FakePlayer : OtherClientPlayerEntity(minecraftClient.getWorld(), player!!.getGameProfile()) {
    /**
     * The minecraft world
     */
    private val world = minecraftClient.getWorld()

    /**
     * The minecraft player
     */
    private val player = minecraftClient.getPlayer()

    /**
     * Creates a fake player entity in the getWorld().
     */
    init {
        copyPositionAndRotation(player)
        inventory.clone(player.inventory)
        val fromTracker = player.dataTracker
        val toTracker = getDataTracker()
        val playerModel = fromTracker.get(PLAYER_MODEL_PARTS)
        toTracker.set(PLAYER_MODEL_PARTS, playerModel)
        headYaw = player.getHeadYaw()
        bodyYaw = player.getBodyYaw()
        inventory.clone(player.inventory)
        this.id = player.id
        minecraftClient.getWorld().addEntity(this)
    }

    /**
     * Removes the fake player from the getWorld().
     */
    fun remove() {
        // move player back to original position
        player.refreshPositionAndAngles(x, y, z, yaw, pitch)
        discard()
    }
}