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
import com.peasenet.extensions.toVec3d
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
import net.minecraft.util.math.*
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
                        drawConnectedBlocks(matrixStack, bufferBuilder, connectedBlocks, chunk)
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

    private fun drawConnectedBlocks(
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        blocks: List<GavBlock>,
        chunk: GavChunk
    ) {
//        for (block in blocks) {
//            val facesToDraw = getExposedFaces(block, chunk)
//            // simplify facesToDraw by minX, minY, minZ, maxX, maxY, maxZ
//            val set = mutableSetOf<Direction>()
//            set.addAll(facesToDraw)
//            for (face in set) {
//                matrixStack.push()
//                val pos =
//                    Vec3i(block.x, block.y, block.z).subtract(RenderUtils.getCameraRegionPos().toVec3i())
//                val box = getFaceBox(pos, face)
//                RenderUtils.drawOutlinedBox(
//                    box,
//                    bufferBuilder,
//                    matrixStack.peek().positionMatrix,
//                    color = getSettings().blockColor,
//                    alpha = getSettings().alpha
//                )
//                matrixStack.pop()
//            }
//        }
        val combinedFaces = combineFaces(blocks, chunk)
        for (box in combinedFaces) {
            matrixStack.push()
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

    private fun getExposedFaces(block: GavBlock, chunk: GavChunk): List<Direction> {
        val exposedFaces = mutableListOf<Direction>()
        val directions = listOf(
            Direction.EAST, Direction.WEST,
            Direction.UP, Direction.DOWN,
            Direction.SOUTH, Direction.NORTH
        )

        for (dir in directions) {
            if(dir == Direction.UP || dir == Direction.DOWN) {
                continue
            }
            val neighborPos = BlockPos(block.x + dir.offsetX, block.y + dir.offsetY, block.z + dir.offsetZ)
            val neighborKey = GavBlock(neighborPos.x, neighborPos.y, neighborPos.z).key
            if (!chunk.blocks.containsKey(neighborKey) || !isSameType(
                    block,
                    chunk.blocks[neighborKey]!!,
                    ChunkPos(chunk.x, chunk.z)
                )
            ) {
                exposedFaces.add(dir)
            }
        }

        return exposedFaces
    }

    private fun isSameType(block1: GavBlock, block2: GavBlock, chunkPos: ChunkPos): Boolean {
        val chunk = minecraftClient.getWorld().getChunk(chunkPos.x, chunkPos.z)
        val blockState1 = chunk.getBlockState(BlockPos(block1.x, block1.y, block1.z))
        val blockState2 = chunk.getBlockState(BlockPos(block2.x, block2.y, block2.z))
        return blockState1.block == blockState2.block
    }


    private fun getFaceBox(pos: Vec3i, face: Direction): Box {
        return when (face) {
            Direction.EAST -> Box(
                pos.x + 1.0,
                pos.y.toDouble(),
                pos.z.toDouble(),
                pos.x + 1.0,
                pos.y + 1.0,
                pos.z + 1.0
            )

            Direction.WEST -> Box(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                pos.x.toDouble(),
                pos.y + 1.0,
                pos.z + 1.0
            )

            Direction.UP -> Box(pos.x.toDouble(), pos.y + 1.0, pos.z.toDouble(), pos.x + 1.0, pos.y + 1.0, pos.z + 1.0)
            Direction.DOWN -> Box(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                pos.x + 1.0,
                pos.y.toDouble(),
                pos.z + 1.0
            )

            Direction.SOUTH -> Box(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z + 1.0,
                pos.x + 1.0,
                pos.y + 1.0,
                pos.z + 1.0
            )

            Direction.NORTH -> Box(
                pos.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                pos.x + 1.0,
                pos.y + 1.0,
                pos.z.toDouble()
            )
        }
    }


    private fun combineFaces(blocks: List<GavBlock>, chunk: GavChunk): List<Box> {
        val faceBoxes = mutableListOf<Box>()
        for (block in blocks) {
            val facesToDraw = getExposedFaces(block, chunk).toMutableList()
            // check all neighbors of block
            val neighbors = getNeighbors(chunk, block)
//            for (neighbor in neighbors) {
//                val neighborFaces = getExposedFaces(neighbor, chunk)
//                for (face in neighborFaces) {
//                    if (face in facesToDraw) {
//                        facesToDraw.remove(face)
//                    }
//                }
//            }
            val pos = Vec3i(block.x, block.y, block.z).subtract(RenderUtils.getCameraRegionPos().toVec3i())
            for (face in facesToDraw) {
                val box = getFaceBox(pos, face)
                faceBoxes.add(box)
            }
        }

        return mergeBoxes(faceBoxes)
    }

    private fun mergeBoxes(boxes: List<Box>): List<Box> {
        val mergedBoxes = mutableListOf<Box>()
        val visited = BooleanArray(boxes.size)

        for (i in boxes.indices) {
            if (visited[i]) continue
            var currentBox = boxes[i]
            visited[i] = true

            for (j in i + 1 until boxes.size) {
                if (visited[j]) continue
                val box = boxes[j]

                if (canMerge(currentBox, box)) {
                    currentBox = mergeTwoBoxes(currentBox, box)
                    visited[j] = true
                }
            }

            mergedBoxes.add(currentBox)
        }

        return mergedBoxes
    }

    private fun canMerge(box1: Box, box2: Box): Boolean {
        return (box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minY == box2.minY && box1.maxY == box2.maxY && (box1.minZ == box2.maxZ || box1.maxZ == box2.minZ)) ||
                (box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && (box1.minY == box2.maxY || box1.maxY == box2.minY)) ||
                (box1.minY == box2.minY && box1.maxY == box2.maxY && box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && (box1.minX == box2.maxX || box1.maxX == box2.minX)) ||
                (box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && box1.minY == box2.minY && box1.maxY == box2.maxY) ||
                (box1.minY == box2.minY && box1.maxY == box2.maxY && box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minZ == box2.minZ && box1.maxZ == box2.maxZ) ||
                (box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minY == box2.minY && box1.maxY == box2.maxY) ||
                (box1.minX == box2.minX && box1.maxX == box2.maxX && box1.minY == box2.minY && box1.maxY == box2.maxY && (box1.minZ <= box2.maxZ && box1.maxZ >= box2.minZ)) ||
                (box1.minY == box2.minY && box1.maxY == box2.maxY && box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && (box1.minX <= box2.maxX && box1.maxX >= box2.minX)) ||
                (box1.minZ == box2.minZ && box1.maxZ == box2.maxZ && box1.minX == box2.minX && box1.maxX == box2.maxX && (box1.minY <= box2.maxY && box1.maxY >= box2.minY))
    }

    private fun mergeTwoBoxes(box1: Box, box2: Box): Box {
        val minX = minOf(box1.minX, box2.minX)
        val minY = minOf(box1.minY, box2.minY)
        val minZ = minOf(box1.minZ, box2.minZ)
        val maxX = maxOf(box1.maxX, box2.maxX)
        val maxY = maxOf(box1.maxY, box2.maxY)
        val maxZ = maxOf(box1.maxZ, box2.maxZ)
        return Box(minX, minY, minZ, maxX, maxY, maxZ)
    }
}