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

package com.peasenet.util

import com.peasenet.config.BlockListConfig
import com.peasenet.gavui.color.Color
import com.peasenet.main.GavinsModClient
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.*
import net.minecraft.world.Heightmap
import net.minecraft.world.chunk.Chunk
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class GavChunk(val x: Int, val z: Int) {
    val pos: BlockPos = BlockPos(x, 0, z)
    val blocks: HashMap<Long, GavBlock> = HashMap()

    fun addBlock(pos: BlockPos) {
        val block = GavBlock(pos.x, pos.y, pos.z)
        blocks[block.key] = block
    }

    fun inRenderDistance(): Boolean {
        val distance = RenderUtils.getRenderDistance()
        val playerPos = GavinsModClient.player?.getBlockPos()!!
        val chunkX = ChunkSectionPos.getSectionCoord(playerPos.x)
        val chunkZ = ChunkSectionPos.getSectionCoord(playerPos.z)
        return x <= chunkX + distance && x >= chunkX - distance && z <= chunkZ + distance && z >= chunkZ - distance
    }

    fun getRenderDistance(): Int {
        val distance = RenderUtils.getRenderDistance()
        val playerPos = GavinsModClient.player?.getBlockPos()!!
        val chunkX = ChunkSectionPos.getSectionCoord(playerPos.x)
        val chunkZ = ChunkSectionPos.getSectionCoord(playerPos.z)
        return Math.abs(x - chunkX) + Math.abs(z - chunkZ)
    }

    companion object {
        fun search(chunk: Chunk, blocks: List<String>): GavChunk {
            val searchChunk = GavChunk(chunk.pos.x, chunk.pos.z)
            val tempBlockPos = BlockPos.Mutable()
            for (x in chunk.pos.startX..chunk.pos.endX) {
                for (z in chunk.pos.startZ..chunk.pos.endZ) {
                    val chunkHeight =
                        chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).get(x - chunk.pos.startX, z - chunk.pos.startZ)
                    for (y in GavinsModClient.minecraftClient.getWorld().bottomY..chunkHeight + 1) {
                        tempBlockPos.set(x, y, z)
                        val block = chunk.getBlockState(tempBlockPos).block
                        if (blocks.contains(BlockListConfig.getId(block))) {
                            searchChunk.addBlock(tempBlockPos)
                        }
                    }
                }
            }
            return searchChunk
        }

        private fun bfs(chunks: List<GavChunk>, start: GavBlock): HashSet<GavBlock> {
            val visited = HashMap<Long, GavBlock>()
            val queue = LinkedList<GavBlock>()
            queue.add(start)
            visited[start.key] = start
            while (queue.isNotEmpty()) {
                val current = queue.poll()
                for (direction in Direction.entries) {
                    val next = current.getPos(direction)
                    if (visited.containsKey(next.key)) {
                        continue
                    }
                    for (chunk in chunks) {
                        if (chunk.blocks.containsKey(next.key)) {
                            visited[next.key] = next
                            queue.add(next)
                            break
                        }
                    }
                }
            }
            return HashSet(visited.values)
        }

        fun getAllStructures(chunks: List<GavChunk>): List<Structure> {
            var allBlocks = HashSet<GavBlock>()
            var structures = ArrayList<Structure>()
            for (chunk in chunks) {
                for (block in chunk.blocks.values) {
                    if (allBlocks.size < 500) {
                        allBlocks.add(block)
                        if (allBlocks.size >= 500) {
                            break
                        }
                    }
                }
                if (allBlocks.size >= 500) {
                    break
                }
            }

            for (block in allBlocks) {
                var found = false
                for (structure in structures) {
                    if (structure.contains(block)) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    val newStructure = Structure()
                    val connectedBlocks = bfs(chunks, block)
                    connectedBlocks.forEach { newStructure.addBlock(it) }
                    structures.add(newStructure)
                }
            }

            return structures
        }
    }
}

class Structure() {
    val blocks = HashMap<Long, GavBlock>()
    fun contains(block: GavBlock): Boolean {
        return blocks.contains(block.key)
    }

    override fun hashCode(): Int {
        if (blocks.isEmpty()) return 0
        return blocks.values.first().key.toInt()
    }

    fun addBlock(block: GavBlock) {
        blocks[block.key] = block
    }

