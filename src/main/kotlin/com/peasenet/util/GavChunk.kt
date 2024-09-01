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