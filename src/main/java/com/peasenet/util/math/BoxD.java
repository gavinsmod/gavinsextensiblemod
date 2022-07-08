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
 * @author gt3ch1
 * @version 6/14/2022
 * A BoxD is a box created by two PointD's - a top left and a bottom right.
 */
public class BoxD {
    private final double width;
    private final double height;
    /**
     * The top left corner of the box.
     */
    private PointD topLeft;
    /**
     * The bottom right corner of the box.
     */
    private PointD bottomRight;

    /**
     * Creates a new box.
     *
     * @param topLeft - The top left corner of the box.
     */
    public BoxD(PointD topLeft, double width, double height) {
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        this.bottomRight = topLeft.add(new PointD(width, height));
    }

    public static BoxD copy(BoxD other) {
        return new BoxD(other.topLeft, other.width, other.height);
    }

    /**
     * Gets the top left corner of the box.
     *
     * @return The top left corner of the box.
     */
    public PointD getTopLeft() {
        return topLeft;
    }

    /**
     * Sets the top left corner of the box.
     *
     * @param point - The new location of the box.
     */
    public void setTopLeft(PointD point) {
        this.topLeft = point;
        this.bottomRight = topLeft.add(new PointD(width, height));
    }

    /**
     * Gets the bottom right corner of the box.
     *
     * @return The bottom right corner of the box.
     */
    public PointD getBottomRight() {
        return bottomRight;
    }

    /**
     * Gets the width of the box.
     *
     * @return The width of the box.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the box.
     *
     * @return The height of the box.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the mid-point of the box.
     *
     * @param middle - The new mid-point of the box.
     */
    public void setMiddle(PointD middle) {
        setTopLeft(middle.subtract(new PointD(width / 2, height / 2)));
    }

}
