/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mods.render;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
public class ModFullBright extends Mod {

    public ModFullBright() {
        super(Type.FULL_BRIGHT);
        ToggleSetting gammaFade = new ToggleSetting("gavinsmod.settings.render.gammafade");
        gammaFade.setCallback(() -> {
            fullbrightConfig.setGammaFade(gammaFade.getValue());
        });
        gammaFade.setValue(fullbrightConfig.isGammaFade());
        ToggleSetting autoFullBright = new ToggleSetting("gavinsmod.settings.render.autofullbright");
        autoFullBright.setCallback(() -> {
            fullbrightConfig.setAutoFullBright(autoFullBright.getValue());
        });
        autoFullBright.setValue(fullbrightConfig.isAutoFullBright());
        addSetting(gammaFade);
        addSetting(autoFullBright);
    }

    @Override
    public void activate() {
        if (!GavinsMod.isEnabled(Type.XRAY))
            RenderUtils.setLastGamma();
        super.activate();
    }

    @Override
    public void onTick() {
        if (isActive() && !RenderUtils.isHighGamma()) {
            RenderUtils.setHighGamma();
        } else if (!GavinsMod.isEnabled(Type.XRAY) && !RenderUtils.isLastGamma() && deactivating) {
            RenderUtils.setLowGamma();
            deactivating = !RenderUtils.isLastGamma();
        }
    }

    @Override
    public boolean isDeactivating() {
        return deactivating;
    }

    @Override
    public void deactivate() {
        deactivating = true;
        RenderUtils.setGamma(4);
        super.deactivate();
    }
}
