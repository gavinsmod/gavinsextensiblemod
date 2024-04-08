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
package com.peasenet.mods.combat

import com.peasenet.main.GavinsModClient
import com.peasenet.util.listeners.PlayerAttackListener
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A combat mod to make the player jump automatically when attacking an entity.
 */
class ModAutoCrit : CombatMod(
    "Auto Crit",
    "gavinsmod.mod.combat.autocrit",
    "autocrit",
), PlayerAttackListener {
    override fun onEnable() {
        super.onEnable()
        em.subscribe(PlayerAttackListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(PlayerAttackListener::class.java, this)
    }

    override fun onAttackEntity() {
        if (GavinsModClient.player == null)
            return
        val player = GavinsModClient.player!!.getPos()
        val x = player.x
        val y = player.y
        val z = player.z
        sendPos(x, y + 0.0625, z, true)
        sendPos(x, y, z, false)
        sendPos(x, y + 1.1E-5, z, false)
        sendPos(x, y, z, false)
    }

    private fun sendPos(x: Double, y: Double, z: Double, onGround: Boolean) {
        val player = GavinsModClient.player

        player!!.getNetworkHandler().sendPacket(PositionAndOnGround(x, y, z, onGround))
    }
}