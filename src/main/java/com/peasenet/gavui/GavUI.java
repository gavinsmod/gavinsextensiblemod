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

package com.peasenet.gavui;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.util.GavUISettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gt3ch1
 * @version 7/13/2022
 * The main initializer class for GavUI
 */
public class GavUI {
    /**
     * The logger of the library.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger("gavui");

    /**
     * Initializes GavUI
     */
    public static void initialize() {
        GavUISettings.initialize();
        LOGGER.info("GavUI has been initialized.");
    }

    public static Color borderColor() {
        return GavUISettings.getColor("gui.color.border");
    }

    /**
     * Gets the background color from settings./
     *
     * @return The background color from settings.
     */
    public static Color backgroundColor() {
        return GavUISettings.getColor("gui.color.background");
    }

    /**
     * Gets the foreground color from settings.
     *
     * @return The foreground color from settings.
     */
    public static Color textColor() {
        return GavUISettings.getColor("gui.color.foreground");
    }

    /**
     * Gets the alpha from settings.
     *
     * @return The alpha from settings.
     */
    public static float getAlpha() {
        return GavUISettings.getFloat("gui.alpha");
    }

    /**
     * Gets the frozen element color from settings.
     *
     * @return The frozen element color from settings.
     */
    public static Color frozenColor() {
        return GavUISettings.getColor("gui.color.frozen");
    }

    /**
     * Gets the category color from settings.
     *
     * @return The category color from settings.
     */
    public static Color parentColor() {
        return GavUISettings.getColor("gui.color.category");
    }

    /**
     * Gets the color if an element is enabled from settings.
     *
     * @return The color if an element is enabled from settings.
     */
    public static Color enabledColor() {
        return GavUISettings.getColor("gui.color.enabled");
    }
}
