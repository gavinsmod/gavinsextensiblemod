package com.peasenet.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public record RegionPos(int x, int z) {
    public Vec3d toVec3d() {
        return new Vec3d(x, 0, z);
    }

    public Vec3i toVec3i() {
        return new Vec3i(x, 0, z);
    }

    static RegionPos fromVec3d(Vec3d vec3d) {
        return new RegionPos((int) vec3d.x, (int) vec3d.z);
    }

    static RegionPos fromBlockPos(BlockPos pos) {
        return new RegionPos(pos.getX() >> 9 << 9, pos.getZ() >> 9 << 9);
    }
}
