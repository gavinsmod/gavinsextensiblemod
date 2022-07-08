/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.util.color;

import java.util.Random;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * A class containing pre-defined colors.
 * Note: Use this site to generate color names: <a href="https://www.color-name.com/hex/">color-name.com</a>
 */
public class Colors {

    public static final Color RED = new Color(255, 0, 0);
    public static final Color DARK_RED = new Color(90, 50, 50);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color DARK_GREEN = new Color(0, 127, 0);
    public static final Color BLUE = new Color(0, 0, 255);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color PURPLE = new Color(255, 0, 255);
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color GRAY = new Color(128, 128, 128);
    public static final Color DARK_GRAY = new Color(16, 16, 16);
    public static final Color DARK_CYAN = new Color(0, 128, 255);
    public static final Color GOLD = new Color(255, 215, 0);
    public static final Color INDIGO = Color.fromInt(0x273859);
    public static final Color SHADOW_BLUE = Color.fromInt(0x7686A6);
    public static final Color DARK_SPRING_GREEN = Color.fromInt(0x1D734B);
    public static final Color MEDIUM_SEA_GREEN = Color.fromInt(0x32a670);
    public static final Color DESERT_SAND = Color.fromInt(0xD9D2B0);

    /**
     * The list of all colors.
     */
    public static final Color[] COLORS = {
            RED, DARK_RED, GREEN, DARK_GREEN, BLUE, YELLOW, PURPLE, CYAN, WHITE, BLACK, GRAY, DARK_GRAY, DARK_CYAN, GOLD,
            INDIGO, SHADOW_BLUE, DARK_SPRING_GREEN, MEDIUM_SEA_GREEN, DESERT_SAND
    };

    /**
     * Returns the color with the given index.
     *
     * @param color - the index of the color to return
     * @return the color with the given index, -1 if the search failed.
     */
    public static int getColorIndex(Color color) {
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i].equals(color)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets a random color index from the list of all colors. See #COLORS.
     *
     * @return A random color index.
     */
    public static int getRandomColor() {
        var num = new Random(System.currentTimeMillis()).nextInt(COLORS.length);
        return num;
    }
}