    private fun magic(matrixStack: MatrixStack, bufferBuilder: BufferBuilder, color: Color) {
        // get max and min x, y, z
        val minX = blocks.minOf { it.value.x }
        val minY = blocks.minOf { it.value.y }
        val minZ = blocks.minOf { it.value.z }
        val maxX = blocks.maxOf { it.value.x }
        val maxY = blocks.maxOf { it.value.y }
        val maxZ = blocks.maxOf { it.value.z }
        val arrayWidth = (maxX - minX) + 1
        val arrayHeight = (maxZ - minZ) + 1
        val arrayDepth = (maxY - minY) + 1
        val array = Array(arrayDepth) { Array(arrayHeight) { Array(arrayWidth) { 0 } } }
        for (block in blocks.values) {
            array[block.y - minY][block.z - minZ][block.x - minX] = 1
        }
        for (y in 0 until arrayDepth) {
            for (z in 0 until arrayHeight) {
                for (x in 0 until arrayWidth) {
                    val block = array[y][z][x]
                    if (block == 0) continue
                    // top left is z-1, x+1, top middle is z,x+1,... and bottom right is z+1, x-1
                    val topLeft = if (z == 0 || x == arrayWidth - 1) 0 else array[y][z - 1][x + 1]
                    val topMiddle = if (x == arrayWidth - 1) 0 else array[y][z][x + 1]
                    val topRight = if (z == arrayHeight - 1 || x == arrayWidth - 1) 0 else array[y][z + 1][x + 1]
                    val middleLeft = if (z == 0) 0 else array[y][z - 1][x]
                    val middleRight = if (z == arrayHeight - 1) 0 else array[y][z + 1][x]
                    val bottomLeft = if (z == 0 || x == 0) 0 else array[y][z - 1][x - 1]
                    val bottomMiddle = if (x == 0) 0 else array[y][z][x - 1]
                    val bottomRight = if (z == arrayHeight - 1 || x == 0) 0 else array[y][z + 1][x - 1]
                    // yTopLeft is y+1,z-1 and yTopRight  is y+1,z+1 and yBottomLeft is y-1,z-1 and yBottomRight is y-1,z+1
                    val zTopLeft = if (y == arrayDepth - 1 || z == 0) 0 else array[y + 1][z - 1][x]
                    val zTopRight = if (y == arrayDepth - 1 || z == arrayHeight - 1) 0 else array[y + 1][z + 1][x]
                    val zBottomLeft = if (y == 0 || z == 0) 0 else array[y - 1][z - 1][x]
                    val zBottomRight = if (y == 0 || z == arrayHeight - 1) 0 else array[y - 1][z + 1][x]
                    // xTopLeft is y+1,x-1 and xTopRight is y+1,x+1 and xBottomLeft is y-1,x-1 and xBottomRight is y-1,x+1
                    val xTopLeft = if (y == arrayDepth - 1 || x == 0) 0 else array[y + 1][z][x - 1]
                    val xTopRight = if (y == arrayDepth - 1 || x == arrayWidth - 1) 0 else array[y + 1][z][x + 1]
                    val xBottomLeft = if (y == 0 || x == 0) 0 else array[y - 1][z][x - 1]
                    val xBottomRight = if (y == 0 || x == arrayWidth - 1) 0 else array[y - 1][z][x + 1]

                    val above = if (y == arrayDepth - 1) 0 else array[y + 1][z][x]
                    val below = if (y == 0) 0 else array[y - 1][z][x]
                    val mask = Neighbors.TopLeft.mask * topLeft or
                            Neighbors.TopMiddle.mask * topMiddle or
                            Neighbors.TopRight.mask * topRight or
                            Neighbors.MiddleLeft.mask * middleLeft or
                            Neighbors.MiddleRight.mask * middleRight or
                            Neighbors.BottomLeft.mask * bottomLeft or
                            Neighbors.BottomMiddle.mask * bottomMiddle or
                            Neighbors.BottomRight.mask * bottomRight or
                            Neighbors.Above.mask * above or
                            Neighbors.Below.mask * below or
                            Neighbors.ZTopLeft.mask * zTopLeft or
                            Neighbors.ZTopRight.mask * zTopRight or
                            Neighbors.ZBottomLeft.mask * zBottomLeft or
                            Neighbors.ZBottomRight.mask * zBottomRight or
                            Neighbors.XTopLeft.mask * xTopLeft or
                            Neighbors.XTopRight.mask * xTopRight or
                            Neighbors.XBottomLeft.mask * xBottomLeft or
                            Neighbors.XBottomRight.mask * xBottomRight
                    val theGavBlock = blocks.values.first { it.x == x + minX && it.y == y + minY && it.z == z + minZ }
                    theGavBlock.setVisibleEdges(mask)
                    theGavBlock.onRender(matrixStack, bufferBuilder, color)
                }
            }
        }
    }


    fun render(matrixStack: MatrixStack, bufferBuilder: BufferBuilder, color: Color) {
        magic(matrixStack, bufferBuilder, color)
    }
}

