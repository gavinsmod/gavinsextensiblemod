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
import com.peasenet.config.BlockEspConfig
import com.peasenet.gui.mod.esp.GuiBlockEsp
import com.peasenet.main.GavinsModClient.Companion.minecraftClient
import com.peasenet.mods.ModCategory
import com.peasenet.mods.commons.BlockEspTracerCommon
import com.peasenet.util.GavBlock
import com.peasenet.util.GavChunk
import com.peasenet.util.RenderUtils
import com.peasenet.util.executor.GemExecutor
import com.peasenet.util.listeners.BlockUpdateListener
import com.peasenet.util.listeners.ChunkUpdateListener
import com.peasenet.util.listeners.RenderListener
import com.peasenet.util.listeners.WorldRenderListener
import net.minecraft.client.gl.ShaderProgram
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import org.lwjgl.opengl.GL11
import java.util.*

/**
 * An ESP mod that draws boxes around user selected blocks in the world.
 *
 * @author GT3CH1
 * @version 09-06-2024
 * @since 09-01-2024
 * @see EspMod
 * @see BlockEspTracerCommon
 */
class ModBlockEsp : BlockEspTracerCommon<BlockEspConfig>("Block ESP",
    "gavinsmod.mod.esp.blockesp",
    "blockesp",
    ModCategory.ESP,
    { minecraftClient.setScreen(GuiBlockEsp()) }), RenderListener {
    private var count = 0

    override fun onEnable() {
        super.onEnable()
        em.subscribe(RenderListener::class.java, this)
    }

    override fun onWorldRender(level: ClientWorld, stack: MatrixStack, bufferBuilder: BufferBuilder, delta: Float) {
        synchronized(chunks) {
            chunks.entries.removeIf { !it.value.inRenderDistance() }
        }
    }

    /*   override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
           if (chunks.isEmpty()) return
           count = 0
           RenderUtils.setupRenderWithShader(matrixStack)
           val bufferBuilder = RenderUtils.getBufferBuilder()
           synchronized(chunks) {
               for (chunk in chunks.values.filter { it.inRenderDistance() }) {
                   for (block in chunk.blocks.values) {
                       // somehow do breadth first search

                       matrixStack.push()
                       val pos = Vec3i(block.x, block.y, block.z).subtract(RenderUtils.getCameraRegionPos().toVec3i())
                       val box = Box(
                           pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), pos.x + 1.0, pos.y + 1.0, pos.z + 1.0
                       )
                       RenderUtils.drawOutlinedBox(
                           box,
                           bufferBuilder,
                           matrixStack.peek().positionMatrix,
                           color = getSettings().blockColor,
                           alpha = getSettings().alpha
                       )
                       matrixStack.pop()
                   }
               }
           }
           RenderUtils.drawBuffer(bufferBuilder, matrixStack)
       }

   */

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        if (chunks.isEmpty()) return
        count = 0
        RenderUtils.setupRenderWithShader(matrixStack)
        val bufferBuilder = RenderUtils.getBufferBuilder()
        synchronized(chunks) {
            for (chunk in chunks.values.filter { it.inRenderDistance() }) {
                val visited = mutableSetOf<Long>()
                for (block in chunk.blocks.values) {
                    if (!visited.contains(block.key)) {
                        val connectedBlocks = bfs(chunk, block)
                        drawConnectedBlocks(matrixStack, bufferBuilder, connectedBlocks)
                        visited.addAll(connectedBlocks.map { it.key })
                    }
                }
            }
        }
        RenderUtils.drawBuffer(bufferBuilder, matrixStack)
    }


    private fun bfs(chunk: GavChunk, startBlock: GavBlock): List<GavBlock> {
        val queue: Queue<GavBlock> = LinkedList()
        val visited = mutableSetOf<Long>()
        val connectedBlocks = mutableListOf<GavBlock>()

        queue.add(startBlock)
        visited.add(startBlock.key)

        while (queue.isNotEmpty()) {
            val block = queue.poll()
            connectedBlocks.add(block)

            val neighbors = getNeighbors(chunk, block)
            for (neighbor in neighbors) {
                if (!visited.contains(neighbor.key)) {
                    queue.add(neighbor)
                    visited.add(neighbor.key)
                }
            }
        }

        return connectedBlocks
    }

    private fun getNeighbors(chunk: GavChunk, block: GavBlock): List<GavBlock> {
        val neighbors = mutableListOf<GavBlock>()
        val directions = listOf(
            Vec3i(1, 0, 0), Vec3i(-1, 0, 0),
            Vec3i(0, 1, 0), Vec3i(0, -1, 0),
            Vec3i(0, 0, 1), Vec3i(0, 0, -1)
        )

        for (dir in directions) {
            val neighborPos = BlockPos(block.x + dir.x, block.y + dir.y, block.z + dir.z)
            val neighbor = GavBlock(neighborPos.x, neighborPos.y, neighborPos.z)
            val neighborKey = neighbor.key
            if (chunk.blocks.containsKey(neighborKey)) {
                neighbors.add(chunk.blocks[neighborKey]!!)
            }
        }

        return neighbors
    }

    private fun drawConnectedBlocks(matrixStack: MatrixStack, bufferBuilder: BufferBuilder, blocks: List<GavBlock>) {
        val minBlockX: Int = blocks.minOf { it.x }
        val minBlockY: Int = blocks.minOf { it.y }
        val minBlockZ: Int = blocks.minOf { it.z }
        val maxBlockX: Int = blocks.maxOf { it.x + 1 }
        val maxBlockY: Int = blocks.maxOf { it.y + 1 }
        val maxBlockZ: Int = blocks.maxOf { it.z + 1 }
        for (block in blocks) {

            matrixStack.push()
            val minPos = Vec3d(minBlockX.toDouble(), minBlockY.toDouble(), minBlockZ.toDouble()).subtract(
                RenderUtils.getCameraRegionPos().toVec3d()
            )
            val maxPos = Vec3d(maxBlockX.toDouble(), maxBlockY.toDouble(), maxBlockZ.toDouble()).subtract(
                RenderUtils.getCameraRegionPos().toVec3d()
            )
            val box = Box(minPos, maxPos)
            RenderUtils.drawOutlinedBox(
                box,
                bufferBuilder,
                matrixStack.peek().positionMatrix,
                color = getSettings().blockColor,
                alpha = getSettings().alpha
            )
            matrixStack.pop()
        }
    }


    private fun isSameType(block1: GavBlock, block2: GavBlock, chunkPos: ChunkPos): Boolean {
        val chunk = minecraftClient.getWorld().getChunk(chunkPos.x, chunkPos.z)
        val blockState1 = chunk.getBlockState(BlockPos(block1.x, block1.y, block1.z))
        val blockState2 = chunk.getBlockState(BlockPos(block2.x, block2.y, block2.z))
        return blockState1.block == blockState2.block
    }
}