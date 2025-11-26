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

@file:Suppress("UNCHECKED_CAST")

package com.peasenet.mods.esp

import com.peasenet.util.RenderUtils
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box
import org.joml.Matrix3x2fStack

/**
 * A class that represents an ESP mod for block entities.
 * @param T The type of block entity to render.
 * @param translationKey The translation key of the mod.
 * @param chatCommand The chat command of the mod.
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 09-01-2024
 *
 */
abstract class BlockEntityEsp<T : BlockEntity>(
    translationKey: String, chatCommand: String, val filter: (BlockEntity) -> Boolean,
) : EspMod<T>(
    translationKey, chatCommand
) {

    override fun onTick() {
        super.onTick()
        espList.clear()
        val level = MinecraftClient.getInstance().world
        for (x in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
            for (z in -RenderUtils.CHUNK_RADIUS..RenderUtils.CHUNK_RADIUS) {
                val chunk = level!!.getChunk(x + client.getPlayer().chunkPos.x, z + client.getPlayer().chunkPos.z)
                for ((_, blockEntity) in chunk.blockEntities.filter { filter(it.value) }) {
                    if (espList.size > 100) return
                    espList.add(blockEntity as T)
                }
            }
        }
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (espList.isEmpty()) return
        matrixStack.push()
        val scale = config.espSize / 0.25f
        for (e in espList) {
            val pos = e.pos.toCenterPos()
            val bb = Box(
                pos.x + 0.5 * scale,
                pos.y + 0.5 * scale,
                pos.z + 0.5 * scale,
                pos.x - 0.5 * scale,
                pos.y - 0.5 * scale,
                pos.z - 0.5 * scale
            )
//            RenderUtils.drawOutlinedBox(
//                bb,
//                matrixStack,
//                getColor(),
//                getAlpha(),
//                false
//            )
        }
        matrixStack.pop()
    }
}