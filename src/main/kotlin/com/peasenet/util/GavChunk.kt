package com.peasenet.util

import net.minecraft.util.math.ChunkPos

class GavChunk(val x: Int, val z: Int) {
    val key: Long = ChunkPos.toLong(x, z)
    val blocks: HashMap<Long, GavBlock> = HashMap()
}

class GavBlock(val x: Int, val y: Int, val z: Int) {
    val key: Long = ((x.toLong() and 0x3FFFFFF shl 38) or (z.toLong() and 0x3FFFFFF shl 12) or (y.toLong() and 0xFFF))
    var color: Int = 0
}