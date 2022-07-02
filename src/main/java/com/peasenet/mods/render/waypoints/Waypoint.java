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
 * A waypoint is a three-dimensional integer coordinate.
 */
public class Waypoint {
    private final int hash;
    private int x;
    private int y;
    private int z;
    private String name;
    private int color;
    private boolean enabled;
    private boolean tracerEnabled;
    private boolean espEnabled;

    public boolean isTracerEnabled() {
        return tracerEnabled;
    }

    public void setTracerEnabled(boolean tracerEnabled) {
        this.tracerEnabled = tracerEnabled;
    }

    public boolean isEspEnabled() {
        return espEnabled;
    }

    public void setEspEnabled(boolean espEnabled) {
        this.espEnabled = espEnabled;
    }

    public Waypoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        hash = (x * 31) ^ (y * 31) ^ (z * 31);
    }

    public Waypoint(Vec3d vec) {
        x = (int) vec.x;
        y = (int) vec.y;
        z = (int) vec.z;
        hash = (x * 31) ^ (y * 31) ^ (z * 31);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorIndex() {
        return color;
    }

    public Color getColor() {
        return Colors.COLORS[color];
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Waypoint w) {
        x = w.x;
        y = w.y;
        z = w.z;
    }

    public boolean equals(Waypoint w) {
        return name.hashCode() == w.name.hashCode() && x == w.x && y == w.y && z == w.z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Vec3d getVec() {
        return new Vec3d(x, y, z);
    }
}
