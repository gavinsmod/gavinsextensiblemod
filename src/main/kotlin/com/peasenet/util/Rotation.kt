package com.peasenet.util

import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d

class Rotation(val pitch: Float, val yaw: Float) {

    fun asLookVec(): Vec3d {
        val radiansPerDegree = MathHelper.RADIANS_PER_DEGREE
        val pi = MathHelper.PI

        val newPitch = -MathHelper.wrapDegrees(pitch) * radiansPerDegree
        val cosPitch = -MathHelper.cos(newPitch)
        val sinPitch = MathHelper.sin(newPitch)

        val newYaw = -MathHelper.wrapDegrees(yaw) * radiansPerDegree - pi
        val cosYaw = MathHelper.cos(newYaw)
        val sinYaw = MathHelper.sin(newYaw)
        return Vec3d(sinYaw * cosPitch.toDouble(), sinPitch.toDouble(), cosYaw * cosPitch.toDouble())
    }
}