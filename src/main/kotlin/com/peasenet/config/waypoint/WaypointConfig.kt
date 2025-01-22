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
package com.peasenet.config.waypoint

import com.peasenet.config.Config
import com.peasenet.mods.render.waypoints.Waypoint

/**
 * The configuration for waypoints.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
class WaypointConfig : Config<WaypointConfig>() {
    /**
     * The list of waypoints.
     */
    private var waypoints: ArrayList<Waypoint>

    init {
        key = "waypoints"
        waypoints = ArrayList()
    }

    /**
     * Adds/updates a waypoint to the list.
     *
     * @param w - The waypoint to add.
     */
    fun addWaypoint(w: Waypoint) {
        waypoints.removeIf { it.uuid == w.uuid }
        waypoints.add(w)
        saveConfig()
    }

    /**
     * Removes a waypoint from the list.
     *
     * @param w - The waypoint to remove.
     */
    fun removeWaypoint(w: Waypoint) {
        waypoints.removeIf { it.uuid == w.uuid }
        saveConfig()
    }

    /**
     * Gets the list of waypoints.
     *
     * @return The list of waypoints.
     */
    fun getLocations(): ArrayList<Waypoint> {
        return waypoints
    }

}