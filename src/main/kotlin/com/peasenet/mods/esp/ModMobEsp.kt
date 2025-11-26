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
package com.peasenet.mods.esp

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.esp.GuiMobEsp
import com.peasenet.util.listeners.EntityRenderListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import org.joml.Matrix3x2fStack


/**
 * A mod that allows the client to see boxes around entityList.
 * @author GT3CH1
 * @version 01-15-2025
 * @since 04-01-2023
 * @see EntityRenderListener
 * @see EspMod
 */
class ModMobEsp : EntityEsp<Entity>("gavinsmod.mod.esp.mob",
    "mobesp",
    { it !is PlayerEntity && it.isLiving && !it.isRemoved && config.inList(it.type) }), RenderListener {


    init {
        clickSetting {
            title = "gavinsmod.settings.mobesp"
            callback = { MinecraftClient.getInstance().setScreen(GuiMobEsp()) }
        }
    }

    override fun getColor(entity: Entity): Color {
        return if (entity.type.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        val newList: MutableList<Entity> = ArrayList()
        if (config.showPeacefulMobs) {
            newList.addAll(espList.filter { it.type.spawnGroup.isPeaceful })
        }
        if (config.showHostileMobs) {
            newList.addAll(espList.filter { !it.type.spawnGroup.isPeaceful })
        }
        espList = newList
        super.onRender(matrixStack, partialTicks)
    }

    override fun getColor(): Color = Colors.BLUE
}