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

import com.peasenet.main.Mods;
import com.peasenet.mods.Type;


/**
 * Configuration file for fullbright.
 *
 * @author gt3ch1
 * @version 12/31/2022
 */
public class FullbrightConfig extends Config<FullbrightConfig> {

    /**
     * The instance of the config.
     */
    private static FullbrightConfig instance;

    /**
     * Whether to automatically enable fullbright when the player is in a dark area.
     */
    boolean autoFullBright = true;

    /**
     * Whether to fade the brightness.
     */
    boolean gammaFade = true;

    /**
     * The current gamma multiplier.
     */
    float gamma = 1.0f;

    public FullbrightConfig() {
        setKey("fullbright");
    }

    /**
     * Gets the gamma multiplier.
     * @return
     */
    public float getGamma() {
        return gamma;
    }

    /**
     * Sets the gamma multiplier.
     * @param gamma - the gamma multiplier.
     */
    public void setGamma(float gamma) {
        this.gamma = gamma;
    }

    /**
     * Gets the maximum gamma value allowed.
     * If XRAY is enabled, this will be 16. 
     * If it is not, then it will be between 1 and 4 inclusive.
     * @return max gamma value.
     */
    public float getMaxGamma() {
        return Mods.isActive(Type.XRAY) ? 16 : 1 + 3 * getGamma();
    }

    /**
     * Whether auto fullbright is enabled.
     * @return true if auto fullbright is enabled.
     */
    public boolean isAutoFullBright() {
        return autoFullBright;
    }


    /**
     * Sets and saves the auto fullbright setting.
     * @param autoFullBright - the new auto fullbright setting.
     */
    public void setAutoFullBright(boolean autoFullBright) {
        this.autoFullBright = autoFullBright;
        setInstance(this);
        saveConfig();
    }

    /**
     * Whether gamma fading is enabled.
     * @return true if gamma fading is enabled.
     */
    public boolean isGammaFade() {
        return gammaFade;
    }

    /**
     * Sets and saves the gamma fade setting.
     * @param gammaFade - the new gamma fade setting.
     */
    public void setGammaFade(boolean gammaFade) {
        this.gammaFade = gammaFade;
        setInstance(this);
        saveConfig();
    }

    @Override
    public FullbrightConfig getInstance() {
        return instance;
    }

    @Override
    public void setInstance(FullbrightConfig data) {
        instance = data;
    }
}
