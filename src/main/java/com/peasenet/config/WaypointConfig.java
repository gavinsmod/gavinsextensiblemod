/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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

package com.peasenet.config;

import com.peasenet.mods.render.waypoints.Waypoint;

import java.util.ArrayList;

/**
 * The configuration for waypoints.
 *
 * @author gt3ch1
 * @version 12/31/2022
 */
public class WaypointConfig extends Config<WaypointConfig> {

    /**
     * The instance of the configuration.
     */
    private static WaypointConfig instance;

    /**
     * The list of waypoints.
     */
    private ArrayList<Waypoint> locations;

    public WaypointConfig() {
        setKey("waypoints");
        locations = new ArrayList<>();
        setInstance(this);
    }

    /**
     * Adds a waypoint to the list.
     * @param w - The waypoint to add.
     */
    public void addWaypoint(Waypoint w) {
        this.getInstance().locations.add(w);
        saveConfig();
    }

    /**
     * Removes a waypoint from the list.
     * @param w - The waypoint to remove.
     */
    public void removeWaypoint(Waypoint w) {
        this.getInstance().locations.remove(w);
        saveConfig();

    }

    /**
     * Gets the list of waypoints.
     * @return The list of waypoints.
     */
    public ArrayList<Waypoint> getLocations() {
        return this.getInstance().locations;
    }

    /**
     * Sets the list of waypoints.
     * @param locations - The list of waypoints.
     */
    public void setLocations(ArrayList<Waypoint> locations) {
        this.getInstance().locations = locations;
        saveConfig();
    }

    @Override
    public WaypointConfig getInstance() {
        return instance;
    }

    @Override
    public void setInstance(WaypointConfig data) {
        instance = data;
    }
}
