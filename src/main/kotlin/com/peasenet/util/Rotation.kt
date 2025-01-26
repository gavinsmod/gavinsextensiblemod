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