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
package com.peasenet.mods.render.waypoints

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.util.Dimension
import net.minecraft.util.math.Vec3i
import java.util.UUID

/**
 * A waypoint is a three-dimensional integer coordinate with a name, color, and can either have
 * an ESP, a tracer, or both.
 *
 * @param coordinates The x, y, z coordinates of the waypoint.
 * @param name The name of the waypoint.
 * @param dimensions The list of dimensions this waypoint will be rendered in.
 * @param color The color of this waypoint.
 * @param isEnabled Whether this waypoint should be rendered.
 * @param renderEsp Whether to render this waypoints esp.
 * @param renderTracer Whether to render this waypoints tracer.
 * @param uuid The waypoints UUID. Defaults to a randomly generated UUID.
 *
 * @see com.peasenet.config.WaypointConfig
 * @see com.peasenet.gui.mod.waypoint.GuiWaypoint
 * @see com.peasenet.mods.render.waypoints
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 03-23-2023
 */
class Waypoint(
    val coordinates: Vec3i = Vec3i.ZERO,
    val name: String = "",
    val dimensions: MutableSet<String> = mutableSetOf(),
    val color: Color = Colors.WHITE,
    val isEnabled: Boolean = true,
    val renderEsp: Boolean = true,
    val renderTracer: Boolean = true,
    val uuid: UUID = UUID.randomUUID(),
) {
    /**
     * Adds a dimension to the list of dimensions.
     * If the dimension is already in the list, this will do nothing.
     */
    fun addDimension(dim: Dimension): Boolean = dimensions.add(dim.dimension)

    /**
     * Clear all dimensions from the list.
     */
    fun clearDimensions() = dimensions.clear()

    /**
     * Checks if the dimension is in the list of dimensions.
     */
    fun hasDimension(dim: Dimension): Boolean = dimensions.any { it == dim.dimension }

    /**
     * Checks if the waypoint has any dimensions.
     */
    fun hasDimensions(): Boolean = dimensions.isNotEmpty()

    /**
     * Checks whether if the waypoint can render in the given dimension.
     */
    fun canRender(dim: Dimension): Boolean = dimensions.contains(dim.dimension) && isEnabled
    override fun equals(other: Any?): Boolean = this.uuid == (other as Waypoint).uuid
    override fun hashCode(): Int = this.uuid.hashCode()
}