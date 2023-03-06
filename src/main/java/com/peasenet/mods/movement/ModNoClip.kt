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
import com.peasenet.util.event.AirStrafeEvent
import com.peasenet.util.listeners.AirStrafeListener

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod for allowing the printer to noclip (move through blocks)
 */
class ModNoClip : Mod(Type.NO_CLIP), AirStrafeListener {
    override fun onEnable() {
        em.subscribe(AirStrafeListener::class.java, this)
        super.onEnable()
    }

    override fun onDisable() {
        em.unsubscribe(AirStrafeListener::class.java, this)
        super.onDisable()
    }

    override fun onTick() {
        if (client.player() == null) return
        val player = client.player()
        player.abilities.flying = true
    }

    override fun onAirStrafe(event: AirStrafeEvent) {
        var speed = 0.2f
        if (client.player() == null) return
        val player = client.player()
        if (player.isSprinting) {
            speed = 1f
        }
        event.speed = speed
    }
}