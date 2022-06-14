package com.peasenet.mods;

import com.peasenet.main.GavinsMod;
import com.peasenet.util.RenderUtils;

/**
 * @author gt3ch1
 * @version 6/14/2022
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
public class ModFullBright extends Mod {

    public ModFullBright() {
        super(Type.FULL_BRIGHT);
    }

    @Override
    public void activate() {
        RenderUtils.setHighGamma();
        super.activate();
    }

    @Override
    public void deactivate() {
        if (!GavinsMod.isEnabled(Type.XRAY))
            RenderUtils.resetGamma();
        super.deactivate();
    }
}
