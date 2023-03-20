/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mods.render.waypoints

import com.peasenet.gavui.color.Color
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A waypoint is a three-dimensional integer coordinate with a name, color, and can either have
 * an ESP, a tracer, or both.
 */
class Waypoint {
    @JvmField
    var x: Int

    @JvmField
    var y: Int

    @JvmField
    var z: Int
    var name: String? = null
        private set

    var dimension: String? = null

    @JvmField
    var color: Color? = null
    var isEnabled = false
    var isTracerEnabled = false
    var isEspEnabled = false

    constructor(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(vec: Vec3i) {
        x = vec.x
        y = vec.y
        z = vec.z
    }

    fun setName(name: String) {
        var name1 = name
        if (name1.isEmpty()) name1 = "Waypoint"
        this.name = name1
    }

    fun equals(w: Waypoint): Boolean {
        return hashCode() == w.hashCode()
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    val pos: Vec3d
        get() = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
}