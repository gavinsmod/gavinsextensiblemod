/*
 * MIT License
 *
 * Copyright (c) 2022-2024.
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

package com.peasenet.gavui.color;

import java.io.Serializable;

/**
 * @author gt3ch1
 * @version 01/07/2022
 * A representation of a color. The maximum value for each channel is 255, and the minimum is 0.
 */
public class Color implements Serializable {

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
     * Creates a new RGBA color. Must be between 0 and 255.
     *
     * @param red   Red value.
     * @param green Green value.
     * @param blue  Blue value.
     */
    public Color(int red, int green, int blue) {
        // Make sure the values are within the range via clamping
        this.red = Math.max(0, Math.min(255, red));
        this.green = Math.max(0, Math.min(255, green));
        this.blue = Math.max(0, Math.min(255, blue));
    }

    /**
     * Gets a color from an int value.
     *
     * @param i - the int value
     * @return the color
     */
    public static Color fromInt(int i) {
        return new Color(i >> 16 & 0xFF, i >> 8 & 0xFF, i & 0xFF);
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
     * Gets the float array value of this color. values range from 0 to 1.
     *
     * @return float array of color values
     */
    public float[] getAsFloatArray() {
        return new float[]{getRed(), getGreen(), getBlue(), 1};
    }

    /**
     * Gets the int value of this color, will be converted to its hex equivalent.
     *
     * @return int value of color
     */
    public int getAsInt() {
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Gets the int value of this color, will be converted to its hex equivalent.
     *
     * @param alpha - the alpha value
     * @return int value of color
     */
    public int getAsInt(float alpha) {
        if (alpha > 1)
            alpha = alpha / 255f;
        if (alpha < 0)
            alpha = 1;
        return (int) (alpha * 255) << 24 | (red << 16) | (green << 8) | blue;
    }

    /**
     * Checks if two colors are equal. This is determined if the red, blue, and green channels are the same values.
     *
     * @param other - The other color.
     * @return True if the RGB channels match.
     */
    public boolean equals(Color other) {
        return red == other.red && green == other.green && blue == other.blue;
    }

    public Color brighten(float amount) {
        if (amount < 0)
            amount = 0;
        if (amount > 1)
            amount = 1;
        var newR = red + (int) (amount * 255);
        var newG = green + (int) (amount * 255);
        var newB = blue + (int) (amount * 255);
        if (newR > 255)
            newR = 255;
        if (newG > 255)
            newG = 255;
        if (newB > 255)
            newB = 255;
        // check if the color is too bright, if so, darken it
        if (newR + newG + newB > 255 * 3) {
            newR = red - (int) (amount * 255);
            newG = green - (int) (amount * 255);
            newB = blue - (int) (amount * 255);
        }
        // check if the color is the same. if so, return a new color that is closer to white
        if (newR == red) {
            newG += (int) (amount * 255);
            newB += blue + (int) (amount * 255);
            return new Color(newR, newG, newB);

        }
        if (newG == green) {
            newR += red + (int) (amount * 255);
            newB += blue + (int) (amount * 255);
            return new Color(newR, newG, newB);

        }
        if (newB == blue) {
            newR += red + (int) (amount * 255);
            newG += green + (int) (amount * 255);
            return new Color(newR, newG, newB);

        }
        return new Color(newR, newG, newB);
    }

    public Color invert() {
        return new Color(255 - red, 255 - green, 255 - blue);
    }

    /**
     * Calculates the similarity between two colors. The value will be between 0 and 1, with 0 being the same color and 1 being the opposite color.
     *
     * @param other - the other color
     * @return the similarity
     */
    public float similarity(Color other) {
        var maxRed = Math.max(red, other.red);
        var maxGreen = Math.max(green, other.green);
        var maxBlue = Math.max(blue, other.blue);
        var minRed = Math.min(red, other.red);
        var minGreen = Math.min(green, other.green);
        var minBlue = Math.min(blue, other.blue);
        var r = (maxRed - minRed);
        var g = (maxGreen - minGreen);
        var b = (maxBlue - minBlue);
        var similarity = (r + g + b) / (255f * 3f);
        return similarity;
    }
}
