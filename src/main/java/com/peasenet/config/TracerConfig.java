package com.peasenet.config;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.main.Settings;

public class TracerConfig extends Config<TracerConfig> {
    private static TracerConfig instance;
    public Color chestColor = Colors.PURPLE;
    public Color hostileMobColor = Colors.RED;
    public Color peacefulMobColor = Colors.GREEN;
    public Color playerColor = Colors.YELLOW;
    public Color itemColor = Colors.CYAN;
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

    public TracerConfig() {
        key = "tracer";
        instance = this;
    }

    @Override
    public void setInstance(TracerConfig data) {
        TracerConfig.instance = data;
    }

    public TracerConfig getInstance() {
        return TracerConfig.instance;
    }

    @Override
    public void loadDefaultConfig() {
        Settings.settings.put("tracer", instance);
        Settings.save();
    }

    @Override
    public void readFromSettings() {
        setInstance(Settings.getTracerConfig());
    }
}
