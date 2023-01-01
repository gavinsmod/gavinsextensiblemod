/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;

/**
 * A class in which controls the configurations of the radar.
 *
 * @author gt3ch1
 * @version 12/31/2022
 */
public class RadarConfig extends Config<RadarConfig> {

    /**
     * The Y position of the radar.
     */
    private static int y = 12;

    /**
     * The X position of the radar.
     */
    private static int x = 0;

    /**
     * The size of the radar in pixels. scale*16+1.
     */
    private static int size = 129;

    /**
     * The current instance of the radar settings.
     */
    private static RadarConfig instance;

    /**
     * The current scale of the radar, used to determine the size of the radar.
     */
    private int scale = 4;

    /**
     * The point size of the radar. Used to draw the boxes on the radar.
     */
    private int pointSize = 3;

    /**
     * The default color used to draw players on the radar.
     */
    private Color playerColor = Colors.GOLD;

    /**
     * The default color used to draw hostile mobs on the radar.
     */
    private Color hostileMobColor = Colors.RED;

    /**
     * The default color used to draw peaceful mobs on the radar.
     */
    private Color peacefulMobColor = Colors.GREEN;

    /**
     * The default color used to draw items on the radar.
     */
    private Color itemColor = Colors.CYAN;

    /**
     * The default color used to draw waypoints on the radar.
     */
    private Color waypointColor = Colors.WHITE;

    /**
     * Whether to show players on the radar.
     */
    private boolean showPlayer = true;

    /**
     * Whether to show hostile mobs on the radar.
     */
    private boolean showHostileMob = true;

    /**
     * Whether to show peaceful mobs on the radar.
     */
    private boolean showPeacefulMob = true;

    /**
     * Whether to show items on the radar.
     */
    private boolean showItem = true;

    /**
     * Whether to show waypoints on the radar.
     */
    private boolean showWaypoint = true;

    /**
     * Whether to use the waypoint color to draw waypoints on the radar.
     */
    private boolean useWaypointColor = true;

    /**
     * The alpha value for points on the radar.
     */
    private float pointAlpha = 0.5f;

    /**
     * The alpha value for the radar background.
     */
    private float backgroundAlpha = 0.5f;

    /**
     * Create a new instance of the radar settings if one does not already exist.
     */
    public RadarConfig() {
        setKey("radar");
        if (instance == null) instance = this;
    }

    /**
     * Gets the current alpha value for the radar background.
     *
     * @return The current alpha value for the radar background.
     */
    public float getPointAlpha() {
        return pointAlpha;
    }

    /**
     * Sets the current alpha value for the radar background.
     *
     * @param pointAlpha The new alpha value for the radar background.
     */
    public void setPointAlpha(float pointAlpha) {
        this.pointAlpha = pointAlpha;
    }

    /**
     * Gets the current alpha value for the radar background.
     *
     * @return The current alpha value for the radar background.
     */
    public float getBackgroundAlpha() {
        return backgroundAlpha;
    }

    /**
     * Sets the current alpha value for the radar background.
     *
     * @param backgroundAlpha The new alpha value for the radar background.
     */
    public void setBackgroundAlpha(float backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
        saveConfig();
    }

    /**
     * Gets the Y position of the radar.
     *
     * @return The Y position of the radar.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the Y position of the radar.
     *
     * @param y - The Y position of the radar.
     */
    public static void setY(int y) {
        RadarConfig.y = y;
    }

    /**
     * Gets the X position of the radar.
     *
     * @return The X position of the radar.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the X position of the radar.
     *
     * @param x - The X position of the radar.
     */
    public static void setX(int x) {
        RadarConfig.x = x;
    }

    /**
     * Gets the size of the radar in pixels.
     *
     * @return The size of the radar in pixels.
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the size of the radar in pixels.
     *
     * @param size - The size of the radar in pixels.
     */
    public void setSize(int size) {
        RadarConfig.size = size;
    }

    /**
     * Gets the scale of the radar.
     *
     * @return The scale of the radar in pixels.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Sets the scale of the radar.
     *
     * @param scale - The scale of the radar.
     */
    public void setScale(int scale) {
        this.scale = Math.max(1, Math.min(8, scale));
        this.setSize(scale * 16 + 1);
    }

    /**
     * The point size of the radar. Used to draw entities/objects on the radar.
     *
     * @return The point size of the radar.
     */
    public int getPointSize() {
        return pointSize;
    }

    /**
     * Sets the point size of the radar. This value can either be 1, 3, or 5.
     *
     * @param pointSize - The point size of the radar.
     */
    public void setPointSize(int pointSize) {
        if (pointSize > 5)
            pointSize = 1;
        this.pointSize = pointSize == 1 ? 1 : pointSize == 3 ? 3 : 5;
        saveConfig();
    }

    /**
     * Gets the color used to draw players on the radar.
     *
     * @return The color used to draw players on the radar.
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * Sets the color used to draw players on the radar.
     *
     * @param playerColor - The color used to draw players on the radar.
     */
    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;

