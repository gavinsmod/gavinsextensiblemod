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

/**
 * @author gt3ch1
 * @version 5/24/2022
 * <p>
 * A representation of a color. The maximum value for each channel is 255, and the minimum is 0.
 */
public class Color {

    /**
     * The red value of this color.
     */
    private final int red;

    /**
     * The green value of this color.
     */
    private final int green;

    /**
     * The blue value of this color.
     */
    private final int blue;

    /**
     * The alpha value of this color.
     */
    private final int alpha;

    /**
     * Creates a new RGBA color. Must be between 0 and 255.
     * 0 being completely "off" and 255 being completely "on".
     *
     * @param red   Red value.
     * @param green Green value.
     * @param blue  Blue value.
     * @param alpha (optional) - defaults to 255
     */
    public Color(int red, int green, int blue, int alpha) {
        this.red = red % 256;
        this.green = green % 256;
        this.blue = blue % 256;
        this.alpha = alpha % 256;
    }

    /**
     * Creates a new RGBA color. Must be between 0 and 255.
     *
     * @param red   Red value.
     * @param green Green value.
     * @param blue  Blue value.
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Gets the red value of this color. Will be within 0 and 1.
     *
     * @return red value
     */
    public float getRed() {
        return red / 255f;
    }

    /**
     * Gets the green value of this color. Will be within 0 and 1.
     *
     * @return green value
     */
    public float getGreen() {
        return green / 255f;
    }

    /**
     * Gets the blue value of this color. Will be within 0 and 1.
     *
     * @return blue value
     */
    public float getBlue() {
        return blue / 255f;
    }

    /**
     * Gets the alpha value of this color. Will be within 0 and 1.
     *
     * @return alpha value
     */
    public float getAlpha() {
        return alpha / 255f;
    }

    /**
     * Gets the float array value of this color. values range from 0 to 1.
     *
     * @return float array of color values
     */
    public float[] getAsFloatArray() {
        return new float[]{getRed(), getGreen(), getBlue(), getAlpha()};
    }

    /**
     * Gets the int value of this color, will be converted to its hex equivalent.
     *
     * @param a - whether to include the alpha value
     * @return int value of color
     */
    public int getAsInt(boolean a) {
        if (a) {
            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        } else {
            return (red << 16) | (green << 8) | blue;
        }
    }

    /**
     * Gets the integer representation of this color without the alpha value.
     *
     * @return int value of color
     */
    public int getAsInt() {
        return getAsInt(false);
    }
}
