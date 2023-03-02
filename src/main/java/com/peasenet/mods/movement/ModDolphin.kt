/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mods.movement

import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

class ModDolphin : Mod(Type.DOLPHIN) {
    override fun onTick() {
        // check if the player is swimming
        if (client.player()!!.isTouchingWater) {
            val player = client.player()

            // if they are, move them up
            player.addVelocity(0.0, 0.1, 0.0)
            player.networkHandler.sendPacket(
                PlayerMoveC2SPacket.PositionAndOnGround(
                    player.x,
                    player.y + 0.1,
                    player.z,
                    false
                )
            )
        }
    }
}