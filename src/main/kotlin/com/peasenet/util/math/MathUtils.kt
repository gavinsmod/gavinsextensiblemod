/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
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
package com.peasenet.util.math

import com.peasenet.main.GavinsModClient
import com.peasenet.util.RegionPos
import net.minecraft.entity.Entity
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * A class that contains math utilities.
 * @author GT3CH1
 * @version 09-06-2024
 * @since 03-02-2023
 */
object MathUtils {
    /**
     * Gets the rotation to set to see the given entity at its midpoint.
     *
     * @param entity The entity to look at.
     * @return The rotation to set to.
     */
    fun getRotationToEntity(entity: Entity): Rotation {
        val player = GavinsModClient.player!!
        val playerPos =
            Vec3d(player.getPrevX(), player.getPos().y + player.getEyeHeightWithPose(), player.getPos().z)

        val diffX = entity.x - playerPos.x
        val diffY = entity.boundingBox.center.y - playerPos.y
        val diffZ = entity.z - playerPos.z
        val diffXZ = sqrt(diffX * diffX + diffZ * diffZ)
        val yaw = Math.toDegrees(atan2(diffZ, diffX)).toFloat() - 90f
        val pitch = -Math.toDegrees(atan2(diffY, diffXZ)).toFloat()
        return Rotation(pitch, yaw)
    }

    /**
     * Returns the linear interpolation of the given position.
     * @param delta The delta to lerp by.
     * @param pos The current position.
     * @param oldPos The old position.
     * @return The camera region position.
     */
    fun lerp(delta: Float, pos: Vec3d, oldPos: Vec3d, region: RegionPos): Vec3d {
        return lerp(delta, pos, oldPos).subtract(region.toVec3d())
    }

    fun lerp(delta: Float, pos: Vec3d, entity: Entity, region: RegionPos): Vec3d {
        return lerp(delta, pos, Vec3d(entity.lastX, entity.lastY, entity.lastZ)).subtract(region.toVec3d())
    }

    /**
     * Returns the linear interpolation of the given position.
     * @param delta The delta to lerp by.
     * @param pos The current position.
     * @param oldPos The old position.
     * @return The lerp position.
     */
    fun lerp(delta: Float, pos: Vec3d, oldPos: Vec3d): Vec3d {
        val xLerped = MathHelper.lerp(delta.toDouble(), oldPos.x, pos.x)
        val yLerped = MathHelper.lerp(delta.toDouble(), oldPos.y, pos.y)
        val zLerped = MathHelper.lerp(delta.toDouble(), oldPos.z, pos.z)
        return Vec3d(xLerped, yLerped, zLerped)
    }
}