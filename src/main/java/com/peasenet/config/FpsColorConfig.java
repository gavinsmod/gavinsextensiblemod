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
