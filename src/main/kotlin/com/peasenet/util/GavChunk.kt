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
import com.peasenet.main.GavinsModClient
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.ChunkSectionPos
import net.minecraft.world.Heightmap
import net.minecraft.world.chunk.Chunk

class GavChunk(val x: Int, val z: Int) {
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

    companion object {
        fun search(chunk: Chunk, blocks: List<String>): GavChunk {
            val searchChunk = GavChunk(chunk.pos.x, chunk.pos.z)
            val tempBlockPos = BlockPos.Mutable()
            for (x in chunk.pos.startX..chunk.pos.endX) {
                for (z in chunk.pos.startZ..chunk.pos.endZ) {
                    val chunkHeight =
                        chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).get(x - chunk.pos.startX, z - chunk.pos.startZ)
                    for (y in GavinsModClient.minecraftClient.getWorld().bottomY..chunkHeight + 10) {
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
    }
}

class GavBlock(val x: Int, val y: Int, val z: Int) {
    val key: Long =
        ((x.toLong() and 0x3FFFFFF shl 38) or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF))
    var color: Int = 0
}