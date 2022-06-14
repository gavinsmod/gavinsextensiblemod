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
     * Gets the center of the box.
     *
     * @return The center of the box.
     */
    public PointD getCenter() {
        return new PointD(topLeft.x() + getWidth() / 2, topLeft.y() + getHeight() / 2);
    }
}
