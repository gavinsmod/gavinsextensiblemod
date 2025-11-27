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
package com.peasenet.gavui

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.util.GavUISettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * The main initializer class for GavUI
 * @author GT3CH1
 * @version 02-02-2025
 * @since 7/13/2022
 */
object GavUI {
    /**
     * The logger of the library.
     */
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("gavui")

    /**
     * Initializes GavUI
     */
    fun initialize() {
        GavUISettings.initialize()
        LOGGER.info("GavUI has been initialized.")
    }

    @JvmStatic
    fun borderColor(withAlpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.border").withAlpha(withAlpha)
    }

    /**
     * Gets the background color from settings./
     *
     * @return The background color from settings.
     */
    fun backgroundColor(withAlpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.background").withAlpha(withAlpha)
    }

    /**
     * Gets the foreground color from settings.
     *
     * @return The foreground color from settings.
     */
    fun textColor(alpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.foreground").withAlpha(alpha)
    }

    val alpha: Float
        /**
         * Gets the alpha from settings.
         *
         * @return The alpha from settings.
         */
        get() = GavUISettings.getFloat("gui.alpha")

    /**
     * Gets the frozen element color from settings.
     *
     * @return The frozen element color from settings.
     */
    fun frozenColor(alpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.frozen").withAlpha(alpha)
    }

    /**
     * Gets the category color from settings.
     *
     * @return The category color from settings.
     */
    fun parentColor(alpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.category").withAlpha(alpha)
    }

    /**
     * Gets the color if an element is enabled from settings.
     *
     * @return The color if an element is enabled from settings.
     */
    fun enabledColor(alpha: Float = 255f): Color {
        return GavUISettings.getColor("gui.color.enabled").withAlpha(alpha)
    }
}
