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

package com.peasenet.gavui.math;

/**
 * @author gt3ch1
 * @version 12/30/2022
 * A BoxD is a box created by two PointF's - a top left and a bottom right.
 * The coordinates of the box are as follows:
 * x1 - The x coordinate of the top left corner.
 * y1 - The y coordinate of the top left corner.
 * x2 - The x coordinate of the bottom right corner.
 * y2 - The y coordinate of the bottom right corner.
 */
public class BoxF {
    private float width;
    private float height;
    /**
     * The top left corner of the box.
     */
    private PointF topLeft;
    /**
     * The bottom right corner of the box.
     */
    private PointF bottomRight;

    /**
     * Creates a new box.
     *
     * @param topLeft - The top left corner of the box.
     * @param width   - The width of the box.
     * @param height  - The height of the box.
     */
    public BoxF(PointF topLeft, float width, float height) {
        this.width = width;
        this.height = height;
        this.topLeft = topLeft;
        this.bottomRight = topLeft.add(new PointF(width, height));
    }

    public BoxF(float x, float y, float width, float height) {
        this(new PointF(x, y), width, height);
    }

    /**
     * Creates a copy of the given box.
     *
     * @param other - The box to copy.
     * @return A copy of the given box.
     */
    public static BoxF copy(BoxF other) {
        return new BoxF(other.topLeft, other.width, other.height);
    }

    /**
     * Gets the top left corner of the box.
     *
     * @return The top left corner of the box.
     */
    public PointF getTopLeft() {
        return topLeft;
    }

    /**
     * Sets the top left corner of the box.
     *
     * @param point - The new location of the box.
     */
    public void setTopLeft(PointF point) {
        this.topLeft = point;
        this.bottomRight = topLeft.add(new PointF(width, height));
    }

    /**
     * Gets the bottom right corner of the box.
     *
     * @return The bottom right corner of the box.
     */
    public PointF getBottomRight() {
        return bottomRight;
    }

    /**
     * Gets the width of the box.
     *
     * @return The width of the box.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the box.
     *
     * @return The height of the box.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the mid-point of the box.
     *
     * @param middle - The new mid-point of the box.
     */
    public void setMiddle(PointF middle) {
        setTopLeft(middle.subtract(new PointF(width / 2, height / 2)));
    }

    public float getX1() {
        return topLeft.x();
    }

    public float getY1() {
        return topLeft.y();
    }

    public float getX2() {
        return bottomRight.x();
    }

    public float getY2() {
        return bottomRight.y();
    }

    public void from(BoxF other) {
        this.topLeft = other.topLeft;
        this.bottomRight = other.bottomRight;
        this.width = other.width;
        this.height = other.height;
    }

}
