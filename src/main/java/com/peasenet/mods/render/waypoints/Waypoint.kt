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
 * @version 03-01-2023
 * A waypoint is a three-dimensional integer coordinate with a name, color, and can either have
 * an ESP, a tracer, or both.
 */
class Waypoint {
    /**
     * Gets the x coordinate of the waypoint.
     *
     * @return The x coordinate.
     */
    /**
     * Sets the X coordinate.
     *
     * @param x - The X coordinate.
     */
    /**
     * The x coordinate.
     */
    @JvmField
    var x: Int
    /**
     * Gets the y coordinate of the waypoint.
     *
     * @return The y coordinate.
     */
    /**
     * Sets the Y coordinate.
     *
     * @param y - The Y coordinate.
     */
    /**
     * The y coordinate.
     */
    @JvmField
    var y: Int
    /**
     * Gets the z coordinate of the waypoint.
     *
     * @return The z coordinate.
     */
    /**
     * Sets the z coordinate.
     *
     * @param z - The z coordinate.
     */
    /**
     * The z coordinate.
     */
    @JvmField
    var z: Int
    /**
     * The name of the waypoint.
     *
     * @return The name.
     */
    /**
     * The name of the waypoint.
     */
    var name: String? = null
        private set
    /**
     * Gets the color of the waypoint.
     *
     * @return The color.
     */
    /**
     * Sets the color of the waypoint.
     *
     * @param color - The color index to set the color of the waypoint to.
     */
    /**
     * The color index of the waypoint.
     */
    @JvmField
    var color: Color? = null
    /**
     * Gets whether the waypoint is enabled.
     *
     * @return True if the waypoint is enabled.
     */
    /**
     * Sets whether the waypoint is enabled.
     *
     * @param enabled - Whether the waypoint is enabled.
     */
    /**
     * Whether the waypoint is currently enabled, meaning it can be rendered.
     */
    var isEnabled = false
    /**
     * Gets whether the tracer is enabled.
     *
     * @return True if the tracer is enabled.
     */
    /**
     * Sets whether the tracer should be enabled.
     *
     * @param tracerEnabled - Whether the tracer is enabled.
     */
    /**
     * Whether the tracer is enabled.
     */
    var isTracerEnabled = false
    /**
     * Gets whether the ESP is enabled.
     *
     * @return True if the ESP is enabled.
     */
    /**
     * Sets whether the waypoint esp is enabled.
     *
     * @param espEnabled - Whether to enable the esp.
     */
    /**
     * Whether the ESP is enabled.
     */
    var isEspEnabled = false

    /**
     * Creates a new waypoint at the given coordinates.
     *
     * @param x - The x coordinate.
     * @param y - The y coordinate.
     * @param z - The z coordinate.
     */
    constructor(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    /**
     * Creates a new waypoint at the given Vec3i coordinate.
     *
     * @param vec - The positional vector.
     */
    constructor(vec: Vec3i) {
        x = vec.x
        y = vec.y
        z = vec.z
    }

    /**
     * Sets the name of the waypoint.
     *
     * @param name - The name to set.
     */
    fun setName(name: String) {
        var name1 = name
        if (name1.isEmpty()) name1 = "Waypoint"
        this.name = name1
    }

    /**
     * Whether this waypoint and the given waypoint are equal. The comparison is done on the name,
     * x, y, and z coordinates.
     *
     * @param w - The waypoint to compare to.
     * @return True if the waypoints are equal.
     */
    fun equals(w: Waypoint): Boolean {
        return hashCode() == w.hashCode()
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    val pos: Vec3d
        /**
         * Gets the position of the waypoint as a Vec3d.
         * @return A Vec3d of the waypoint's position.
         */
        get() = Vec3d(x.toDouble(), y.toDouble(), z.toDouble())
}