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
package com.peasenet.config

import com.peasenet.gavui.color.Colors

/**
 * A class in which controls the configurations of the radar.
 *
 * @author gt3ch1
 * @version 03-02-2023
 */
class RadarConfig : Config<RadarConfig>() {
    /**
     * The current scale of the radar, used to determine the size of the radar.
     */
    var scale = 4
        set(value) {
            field = value
            // cooerce field to be between 1 and 8 inclusive
            if (field > MAX_SCALE)
                field = 1
            if (field < 1)
                field = MAX_SCALE
            saveConfig()
        }

    /**
     * The point size of the radar. Used to draw the boxes on the radar.
     */
    var pointSize = 3
        set(value) {
            field = value
            if (pointSize > 5) field = 1
            field = if (pointSize == 1) 1 else if (pointSize == 3) 3 else 5
            saveConfig()
        }

    /**
     * Gets the color used to draw players on the radar.
     *
     * @return The color used to draw players on the radar.
     */
    /**
     * The default color used to draw players on the radar.
     */
    var playerColor = Colors.GOLD!!
        /**
         * Sets the color used to draw players on the radar.
         *
         * @param playerColor - The color used to draw players on the radar.
         */
        set(playerColor) {
            field = playerColor
            saveConfig()
        }

    var backgroundColor = Colors.DARK_GRAY!!
        set(backgroundColor) {
            field = backgroundColor
            saveConfig()
        }


    /**
     * Gets the color used to draw hostile mobs on the radar.
     *
     * @return The color used to draw hostile mobs on the radar.
     */
    /**
     * The default color used to draw hostile mobs on the radar.
     */
    var hostileMobColor = Colors.RED!!
        /**
         * Sets the color used to draw hostile mobs on the radar.
         *
         * @param hostileMobColor - The color used to draw hostile mobs on the radar.
         */
        set(hostileMobColor) {
            field = hostileMobColor
            saveConfig()
        }
    /**
     * Gets the color used to draw peaceful mobs on the radar.
     *
     * @return The color used to draw peaceful mobs on the radar.
     */
    /**
     * The default color used to draw peaceful mobs on the radar.
     */
    var peacefulMobColor = Colors.GREEN!!
        /**
         * Sets the color used to draw peaceful mobs on the radar.
         *
         * @param peacefulMobColor - The color used to draw peaceful mobs on the radar.
         */
        set(peacefulMobColor) {
            field = peacefulMobColor
            saveConfig()
        }
    /**
     * Gets the color used to draw items on the radar.
     *
     * @return The color used to draw items on the radar.
     */
    /**
     * The default color used to draw items on the radar.
     */
    var itemColor = Colors.CYAN!!
        /**
         * Sets the color used to draw items on the radar.
         *
         * @param itemColor - The color used to draw items on the radar.
         */
        set(itemColor) {
            field = itemColor
            saveConfig()
        }
    /**
     * Sets the color used to draw waypoints on the radar. Will use this color if isUseWaypointColor is false.
     *
     * @return The color used to draw waypoints on the radar.
     * @see .isUseWaypointColor
     */
    /**
     * The default color used to draw waypoints on the radar.
     */
    var waypointColor = Colors.WHITE!!
        /**
         * Sets the color used to draw waypoints on the radar.
         *
         * @param waypointColor - The color used to draw waypoints on the radar.
         */
        set(waypointColor) {
            field = waypointColor
            saveConfig()
        }
    /**
     * Whether to show players on the radar.
     *
     * @return True if players should be shown on the radar.
     */
    /**
     * Whether to show players on the radar.
     */
    var isShowPlayer = true
        /**
         * Sets whether to show players on the radar.
         *
         * @param showPlayer - True if players should be shown on the radar.
         */
        set(showPlayer) {
            field = showPlayer
            saveConfig()
        }
    /**
     * Whether to show hostile mobs on the radar.
     *
     * @return True if hostile mobs should be shown on the radar.
     */
    /**
     * Whether to show hostile mobs on the radar.
     */
    var isShowHostileMob = true
        /**
         * Sets whether to show hostile mobs on the radar.
         *
         * @param showHostileMob - True if hostile mobs should be shown on the radar.
         */
        set(showHostileMob) {
            field = showHostileMob
            saveConfig()
        }
    /**
     * Whether to show peaceful mobs on the radar.
     *
     * @return True if peaceful mobs should be shown on the radar.
     */
    /**
     * Whether to show peaceful mobs on the radar.
     */
    var isShowPeacefulMob = true
        /**
         * Sets whether to show peaceful mobs on the radar.
         *
         * @param showPeacefulMob - True if peaceful mobs should be shown on the radar.
         */
        set(showPeacefulMob) {
            field = showPeacefulMob
            saveConfig()
        }
    /**
     * Whether to show items on the radar.
     *
     * @return True if items should be shown on the radar.
     */
    /**
     * Whether to show items on the radar.
     */
    var isShowItem = true
        /**
         * Sets whether to show items on the radar.
         *
         * @param showItem - True if items should be shown on the radar.
         */
        set(showItem) {
            field = showItem
            saveConfig()
        }
    /**
     * Whether to show waypoints on the radar.
     *
     * @return True if waypoints should be shown on the radar.
     */
    /**
     * Whether to show waypoints on the radar.
     */
    var isShowWaypoint = true
        /**
         * Sets whether to show waypoints on the radar.
         *
         * @param showWaypoint - True if waypoints should be shown on the radar.
         */
        set(showWaypoint) {
            field = showWaypoint
            saveConfig()
        }
    /**
     * Whether to use the waypoint color to draw waypoints on the radar.
     *
     * @return True if the waypoint color should be used to draw waypoints on the radar.
     */
    /**
     * Whether to use the waypoint color to draw waypoints on the radar.
     */
    var isUseWaypointColor = true
        /**
         * Sets whether to use the waypoint color to draw waypoints on the radar.
         *
         * @param useWaypointColor - True if the color defined by the waypoint should be used to draw waypoints on the radar.
         */
        set(useWaypointColor) {
            field = useWaypointColor
            saveConfig()
        }

    /**
     * The alpha value for points on the radar.
     */
    var pointAlpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * Gets the current alpha value for the radar background.
     *
     * @return The current alpha value for the radar background.
     */
    /**
     * The alpha value for the radar background.
     */
    var backgroundAlpha = 0.5f
        /**
         * Sets the current alpha value for the radar background.
         *
         * @param backgroundAlpha The new alpha value for the radar background.
         */
        set(backgroundAlpha) {
            field = backgroundAlpha
            saveConfig()
        }

    /*
     * Create a new instance of the radar settings if one does not already exist.
     */
    init {
        key = "radar"
    }

    val size: Int
        get() = scale * 16 + 1


    /**
     * The call back used to increase the scale.
     */
    fun increaseScaleCallback() {
        scale = scale + 1
        if (scale > MAX_SCALE) scale = 1
        saveConfig()
    }

    /**
     * The callback used to update the point size.
     */
    fun updatePointSizeCallback() {
        pointSize += 2
        saveConfig()
    }

    companion object {
        /**
         * The Y position of the radar.
         */
        @JvmField
        var y = 12

        @JvmField
        var MAX_SCALE = 10

        /**
         * The X position of the radar.
         */
        @JvmField
        var x = 0

    }
}