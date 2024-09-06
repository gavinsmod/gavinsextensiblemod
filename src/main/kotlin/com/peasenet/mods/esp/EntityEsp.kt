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
import com.peasenet.mods.tracer.TracerMod
import com.peasenet.mods.tracer.TracerMod.Companion
import com.peasenet.util.RenderUtils
import com.peasenet.util.math.MathUtils
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d


abstract class EntityEsp<T : Entity>(
    name: String, translationKey: String, chatCommand: String, val entityFilter: (Entity) -> Boolean
) : EspMod<T>(name, translationKey, chatCommand) {

    override fun onTick() {
        super.onTick()
        espList.clear()
        client.getWorld().entities.filter { entityFilter(it) }.forEach { espList.add(it as T) }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (espList.isEmpty()) return
        RenderUtils.setupRender(matrixStack)
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.applyModelViewMatrix()
        val region = RenderUtils.getCameraRegionPos()
        val entry = matrixStack.peek().positionMatrix
        val tessellator = RenderSystem.renderThreadTesselator()
        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        val regionVec = region.toVec3d();
        val start = RenderUtils.getLookVec(partialTicks).add(RenderUtils.getCameraPos()).subtract(regionVec);

            val box = e.boundingBox
            val x = MathHelper.lerp(partialTicks, e.lastRenderX.toFloat(), e.x.toFloat()) - e.x
            val y = MathHelper.lerp(partialTicks, e.lastRenderY.toFloat(), e.y.toFloat()) - e.y
            val z = MathHelper.lerp(partialTicks, e.lastRenderZ.toFloat(), e.z.toFloat()) - e.z

            val lerpedPos = MathUtils.lerp(
                partialTicks,
                e.pos,
                Vec3d(e.lastRenderX, e.lastRenderY, e.lastRenderZ),
                RenderUtils.getCameraRegionPos()
            )
            matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z)
            val color = if (e.type.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
            val box2 = Box(x + box.minX, y + box.minY, z + box.minZ, x + box.maxX, y + box.maxY, z + box.maxZ)


        for (e in espList) {
            val center = e.boundingBox.center.subtract(region.toVec3d())
            RenderUtils.drawOutlinedBox(
                box, vertexBuffer!!, entry, getColor(e), config.alpha
            )
        }
        val end = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(end)
        RenderUtils.cleanupRender(matrixStack)
    }

//    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
//        if (espList.isEmpty()) return;
//        RenderUtils.setupRender(matrixStack)
//
//        val tessellator = RenderSystem.renderThreadTesselator()
//        val bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
//        espList.forEach { e ->
//            matrixStack.push()
//
//            val entry = matrixStack.peek().positionMatrix
//            val box = e.boundingBox
//            val x = MathHelper.lerp(partialTicks, e.lastRenderX.toFloat(), e.x.toFloat()) - e.x
//            val y = MathHelper.lerp(partialTicks, e.lastRenderY.toFloat(), e.y.toFloat()) - e.y
//            val z = MathHelper.lerp(partialTicks, e.lastRenderZ.toFloat(), e.z.toFloat()) - e.z
//
//            val lerpedPos = MathUtils.lerp(
//                partialTicks,
//                e.pos,
//                Vec3d(e.lastRenderX, e.lastRenderY, e.lastRenderZ),
//                RenderUtils.getCameraRegionPos()
//            )
//            matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z)
//            val color = if (e.type.spawnGroup.isPeaceful) config.peacefulMobColor else config.hostileMobColor
//            val box2 = Box(x + box.minX, y + box.minY, z + box.minZ, x + box.maxX, y + box.maxY, z + box.maxZ)
//            RenderUtils.drawOutlinedBox(
//                box2, bufferBuilder, entry, color, config.alpha
//            )
//            matrixStack.pop()
//        }
//
//        val end = bufferBuilder.end()
//        BufferRenderer.drawWithGlobalProgram(end)
//        RenderUtils.cleanupRender(matrixStack)
//    }

    abstract fun getColor(entity: T): Color
}