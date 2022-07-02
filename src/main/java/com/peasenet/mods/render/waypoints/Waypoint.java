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

package com.peasenet.mods.render.waypoints;

import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import net.minecraft.util.math.Vec3d;

/**
 * @author gt3ch1
 * @version 6/30/2022
 * A waypoint is a three-dimensional integer coordinate with a name, color, and can either have
 * an ESP, a tracer, or both.
 */
public class Waypoint {

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * The z coordinate.
     */
    private int z;

    /**
     * The name of the waypoint.
     */
    private String name;

    /**
     * The color index of the waypoint.
     */
    private int color;

    /**
     * Whether the waypoint is currently enabled, meaning it can be rendered.
     */
    private boolean enabled;

    /**
     * Whether the tracer is enabled.
     */
    private boolean tracerEnabled;

    /**
     * Whether the ESP is enabled.
     */
    private boolean espEnabled;

    /**
     * Creates a new waypoint at the given coordinates.
     *
     * @param x - The x coordinate.
     * @param y - The y coordinate.
     * @param z - The z coordinate.
     */
    public Waypoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new waypoint at the given Vec3d.
     *
     * @param vec - The Vec3d containing the position of the waypoint.
     */
    public Waypoint(Vec3d vec) {
        x = (int) vec.x;
        y = (int) vec.y;
        z = (int) vec.z;
    }

    /**
     * Gets whether the tracer for this waypoint is enabled.
     *
     * @return True if the tracer should be drawn.
     */
    public boolean isTracerEnabled() {
        return tracerEnabled;
    }

    /**
     * Sets whether the tracer should be enabled for this waypoint.
     *
     * @param tracerEnabled - Whether the tracer should be enabled.
     */
    public void setTracerEnabled(boolean tracerEnabled) {
        this.tracerEnabled = tracerEnabled;
    }

    /**
     * Gets whether the ESP for this waypoint is enabled.
     *
     * @return True if the ESP should be drawn.
     */
    public boolean isEspEnabled() {
        return espEnabled;
    }

    /**
     * Sets whether the ESP should be enabled for this waypoint.
     *
     * @param espEnabled - Whether the ESP should be enabled.
     */
    public void setEspEnabled(boolean espEnabled) {
        this.espEnabled = espEnabled;
    }

    /**
     * The name of the waypoint.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the waypoint.
     *
     * @param name - The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The color index of the waypoint.
     *
     * @return The index of the color.
     */
    public int getColorIndex() {
        return color;
    }

    /**
     * Gets the color of the waypoint.
     *
     * @return The color.
     */
    public Color getColor() {
        return Colors.COLORS[color];
    }

    /**
     * Sets the color of the waypoint.
     *
     * @param color - The color index to set the color of the waypoint to.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Gets whether the waypoint is enabled.
     *
     * @return True if the waypoint is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the waypoint is enabled.
     *
     * @param enabled - Whether the waypoint is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the x coordinate of the waypoint.
     *
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }


    /**
     * Gets the y coordinate of the waypoint.
     *
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the z coordinate of the waypoint.
     *
     * @return The z coordinate.
     */
    public int getZ() {
        return z;
    }

    /**
     * Whether this waypoint and the given waypoint are equal. The comparison is done on the name,
     * x, y, and z coordinates.
     *
     * @param w - The waypoint to compare to.
     * @return True if the waypoints are equal.
     */
    public boolean equals(Waypoint w) {
        return name.hashCode() == w.name.hashCode() && x == w.x && y == w.y && z == w.z;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Gets the location of this waypoint as a vector.
     *
     * @return The waypoint as a Vec3d.
     */
    public Vec3d getVec() {
        return new Vec3d(x, y, z);
    }
}
