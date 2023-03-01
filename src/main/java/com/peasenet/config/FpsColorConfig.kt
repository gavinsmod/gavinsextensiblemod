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
 * The configuration for the FPS colors.
 *
 * @author gt3ch1
 * @version 12/31/2022
 */
public class FpsColorConfig extends Config<FpsColorConfig> {

    /**
     * The instance of the configuration.
     */
    private static FpsColorConfig instance;

    /**
     * The color for less than 50% of the FPS - default is red.
     */
    private Color slowFps = Colors.RED;

    /**
     * The color for 50% to 85% of the FPS - default is yellow.
     */
    private Color okFps = Colors.YELLOW;

    /**
     * The color for 85% to 100% of the FPS - default is green.
     */
    private Color fastFps = Colors.GREEN;

    /**
     * Whether the FPS colors should be enabled - default is true.
     */
    private boolean colorsEnabled = true;

    public FpsColorConfig() {
        setKey("fpsColors");
        instance = this;
    }

    /**
     * Gets the color for slow FPS.
     * @return The color for slow FPS.
     */
    public Color getSlowFps() {
        return slowFps;
    }

    /**
     * Sets the color for slow FPS.
     * @param slowFps - The color for slow FPS.
     */
    public void setSlowFps(Color slowFps) {
        this.slowFps = slowFps;
        setInstance(this);
        saveConfig();
    }

    /**
     * Gets the color for ok FPS.
     * @return The color for ok FPS.
     */
    public Color getOkFps() {
        return okFps;
    }

    /**
     * Sets the color for ok FPS.
     * @param okFps - The color for ok FPS.
     */
    public void setOkFps(Color okFps) {
        this.okFps = okFps;
        setInstance(this);
        saveConfig();
    }

    /**
     * Gets the color for fast FPS.
     * @return The color for fast FPS.
     */
    public Color getFastFps() {
        return fastFps;
    }

    /**
     * Sets the color for fast FPS.
     * @param fastFps - The color for fast FPS.
     */
    public void setFastFps(Color fastFps) {
        this.fastFps = fastFps;
        setInstance(this);
        saveConfig();
    }

    /**
     * Gets whether the FPS colors are enabled.
     * @return Whether the FPS colors are enabled.
     */
    public boolean isColorsEnabled() {
        return colorsEnabled;
    }

    /**
     * Sets whether the FPS colors are enabled.
     * @param colorsEnabled - Whether the FPS colors are enabled.
     */
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
