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
package com.peasenet.mods.tracer

import com.peasenet.main.GavinsMod
import com.peasenet.mods.Mod
import com.peasenet.mods.Type
import com.peasenet.settings.ColorSetting
import com.peasenet.settings.ToggleSetting
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.CameraBob
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.CameraBobListener
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.entity.mob.MobEntity

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mod that allows the client to see lines, called tracers, towards mobs.
 */
class ModMobTracer : Mod(Type.MOB_TRACER), EntityRenderListener, CameraBobListener {
    init {
        val peacefulColor = ColorSetting(
            "gavinsmod.settings.tracer.mob.peaceful.color", GavinsMod.tracerConfig!!.peacefulMobColor
        )
        peacefulColor.setCallback { tracerConfig!!.peacefulMobColor = peacefulColor.color }
        peacefulColor.color = GavinsMod.tracerConfig!!.peacefulMobColor
        val hostileColor = ColorSetting(
            "gavinsmod.settings.tracer.mob.hostile.color", GavinsMod.tracerConfig!!.hostileMobColor
        )
        hostileColor.setCallback { tracerConfig!!.hostileMobColor = hostileColor.color }
        hostileColor.color = GavinsMod.tracerConfig!!.hostileMobColor
        val hostile = ToggleSetting("gavinsmod.settings.tracer.mob.hostile")
        hostile.setCallback { tracerConfig!!.showHostileMobs = hostile.value }
        hostile.value = GavinsMod.tracerConfig!!.showHostileMobs
        val peaceful = ToggleSetting("gavinsmod.settings.tracer.mob.peaceful")
        peaceful.setCallback { tracerConfig!!.showPeacefulMobs = peaceful.value }
        peaceful.value = GavinsMod.tracerConfig!!.showPeacefulMobs
        addSetting(hostileColor)
        addSetting(peacefulColor)
        addSetting(hostile)
        addSetting(peaceful)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderListener::class.java, this)
        em.subscribe(CameraBobListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderListener::class.java, this)
        em.unsubscribe(CameraBobListener::class.java, this)
    }

    override fun onEntityRender(er: EntityRender) {
        if (er.buffer == null) return
        // check if entity is a mob
        val entity = er.entity
        val stack = er.stack
        val buffer = er.buffer
        val center = er.center
        val playerPos = er.playerPos
        if (entity !is MobEntity) return
        if (er.entityType.spawnGroup.isPeaceful && tracerConfig!!.showPeacefulMobs) {
            RenderUtils.renderSingleLine(
                stack, buffer!!, playerPos!!, center!!, tracerConfig!!.peacefulMobColor, tracerConfig!!.alpha
            )
        } else if (!er.entityType.spawnGroup.isPeaceful && tracerConfig!!.showHostileMobs) {
            RenderUtils.renderSingleLine(
                stack, buffer!!, playerPos!!, center!!, tracerConfig!!.hostileMobColor, tracerConfig!!.alpha
            )
        }
    }

    override fun onCameraViewBob(c: CameraBob) {
        c.cancel()
    }
}