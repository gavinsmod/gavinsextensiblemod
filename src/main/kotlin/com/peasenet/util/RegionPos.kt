package com.peasenet.util

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

data class RegionPos(val x: Int, val z: Int) {
    fun toVec3d(): Vec3d {
        return Vec3d(x.toDouble(), 0.0, z.toDouble())
    }

    fun toVec3i(): Vec3i {
        return Vec3i(x, 0, z)
    }

    companion object {
        fun fromBlockPos(pos: BlockPos): RegionPos {
            return RegionPos(pos.x shr 9 shl 9, pos.z shr 9 shl 9)
        }
    }
}
