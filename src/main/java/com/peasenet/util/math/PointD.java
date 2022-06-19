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

package com.peasenet.util.math;

/**
 * @param x The x coordinate of the point.
 * @param y The y coordinate of the point.
 * @author gt3ch1
 * @version 6/14/2022
 * A point in 2D space.
 */
public record PointD(double x, double y) {

    /**
     * Creates a new point in 2D space.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public PointD {
    }

    /**
     * Adds two points together
     *
     * @param other - The other point to add.
     * @return The sum of the two points.
     */
    public PointD add(PointD other) {
        return new PointD(x + other.x, y + other.y);
    }

    /**
     * Subtracts two points together
     *
     * @param other - The other point to subtract.
     * @return A point with the difference of the two points.
     */
    public PointD subtract(PointD other) {
        return new PointD(x - other.x, y - other.y);
    }
}
