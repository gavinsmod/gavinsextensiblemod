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
import net.minecraft.util.math.Vec3i;

/**
 * @author gt3ch1
 * @version 7/2/2022
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
    private Color color;

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
     * Creates a new waypoint at the given Vec3i coordinate.
     *
     * @param vec - The positional vector.
     */
    public Waypoint(Vec3i vec) {
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
    }

    /**
     * Gets whether the tracer is enabled.
     *
     * @return True if the tracer is enabled.
     */
    public boolean isTracerEnabled() {
        return tracerEnabled;
    }

    /**
     * Sets whether the tracer should be enabled.
     *
     * @param tracerEnabled - Whether the tracer is enabled.
     */
    public void setTracerEnabled(boolean tracerEnabled) {
        this.tracerEnabled = tracerEnabled;
    }

    /**
     * Gets whether the ESP is enabled.
     * @return True if the ESP is enabled.
     */
    public boolean isEspEnabled() {
        return espEnabled;
    }

    /**
     * Sets whether the waypoint esp is enabled.
     * @param espEnabled - Whether to enable the esp.
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
        if (name.isEmpty())
            name = "Waypoint";
        this.name = name;
    }

    /**
     * Gets the color of the waypoint.
     *
     * @return The color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the waypoint.
     *
     * @param color - The color index to set the color of the waypoint to.
     */
    public void setColor(Color color) {
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
     * Sets the X coordinate.
     *
     * @param x - The X coordinate.
     */
    public void setX(int x) {
        this.x = x;
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
     * Sets the Y coordinate.
     *
     * @param y - The Y coordinate.
     */
    public void setY(int y) {
        this.y = y;
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
     * Sets the z coordinate.
     *
     * @param z - The z coordinate.
     */
    public void setZ(int z) {
        this.z = z;
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

}
