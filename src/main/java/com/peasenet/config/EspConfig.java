package com.peasenet.config;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.main.Settings;

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
        return showHostileMobs;
    }

    public void setShowHostileMobs(boolean showHostileMobs) {
        this.showHostileMobs = showHostileMobs;
        setInstance(this);
        saveConfig();
    }

    public boolean isShowPeacefulMobs() {
        return showPeacefulMobs;
    }

    public void setShowPeacefulMobs(boolean showPeacefulMobs) {
        this.showPeacefulMobs = showPeacefulMobs;
        setInstance(this);
        saveConfig();
    }

    public boolean isShowPlayers() {
        return showPlayers;
    }

    public void setShowPlayers(boolean showPlayers) {
        this.showPlayers = showPlayers;
        setInstance(this);
        saveConfig();
    }

    public Color getChestColor() {
        return chestColor;
    }

    public void setChestColor(Color chestColor) {
        this.chestColor = chestColor;
        setInstance(this);
        saveConfig();
    }

    public Color getHostileMobColor() {
        return hostileMobColor;
    }

    public void setHostileMobColor(Color hostileMobColor) {
        this.hostileMobColor = hostileMobColor;
        setInstance(this);
        saveConfig();
    }

    public Color getPeacefulMobColor() {
        return peacefulMobColor;
    }

    public void setPeacefulMobColor(Color peacefulMobColor) {
        this.peacefulMobColor = peacefulMobColor;
        setInstance(this);
        saveConfig();
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
        setInstance(this);
        saveConfig();
    }

    public Color getItemColor() {
        return itemColor;
    }

    public void setItemColor(Color itemColor) {
        this.itemColor = itemColor;
        setInstance(this);
        saveConfig();
    }

    public EspConfig() {
        key = "esp";
        if (instance == null)
            instance = getConfig();
    }

    public EspConfig(Color chestColor, Color hostileMobColor, Color peacefulMobColor, Color playerColor, Color itemColor,
                     boolean showHostileMobs, boolean showPeacefulMobs, boolean showPlayers) {
        key = "esp";
        this.chestColor = chestColor;
        this.hostileMobColor = hostileMobColor;
        this.peacefulMobColor = peacefulMobColor;
        this.playerColor = playerColor;
        this.itemColor = itemColor;
        this.showHostileMobs = showHostileMobs;
        this.showPeacefulMobs = showPeacefulMobs;
        this.showPlayers = showPlayers;
    }

    @Override
    public void setInstance(EspConfig data) {
        EspConfig.instance = data;
    }

    public EspConfig getInstance() {
        return EspConfig.instance;
    }

    @Override
    public void loadDefaultConfig() {
        instance = new EspConfig(
                Colors.PURPLE,
                Colors.RED,
                Colors.GREEN,
                Colors.YELLOW,
                Colors.CYAN,
                true,
                true,
                true
        );
        Settings.settings.put("esp", instance);
        Settings.save();
    }
}
