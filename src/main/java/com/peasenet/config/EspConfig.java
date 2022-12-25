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
    public boolean showHostileMobs = true;
    public boolean showPeacefulMobs = true;
    public boolean showPlayers = true;

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

    public boolean isShowPlayers() {
        return getInstance().showPlayers;
    }

    public void setShowPlayers(boolean showPlayers) {
        getInstance().showPlayers = showPlayers;
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
