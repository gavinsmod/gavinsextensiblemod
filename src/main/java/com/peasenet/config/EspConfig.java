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

public class EspConfig extends Config<EspConfig> {
    private static EspConfig instance;
    public Color chestColor = Colors.PURPLE;
    public Color hostileMobColor = Colors.RED;
    public Color peacefulMobColor = Colors.GREEN;
    public Color playerColor = Colors.YELLOW;
    public Color itemColor = Colors.CYAN;

    public Color beehiveColor = Colors.GOLD;

    public Color getBeehiveColor() {
        return beehiveColor;
    }

    public void setBeehiveColor(Color beehiveColor) {
        this.beehiveColor = beehiveColor;
    }
    public boolean showHostileMobs = true;
    public boolean showPeacefulMobs = true;

    public boolean isShowHostileMobs() {
        return getInstance().showHostileMobs;
    }

    public void setShowHostileMobs(boolean showHostileMobs) {
        getInstance().showHostileMobs = showHostileMobs;
        saveConfig();
    }

    public boolean isShowPeacefulMobs() {
        return getInstance().showPeacefulMobs;
    }

    public void setShowPeacefulMobs(boolean showPeacefulMobs) {
        getInstance().showPeacefulMobs = showPeacefulMobs;
        saveConfig();
    }

    public Color getChestColor() {
        return getInstance().chestColor;
    }

    public void setChestColor(Color chestColor) {
        getInstance().chestColor = chestColor;
        saveConfig();
    }

    public Color getHostileMobColor() {
        return getInstance().hostileMobColor;
    }

    public void setHostileMobColor(Color hostileMobColor) {
        getInstance().hostileMobColor = hostileMobColor;
        saveConfig();
    }

    public Color getPeacefulMobColor() {
        return getInstance().peacefulMobColor;
    }

    public void setPeacefulMobColor(Color peacefulMobColor) {
        getInstance().peacefulMobColor = peacefulMobColor;
        saveConfig();
    }

    public Color getPlayerColor() {
        return getInstance().playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        getInstance().playerColor = playerColor;
        saveConfig();
    }

    public Color getItemColor() {
        return getInstance().itemColor;
    }

    public void setItemColor(Color itemColor) {
        getInstance().itemColor = itemColor;
        saveConfig();
    }

    public EspConfig() {
        setKey("esp");
        instance = this;
    }

    @Override
    public void setInstance(EspConfig data) {
        EspConfig.instance = data;
    }

    public EspConfig getInstance() {
        return EspConfig.instance;
    }

}
