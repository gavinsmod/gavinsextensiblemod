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

import com.peasenet.config.BlockEspConfig
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.mods.ModCategory
import com.peasenet.mods.commons.BlockEspTracerCommon
import com.peasenet.util.RenderUtils
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.Box

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 09-01-2024
 * @since 09-01-2024
 * @see EspMod
 *
 */
class ModBlockEsp : BlockEspTracerCommon<BlockEspConfig>("Block ESP",
    "gavinsmod.mod.esp.blockesp",
    "blockesp",
    ModCategory.ESP,
    { minecraftClient.setScreen(GuiBlockEsp()) }) {

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(chunks) {
            for (chunk in chunks.values.filter { it.inRenderDistance() }) {
                for (block in chunk.blocks.values) {
                    val box = Box(
                        block.x.toDouble(),
                        block.y.toDouble(),
                        block.z.toDouble(),
                        block.x + 1.0,
                        block.y + 1.0,
                        block.z + 1.0
                    )
                    RenderUtils.drawBox(stack, bufferBuilder, box, getSettings().blockColor, getSettings().alpha)
                }
            }
            // clear out all chunks that are not in render distance
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }
}