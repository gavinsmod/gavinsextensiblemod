package com.peasenet.config;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;

public class FpsColorConfig extends Config<FpsColorConfig> {
    private static FpsColorConfig instance;
    private Color slowFps = Colors.RED;
    private Color okFps = Colors.YELLOW;
    private Color fastFps = Colors.GREEN;
    private boolean colorsEnabled = true;

    public FpsColorConfig() {
        setKey("fpsColors");
        instance = this;
    }

    public Color getSlowFps() {
        return slowFps;
    }

    public void setSlowFps(Color slowFps) {
        this.slowFps = slowFps;
        setInstance(this);
        saveConfig();
    }

    public Color getOkFps() {
        return okFps;
    }

    public void setOkFps(Color okFps) {
        this.okFps = okFps;
        setInstance(this);
        saveConfig();
    }

    public Color getFastFps() {
        return fastFps;
    }

    public void setFastFps(Color fastFps) {
        this.fastFps = fastFps;
        setInstance(this);
        saveConfig();
    }

    public boolean isColorsEnabled() {
        return colorsEnabled;
    }

    public void setColorsEnabled(boolean colorsEnabled) {
        this.colorsEnabled = colorsEnabled;
        setInstance(this);
        saveConfig();
    }

    public FpsColorConfig getInstance() {
        return FpsColorConfig.instance;
    }

    @Override
    public void setInstance(FpsColorConfig data) {
        FpsColorConfig.instance = data;
    }

}
