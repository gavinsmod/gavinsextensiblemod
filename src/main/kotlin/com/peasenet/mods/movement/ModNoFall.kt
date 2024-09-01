/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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
package com.peasenet.mods.movement

import com.peasenet.main.GavinsModClient
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that prevents the player from taking fall damage.
 */
class ModNoFall : MovementMod(
    "No Fall",
    "gavinsmod.mod.movement.nofall",
    "nofall"
) {
    override fun onTick() {
        val player = GavinsModClient.player
        if (player != null && isActive) {
            if (player.getFallDistance() <= (if (isFalling) 1 else 2)) return
            if (player.isSneaking() && !fallSpeedCanDamage && player.isFallFlying()) return
            player.getNetworkHandler().sendPacket(PlayerMoveC2SPacket.OnGroundOnly(true))
        }
    }

    /**
     *
     * Whether the player is falling.
     *
     * @return True if the player is falling, false otherwise.
     */
    private val isFalling: Boolean
        get() {
            val player = GavinsModClient.player
            return player!!.isFallFlying()
        }

    /**
     * Whether the player can be damaged by the current fall speed.
     *
     * @return True if the player can be damaged, false otherwise.
     */
    private val fallSpeedCanDamage: Boolean
        get() {
            val player = GavinsModClient.player
            return player!!.getVelocity().y < -0.5
        }

}