class GavBlock(val x: Int, val y: Int, val z: Int) {
    val key: Long = ((x.toLong() and 0x3FFFFFF shl 38) or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF))
    private var visibleEdges = Edge.All.mask

    override fun hashCode(): Int {
        return ((x and 0x3FFFFFF shl 38) or (z and 0x3FFFFFF shl 12) or (y and 0xFFF))
    }

    fun setVisibleEdges(neighbors: Int) {
        if (neighbors == Neighbors.None.mask) {
            visibleEdges = Edge.All.mask
            return
        }
        if (neighbors and Neighbors.TopMiddle.mask != 0) {
            visibleEdges = (visibleEdges and Edge.Edge3.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge11.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge7.mask.inv())
            visibleEdges = (visibleEdges and Edge.Edge8.mask.inv())
        }
        if (neighbors and Neighbors.MiddleLeft.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge4.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge12.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge8.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge5.mask.inv()
        }
        if (neighbors and Neighbors.MiddleRight.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge2.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge10.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge6.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge7.mask.inv()
        }
        if (neighbors and Neighbors.BottomMiddle.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge1.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge9.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge5.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge6.mask.inv()
        }
        if (neighbors and Neighbors.Above.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge9.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge10.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge11.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge12.mask.inv()

        }
        if (neighbors and Neighbors.Below.mask != 0) {
            visibleEdges = visibleEdges and Edge.Edge1.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge2.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge3.mask.inv()
            visibleEdges = visibleEdges and Edge.Edge4.mask.inv()
        }
    }

    val pos = BlockPos(x, y, z)

    fun getPos(direction: Direction): GavBlock {
        return when (direction) {
            Direction.NORTH -> GavBlock(x, y, z - 1)
            Direction.SOUTH -> GavBlock(x, y, z + 1)
            Direction.EAST -> GavBlock(x + 1, y, z)
            Direction.WEST -> GavBlock(x - 1, y, z)
            Direction.UP -> GavBlock(x, y + 1, z)
            Direction.DOWN -> GavBlock(x, y - 1, z)
            else -> this
        }
    }

    fun renderEdges(
        edges: Int,
        blockPos: BlockPos,
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        color: Color
    ) {
        for (edge in Edge.entries) {
            if (edge == Edge.All || edge == Edge.None) continue
            val maskedVal = edges and edge.mask
            if (maskedVal != 0) {
                renderEdge(edge, blockPos, matrixStack, bufferBuilder, color)
            }
        }
    }

    fun renderEdge(
        edge: Edge,
        blockPos: BlockPos,
        matrixStack: MatrixStack,
        bufferBuilder: BufferBuilder,
        color: Color
    ) {
        val posMatrix = matrixStack.peek().positionMatrix
        when (edge) {
            Edge.Edge1 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge2 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge3 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge4 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge5 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge6 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge7 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge8 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble(), blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge9 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge10 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge11 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble() + 1),
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.Edge12 -> {
                RenderUtils.drawSingleLine(
                    bufferBuilder,
                    posMatrix,
                    Vec3d(blockPos.x.toDouble() + 1, blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble() + 1, blockPos.z.toDouble()),
                    color,
                    1f,
                    true
                )
            }

            Edge.All -> {
                renderEdge(Edge.Edge1, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge2, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge3, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge4, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge5, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge6, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge7, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge8, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge9, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge10, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge11, blockPos, matrixStack, bufferBuilder, color)
                renderEdge(Edge.Edge12, blockPos, matrixStack, bufferBuilder, color)
            }

            Edge.None -> {}
        }
    }


    fun onRender(matrixStack: MatrixStack, bufferBuilder: BufferBuilder, color: Color) {
        matrixStack.push()
        renderEdges(visibleEdges, pos, matrixStack, bufferBuilder, color)
        matrixStack.pop()
    }
}


enum class Edge(val mask: Int) {
    Edge1(1 shl 1),
    Edge2(1 shl 2),
    Edge3(1 shl 3),
    Edge4(1 shl 4),
    Edge5(1 shl 5),
    Edge6(1 shl 6),
    Edge7(1 shl 7),
    Edge8(1 shl 8),
    Edge9(1 shl 9),
    Edge10(1 shl 10),
    Edge11(1 shl 11),
    Edge12(1 shl 12),
    All(0xFFFF),
    None(0)
}

enum class Neighbors(val mask: Int) {
    TopLeft(1 shl 1),
    TopMiddle(1 shl 2),
    TopRight(1 shl 3),
    MiddleLeft(1 shl 4),
    Self(1 shl 5),
    MiddleRight(1 shl 6),
    BottomLeft(1 shl 7),
    BottomMiddle(1 shl 8),
    BottomRight(1 shl 9),
    Above(1 shl 10),
    Below(1 shl 11),
    ZTopLeft(1 shl 12),
    ZTopRight(1 shl 13),
    ZBottomLeft(1 shl 14),
    ZBottomRight(1 shl 15),
    XTopLeft(1 shl 16),
    XTopRight(1 shl 17),
    XBottomLeft(1 shl 18),
    XBottomRight(1 shl 19),
    All(0xFFFFF),
    None(0);
}