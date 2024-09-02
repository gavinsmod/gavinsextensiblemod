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
package com.peasenet.mods.esp

import com.peasenet.gui.mod.esp.GuiMobEsp
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderListener
import com.peasenet.util.math.MathUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper

/**
 * @author gt3ch1
 * @version 04-01-2023
 * A mod that allows the client to see boxes around mobs.
 */
class ModMobEsp : EspMod(
    "Mob ESP",
    "gavinsmod.mod.esp.mob",
    "mobesp"
), EntityRenderListener {
    init {
        val menu = SettingBuilder()
            .setTitle("gavinsmod.settings.mobesp")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiMobEsp()) }
            .buildClickSetting()
        addSetting(menu)
    }

    override fun onEnable() {
        super.onEnable()
        em.subscribe(EntityRenderListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(EntityRenderListener::class.java, this)
    }

    override fun onEntityRender(er: EntityRender) {
        var box = er.entity.boundingBox
//        box.union(box.offset(deltaMinX, deltaMinY, deltaMinZ)).union(box.offset(deltaMaxX, deltaMaxY, deltaMaxZ))
        if (er.entity !is MobEntity) return
        if (er.buffer == null) return
        val color = if (er.entityType.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
        if (config.mobIsShown(er.entityType)) {
            val x = MathHelper.lerp(er.delta, er.entity.lastRenderX.toFloat(), er.entity.x.toFloat()) - er.entity.x
            val y = MathHelper.lerp(er.delta, er.entity.lastRenderY.toFloat(), er.entity.y.toFloat()) - er.entity.y
            val z = MathHelper.lerp(er.delta, er.entity.lastRenderZ.toFloat(), er.entity.z.toFloat()) - er.entity.z
            box = Box(x + box.minX, y + box.minY, z + box.minZ, x + box.maxX, y + box.maxY, z + box.maxZ)
            RenderUtils.drawBox(er.stack, er.buffer, box, color, config.alpha)
        }
    }
}