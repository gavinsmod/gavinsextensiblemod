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
 * @param x The x coordinate of the point.
 * @param y The y coordinate of the point.
 * @author gt3ch1
 * @version 12/30/2022
 * A point in 2D space.
 */
public record PointF(float x, float y) {

    /**
     * Creates a new point in 2D space.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public PointF {
    }

    public PointF(double x, double y) {
        this((float) x, (float) y);
    }

    public PointF(int x, int y) {
        this((float) x, (float) y);
    }

    /**
     * Adds two points together
     *
     * @param other - The other point to add.
     * @return The sum of the two points.
     */
    public PointF add(PointF other) {
        return new PointF(x + other.x, y + other.y);
    }

    public PointF add(double x, double y) {
        return new PointF(this.x + x, this.y + y);
    }

    public PointF add(int x, int y) {
        return new PointF(this.x + x, this.y + y);
    }

    public PointF add(float x, float y) {
        return new PointF(this.x + x, this.y + y);
    }

    /**
     * Subtracts two points together
     *
     * @param other - The other point to subtract.
     * @return A point with the difference of the two points.
     */
    public PointF subtract(PointF other) {
        return new PointF(x - other.x, y - other.y);
    }

    /**
     * Multiplies two points together
     *
     * @param other - The other point to multiply.
     * @return A point with the product of the two points.
     */
    public PointF multiply(PointF other) {
        return new PointF(x * other.x, y * other.y);
    }

    public PointF multiply(float other) {
        return new PointF(x * other, y * other);
    }

    /**
     * Divides two points together
     *
     * @param other - The other point to divide.
     * @return A point with the quotient of the two points.
     */
    public PointF divide(PointF other) {
        return new PointF(x / other.x, y / other.y);
    }

    public PointF divide(float other) {
        return new PointF(x / other, y / other);
    }

    /**
     * Calculates the distance between two points.
     *
     * @param other - The other point to calculate the distance to.
     * @return The distance between the two points.
     */
    public float distance(PointF other) {
        return (float) Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

}
