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

import net.minecraft.util.math.Vec3d;

/**
 * @author gt3ch1
 * @version 6/30/2022
 * A waypoint is a three-dimensional integer coordinate.
 */
public class Waypoint {
    private int x, y, z, hash;

    public boolean isEnabled() {
        return enabled;
    }

    private boolean enabled;

    public Waypoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        hash = (x * 31) ^ (y * 31) ^ (z * 31);
//        worldName = w.na
    }

    public Waypoint(Vec3d vec) {
        x = (int) vec.x;
        y = (int) vec.y;
        z = (int) vec.z;
        hash = (x * 31) ^ (y * 31) ^ (z * 31);
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
        return x == w.x && y == w.y && z == w.z;
    }

    public boolean equals(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
