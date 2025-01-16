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
package com.peasenet.mods.tracer

import com.peasenet.gavui.color.Color
import com.peasenet.gui.mod.tracer.GuiMobTracer
import com.peasenet.settings.SettingBuilder
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity

/**
 * A mod that allows the client to see lines, called tracers, towards mobs.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-01-2023
 */
class ModMobTracer : EntityTracer<LivingEntity>(
    "gavinsmod.mod.tracer.mob",
    "mobtracer",
    { it is MobEntity && config.inList(it.type) }
) {
    init {
        val menu = SettingBuilder()
            .setTitle("gavinsmod.settings.mobtracer")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiMobTracer()) }
            .buildClickSetting()
        addSetting(menu)
    }

    override fun getColor(entity: LivingEntity): Color {
        if (entity.type.spawnGroup.isPeaceful)
            return config.peacefulMobColor
        return config.hostileMobColor
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        val newList: MutableList<LivingEntity> = ArrayList()
        if (config.showPeacefulMobs) {
            newList.addAll(entityList.filter { it.type.spawnGroup.isPeaceful })
        }
        if (config.showHostileMobs) {
            newList.addAll(entityList.filter { !it.type.spawnGroup.isPeaceful })
        }
        entityList = newList
        super.onRender(matrixStack, partialTicks)
    }
}
