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

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;

/**
 * @author gt3ch1
 * @version 01/03/2023
 */
public class TracerEspConfig<E> extends Config<TracerEspConfig<E>> {
    /**
     * The color for chests - default is purple.
     */
    public Color chestColor = Colors.PURPLE;
    /**
     * The color for hostile mobs - default is red.
     */
    public Color hostileMobColor = Colors.RED;
    /**
     * The color for passive mobs - default is green.
     */
    public Color peacefulMobColor = Colors.GREEN;
    /**
     * The color for players - default is yellow.
     */
    public Color playerColor = Colors.YELLOW;
    /**
     * The color for items - default is cyan.
     */
    public Color itemColor = Colors.CYAN;
    /**
     * The color for beehives - default is gold.
     */
    public Color beehiveColor = Colors.GOLD;
    /**
     * The color for furnaces - default is red-orange.
     */
    public Color furnaceColor = Colors.RED_ORANGE;
    /**
     * The alpha value for ESP's
     */
    public float alpha = 0.5f;
    /**
     * Whether to show hostile mobs.
     */
    public boolean showHostileMobs = true;
    /**
     * Whether to show peaceful mobs.
     */
    public boolean showPeacefulMobs = true;
    /**
     * The instance of the config.
     */
    private TracerEspConfig<E> instance;

    /**
     * Gets the alpha value for ESP's.
     *
     * @return The alpha value.
     */
    public float getAlpha() {
        return alpha;
    }


    /**
     * Sets the alpha value for ESP's.
     *
     * @param alpha - The alpha value.
     */
    public void setAlpha(float alpha) {
        getInstance().alpha = alpha;
        saveConfig();
    }

    /**
     * Gets the color for furnaces.
     *
     * @return The color for furnaces.
     */
    public Color getFurnaceColor() {
        return furnaceColor;
    }

    /**
     * Sets the color for furnaces.
     *
     * @param furnaceColor - The color for furnaces.
     */
    public void setFurnaceColor(Color furnaceColor) {
        getInstance().furnaceColor = furnaceColor;
        saveConfig();
    }

    /**
     * Gets the color for beehives.
     *
     * @return The color for beehives.
     */
    public Color getBeehiveColor() {
        return beehiveColor;
    }

    /**
     * Sets the color for beehives.
     *
     * @param beehiveColor - The color for beehives.
     */
    public void setBeehiveColor(Color beehiveColor) {
        getInstance().beehiveColor = beehiveColor;
        saveConfig();
    }

    /**
     * Whether to show hostile mobs.
     *
     * @return Whether to show hostile mobs.
     */
    public boolean isShowHostileMobs() {
        return getInstance().showHostileMobs;
    }

    /**
     * Sets whether to show hostile mobs.
     *
     * @param showHostileMobs - Whether to show hostile mobs.
     */
    public void setShowHostileMobs(boolean showHostileMobs) {
        getInstance().showHostileMobs = showHostileMobs;
        saveConfig();
    }

    /**
     * Whether to show peaceful mobs.
     *
     * @return Whether to show peaceful mobs.
     */
    public boolean isShowPeacefulMobs() {
        return getInstance().showPeacefulMobs;
    }

    /**
     * Sets whether to show peaceful mobs.
     *
     * @param showPeacefulMobs - Whether to show peaceful mobs.
     */
    public void setShowPeacefulMobs(boolean showPeacefulMobs) {
        getInstance().showPeacefulMobs = showPeacefulMobs;
        saveConfig();
    }

    /**
     * Gets the color for chests.
     *
     * @return The color for chests.
     */
    public Color getChestColor() {
        return getInstance().chestColor;
    }

    /**
     * Sets the color for chests.
     *
     * @param chestColor - The color for chests.
     */
    public void setChestColor(Color chestColor) {
        getInstance().chestColor = chestColor;
        saveConfig();
    }

    /**
     * Gets the color for hostile mobs.
     *
     * @return The color for hostile mobs.
     */
    public Color getHostileMobColor() {
        return getInstance().hostileMobColor;
    }

    /**
     * Sets the color for hostile mobs.
     *
     * @param hostileMobColor - The color for hostile mobs.
     */
    public void setHostileMobColor(Color hostileMobColor) {
        getInstance().hostileMobColor = hostileMobColor;
        saveConfig();
    }

    /**
     * Gets the color for peaceful mobs.
     *
     * @return The color for peaceful mobs.
     */
    public Color getPeacefulMobColor() {
        return getInstance().peacefulMobColor;
    }

    /**
     * Sets the color for peaceful mobs.
     *
     * @param peacefulMobColor - The color for peaceful mobs.
     */
    public void setPeacefulMobColor(Color peacefulMobColor) {
        getInstance().peacefulMobColor = peacefulMobColor;
        saveConfig();
    }

    /**
     * Gets the color for players.
     *
     * @return The color for players.
     */
    public Color getPlayerColor() {
        return getInstance().playerColor;
    }

    /**
     * Sets the color for players.
     *
     * @param playerColor - The color for players.
     */
    public void setPlayerColor(Color playerColor) {
        getInstance().playerColor = playerColor;
        saveConfig();
    }

    /**
     * Gets the color for items.
     *
     * @return The color for items.
     */
    public Color getItemColor() {
        return getInstance().itemColor;
    }

    /**
     * Sets the color for items.
     *
     * @param itemColor - The color for items.
     */
    public void setItemColor(Color itemColor) {
        getInstance().itemColor = itemColor;
        saveConfig();
    }


    @Override
    public TracerEspConfig<E> getInstance() {
        return instance;
    }

    @Override
    public void setInstance(TracerEspConfig<E> data) {
        this.instance = data;
    }
}