        saveConfig();
    }

    /**
     * Gets the color used to draw hostile mobs on the radar.
     *
     * @return The color used to draw hostile mobs on the radar.
     */
    public Color getHostileMobColor() {
        return hostileMobColor;
    }

    /**
     * Sets the color used to draw hostile mobs on the radar.
     *
     * @param hostileMobColor - The color used to draw hostile mobs on the radar.
     */
    public void setHostileMobColor(Color hostileMobColor) {
        this.hostileMobColor = hostileMobColor;
        saveConfig();
    }

    /**
     * Gets the color used to draw peaceful mobs on the radar.
     *
     * @return The color used to draw peaceful mobs on the radar.
     */
    public Color getPeacefulMobColor() {
        return peacefulMobColor;
    }

    /**
     * Sets the color used to draw peaceful mobs on the radar.
     *
     * @param peacefulMobColor - The color used to draw peaceful mobs on the radar.
     */
    public void setPeacefulMobColor(Color peacefulMobColor) {
        this.peacefulMobColor = peacefulMobColor;
        saveConfig();
    }

    /**
     * Gets the color used to draw items on the radar.
     *
     * @return The color used to draw items on the radar.
     */
    public Color getItemColor() {
        return itemColor;
    }

    /**
     * Sets the color used to draw items on the radar.
     *
     * @param itemColor - The color used to draw items on the radar.
     */
    public void setItemColor(Color itemColor) {
        this.itemColor = itemColor;
        saveConfig();
    }

    /**
     * Sets the color used to draw waypoints on the radar. Will use this color if isUseWaypointColor is false.
     *
     * @return The color used to draw waypoints on the radar.
     * @see #isUseWaypointColor()
     */
    public Color getWaypointColor() {
        return waypointColor;
    }

    /**
     * Sets the color used to draw waypoints on the radar.
     *
     * @param waypointColor - The color used to draw waypoints on the radar.
     */
    public void setWaypointColor(Color waypointColor) {
        this.waypointColor = waypointColor;
        saveConfig();
    }

    /**
     * Whether to show players on the radar.
     *
     * @return True if players should be shown on the radar.
     */
    public boolean isShowPlayer() {
        return showPlayer;
    }

    /**
     * Sets whether to show players on the radar.
     *
     * @param showPlayer - True if players should be shown on the radar.
     */
    public void setShowPlayer(boolean showPlayer) {
        this.showPlayer = showPlayer;
        saveConfig();
    }

    /**
     * Whether to show hostile mobs on the radar.
     *
     * @return True if hostile mobs should be shown on the radar.
     */
    public boolean isShowHostileMob() {
        return showHostileMob;
    }

    /**
     * Sets whether to show hostile mobs on the radar.
     *
     * @param showHostileMob - True if hostile mobs should be shown on the radar.
     */
    public void setShowHostileMob(boolean showHostileMob) {
        this.showHostileMob = showHostileMob;
        saveConfig();
    }

    /**
     * Whether to show peaceful mobs on the radar.
     *
     * @return True if peaceful mobs should be shown on the radar.
     */
    public boolean isShowPeacefulMob() {
        return showPeacefulMob;
    }

    /**
     * Sets whether to show peaceful mobs on the radar.
     *
     * @param showPeacefulMob - True if peaceful mobs should be shown on the radar.
     */
    public void setShowPeacefulMob(boolean showPeacefulMob) {
        this.showPeacefulMob = showPeacefulMob;
        saveConfig();

    }

    /**
     * Whether to show items on the radar.
     *
     * @return True if items should be shown on the radar.
     */
    public boolean isShowItem() {
        return showItem;
    }

    /**
     * Sets whether to show items on the radar.
     *
     * @param showItem - True if items should be shown on the radar.
     */
    public void setShowItem(boolean showItem) {
        this.showItem = showItem;
        saveConfig();
    }

    /**
     * Whether to show waypoints on the radar.
     *
     * @return True if waypoints should be shown on the radar.
     */
    public boolean isShowWaypoint() {
        return showWaypoint;
    }

    /**
     * Sets whether to show waypoints on the radar.
     *
     * @param showWaypoint - True if waypoints should be shown on the radar.
     */
    public void setShowWaypoint(boolean showWaypoint) {
        this.showWaypoint = showWaypoint;
        saveConfig();
    }

    /**
     * Whether to use the waypoint color to draw waypoints on the radar.
     *
     * @return True if the waypoint color should be used to draw waypoints on the radar.
     */
    public boolean isUseWaypointColor() {
        return useWaypointColor;
    }

    /**
     * Sets whether to use the waypoint color to draw waypoints on the radar.
     *
     * @param useWaypointColor - True if the color defined by the waypoint should be used to draw waypoints on the radar.
     */
    public void setUseWaypointColor(boolean useWaypointColor) {
        this.useWaypointColor = useWaypointColor;
        saveConfig();
    }

    /**
     * Gets the current instance of the radar settings.
     *
     * @return The current instance of the radar settings.
     */
    public RadarConfig getInstance() {
        return instance;
    }

    /**
     * Sets the instance of the radar.
     *
     * @param radar The radar instance.
     */
    public void setInstance(RadarConfig radar) {
        instance = radar;
    }

    /**
     * The call back used to increase the scale.
     */
    public void increaseScaleCallback() {
        scale++;
        if (scale > 8) scale = 1;
        setScale(scale);
        saveConfig();
    }

    /**
     * The callback used to update the point size.
     */
    public void updatePointSizeCallback() {
        pointSize += 2;
        setPointSize(pointSize);
        saveConfig();
    }
}
