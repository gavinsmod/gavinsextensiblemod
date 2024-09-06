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

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.esp.GuiMobEsp
import com.peasenet.settings.SettingBuilder
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.EntityRenderListener
import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d


/**
 * A mod that allows the client to see boxes around entityList.
 * @author gt3ch1
 * @version 09-02-2024
 * @since 04-01-2023
 * @see EntityRenderListener
 * @see EspMod
 */
class ModMobEsp : EntityEsp<Entity>("Mob ESP",
    "gavinsmod.mod.esp.mob",
    "mobesp",
    { it !is PlayerEntity && it.isLiving && !it.isRemoved && config.mobIsShown(it.type) }), RenderListener {


    init {
        val menu = SettingBuilder().setTitle("gavinsmod.settings.mobesp")
            .setCallback { MinecraftClient.getInstance().setScreen(GuiMobEsp()) }.buildClickSetting()
        addSetting(menu)
    }

    override fun getColor(entity: Entity): Color {
        return if (entity.type.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
    }

    override fun getColor(): Color = Colors.BLUE

//    override fun onTick() {
//        super.onTick()
//        espList.clear()
//        espList.addAll(client.getWorld().entities.filter { e -> e.isLiving }.filter { e -> e !is PlayerEntity }
//            .filter { e -> !e.isRemoved }.filter { e -> config.mobIsShown(e.type) })
//    }

//    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
//        if (espList.isEmpty())
//            return;
//        RenderUtils.setupRender(matrixStack)
//
//        espList.forEach { e ->
//            matrixStack.push()
//            val box = e.boundingBox
//            val x = MathHelper.lerp(partialTicks, e.lastRenderX.toFloat(), e.x.toFloat()) - e.x
//            val y = MathHelper.lerp(partialTicks, e.lastRenderY.toFloat(), e.y.toFloat()) - e.y
//            val z = MathHelper.lerp(partialTicks, e.lastRenderZ.toFloat(), e.z.toFloat()) - e.z
//
//            val lerpedPos = Vec3d(x, y, z).subtract(RenderUtils.getCameraRegionPos().toVec3d())
//            matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z)
//            val color = if (e.type.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
//            val box2 = Box(x + box.minX, y + box.minY, z + box.minZ, x + box.maxX, y + box.maxY, z + box.maxZ)
//            RenderUtils.drawOutlinedBox(
//                box2,
//                vertexBuffer!!,
//                matrixStack,
//                color,
//                config.alpha
//            )
//            matrixStack.pop()
//        }
//        RenderUtils.cleanupRender(matrixStack)
//    }
}