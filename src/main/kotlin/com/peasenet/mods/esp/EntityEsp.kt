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

import com.mojang.blaze3d.systems.RenderSystem
import com.peasenet.gavui.color.Color
import com.peasenet.mods.misc.ModFreeCam
import com.peasenet.mods.misc.ModFreeCam.Companion
import com.peasenet.util.RegionPos
import com.peasenet.util.RenderUtils
import com.peasenet.util.RenderUtils.applyRegionalRenderOffset
import net.minecraft.client.gl.GlUsage
import net.minecraft.client.gl.ShaderProgramKeys
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import org.lwjgl.opengl.GL11


/**
 * A class that represents an ESP mod for entities.
 * @param T The type of entity to render.
 * @param name The name of the mod.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 *
 * @author GT3CH1
 * @version 09-01-2024
 * @since 09-01-2024
 *
 */
abstract class EntityEsp<T : Entity>(
    name: String, translationKey: String, chatCommand: String, val entityFilter: (Entity) -> Boolean
) : EspMod<T>(name, translationKey, chatCommand) {

    private lateinit var vertexBuffer: VertexBuffer
    override fun onEnable() {
        vertexBuffer = VertexBuffer(GlUsage.STATIC_WRITE)
        val bb = Box(-1.0, 0.0, -1.0, 1.0, 1.0, 1.0)
        RenderUtils.drawOutlinedBox(bb, vertexBuffer)
        super.onEnable()
    }

    @Suppress("UNCHECKED_CAST")
    override fun onTick() {
        super.onTick()
        espList.clear()
        client.getWorld().entities.filter { entityFilter(it) }.forEach { espList.add(it as T) }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (espList.isEmpty()) return

        val region = RenderUtils.getCameraRegionPos()
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        matrixStack.push()
        applyRegionalRenderOffset(matrixStack, region)
        for (e in espList) {

            matrixStack.push()
            val color = getColor(e)
            RenderSystem.setShaderColor(color.red, color.green, color.blue, config.alpha)
            matrixStack.push()
            val lerped = RenderUtils.getLerpedPos(e, partialTicks).subtract(region.toVec3d())
            matrixStack.translate(
                e.x + lerped.x, e.y + lerped.y, e.z + lerped.z
            );
            var bb = Box(-0.5, 0.0, -0.5, 0.5, 1.0, 0.5)
            RenderUtils.drawOutlinedBox(bb, matrixStack)

            matrixStack.pop()
            matrixStack.pop()
        }
        RenderUtils.cleanupRender(matrixStack)
    }


    /**
     * Gets the color of the entity for rendering.
     * @param entity The entity to get the color of.
     * @return The color of the entity.
     */
    abstract fun getColor(entity: T): Color
}