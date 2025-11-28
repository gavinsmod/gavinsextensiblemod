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
package com.peasenet.config.render

import com.peasenet.config.Config
import com.peasenet.extensions.wrapAround
import com.peasenet.gavui.color.Colors

/**
 * A class in which controls the configurations of the radar.
 *
 * @author GT3CH1
 * @version 01-15-2025
 * @since 03-02-2023
 */
class RadarConfig : Config<RadarConfig>() {
    /**
     * The current scale of the radar, used to determine the size of the radar.
     */
    var scale = 4
        set(value) {
            field = value.wrapAround(1, MAX_SCALE)
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
    var playerColor = Colors.GOLD
        set(playerColor) {
            field = playerColor
            saveConfig()
        }

    var backgroundColor = Colors.DARK_GRAY
        set(backgroundColor) {
            field = backgroundColor
            saveConfig()
        }


    /**
     * Gets the color used to draw hostile mobs on the radar.
     *
     * @return The color used to draw hostile mobs on the radar.
     */
    var hostileMobColor = Colors.RED
        set(hostileMobColor) {
            field = hostileMobColor
            saveConfig()
        }

    /**
     * Gets the color used to draw peaceful mobs on the radar.
     *
     * @return The color used to draw peaceful mobs on the radar.
     */
    var peacefulMobColor = Colors.GREEN
        set(peacefulMobColor) {
            field = peacefulMobColor
            saveConfig()
        }

    /**
     * Gets the color used to draw items on the radar.
     *
     * @return The color used to draw items on the radar.
     */
    var itemColor = Colors.CYAN
        set(itemColor) {
            field = itemColor
            saveConfig()
        }

    /**
     * Whether to show players on the radar.
     */
    var isShowPlayer = true
        set(showPlayer) {
            field = showPlayer
            saveConfig()
        }

    /**
     * Whether to show hostile mobs on the radar.
     *
     * @return True if hostile mobs should be shown on the radar.
     */
    var isShowHostileMob = true
        set(showHostileMob) {
            field = showHostileMob
            saveConfig()
        }

    /**
     * Whether to show peaceful mobs on the radar.
     *
     * @return True if peaceful mobs should be shown on the radar.
     */
    var isShowPeacefulMob = true
        set(showPeacefulMob) {
            field = showPeacefulMob
            saveConfig()
        }

    /**
     * Whether to show items on the radar.
     *
     * @return True if items should be shown on the radar.
     */
    var isShowItem = true
        set(showItem) {
            field = showItem
            saveConfig()
        }

    var showEggs = true
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * The alpha value of points on the radar.
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
    var backgroundAlpha = 0.5f
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
        get() { return scale * 16 }

    companion object {
        /**
         * The Y position of the radar.
         */
        @JvmField
        var y = 12

        var MAX_SCALE = 10

        /**
         * The X position of the radar.
         */
        @JvmField
        var x = 0

    }
}