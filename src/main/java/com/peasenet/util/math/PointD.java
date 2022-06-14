package com.peasenet.util.math;

/**
 * @param x The x coordinate of the point.
 * @param y The y coordinate of the point.
 * @author gt3ch1
 * @version 6/13/2022
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
}
