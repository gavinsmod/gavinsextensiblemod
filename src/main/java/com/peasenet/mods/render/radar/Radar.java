package com.peasenet.mods.render.radar;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.main.Settings;

public class Radar {
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public static void setY(int y) {
        getInstance().y = y;
    }

    public static void setX(int x) {
        getInstance().x = x;
    }

    private static int y = 12;
    private static int x = 0;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getPointSize() {
        return pointSize;
    }

    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;

        Settings.saveRadar();
    }

    public Color getHostileMobColor() {
        return hostileMobColor;
    }

    public void setHostileMobColor(Color hostileMobColor) {
        this.hostileMobColor = hostileMobColor;

        Settings.saveRadar();
    }

    public Color getPeacefulMobColor() {
        return peacefulMobColor;
    }

    public void setPeacefulMobColor(Color peacefulMobColor) {
        this.peacefulMobColor = peacefulMobColor;
        Settings.saveRadar();
    }

    public Color getItemColor() {
        return itemColor;
    }

    public void setItemColor(Color itemColor) {
        this.itemColor = itemColor;
        Settings.saveRadar();
    }

    public Color getWaypointColor() {
        return waypointColor;
    }

    public void setWaypointColor(Color waypointColor) {
        this.waypointColor = waypointColor;
        Settings.saveRadar();
    }

    public boolean isShowPlayer() {
        return showPlayer;
    }

    public void setShowPlayer(boolean showPlayer) {
        this.showPlayer = showPlayer;
        Settings.saveRadar();
    }

    public boolean isShowHostileMob() {
        return showHostileMob;
    }

    public void setShowHostileMob(boolean showHostileMob) {
        this.showHostileMob = showHostileMob;
        Settings.saveRadar();
    }

    public boolean isShowPeacefulMob() {
        return showPeacefulMob;
    }

    public void setShowPeacefulMob(boolean showPeacefulMob) {
        this.showPeacefulMob = showPeacefulMob;
        Settings.saveRadar();

    }

    public boolean isShowItem() {
        return showItem;
    }

    public void setShowItem(boolean showItem) {
        this.showItem = showItem;
        Settings.saveRadar();
    }

    public boolean isShowWaypoint() {
        return showWaypoint;
    }

    public void setShowWaypoint(boolean showWaypoint) {
        this.showWaypoint = showWaypoint;
        Settings.saveRadar();
    }

    public boolean isUseWaypointColor() {
        return useWaypointColor;
    }

    public void setUseWaypointColor(boolean useWaypointColor) {
        this.useWaypointColor = useWaypointColor;
        Settings.saveRadar();
    }

    private static int size = 129;
    private int scale = 4;
    private int pointSize = 3;
    private Color playerColor = Colors.GOLD;
    private Color hostileMobColor = Colors.RED;
    private Color peacefulMobColor = Colors.GREEN;
    private Color itemColor = Colors.CYAN;
    private Color waypointColor = Colors.WHITE;
    private boolean showPlayer = true;
    private boolean showHostileMob = true;
    private boolean showPeacefulMob = true;
    private boolean showItem = true;
    private boolean showWaypoint = true;
    private boolean useWaypointColor = true;

    public static Radar getInstance() {
        return INSTANCE;
    }

    public static Radar INSTANCE;

    public Radar() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }
    }

    public void increaseScaleCallback() {
        scale++;
        if (scale > 8) scale = 1;
        updateScaleCallback();
    }

    public void updateScaleCallback() {
        size = 16 * scale + 1;
        Settings.saveRadar();
    }

    public void updatePointSizeCallback() {
        if (pointSize + 2 > 5) pointSize = 1;
        else pointSize += 2;
        Settings.saveRadar();
    }
}
