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
package com.peasenet.mods.tracer

import com.peasenet.config.TracerConfig
import com.peasenet.gui.mod.tracer.GuiMobTracer
import com.peasenet.main.Settings
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.BlockEntityRender
import com.peasenet.util.event.data.EntityRender
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity

/**
 * @author gt3ch1
 * @version 04-01-2023
 * A mod that allows the client to see lines, called tracers, towards mobs.
 */
class ModMobTracer : TracerMod(
    "Mob Tracer",
    "gavinsmod.mod.tracer.mob",
    "mobtracer"
) {
    init {
        val menu = SettingBuilder()
            .setTitle("gavinsmod.settings.mobtracer")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiMobTracer()) }
            .buildClickSetting()
        addSetting(menu)
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
        val color =
            if (er.entityType.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
        if (config.mobIsShown(entity.type))
            RenderUtils.renderSingleLine(
                stack, buffer!!, playerPos!!, center!!, color, config.alpha
            )
    }


    override fun onRenderBlockEntity(er: BlockEntityRender) {
    }

    companion object {
        val config: TracerConfig
            get() {
                return Settings.getConfig<TracerConfig>("tracer")
            }
    }
}
