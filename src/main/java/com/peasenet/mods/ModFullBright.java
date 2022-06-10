package com.peasenet.mods;

import com.peasenet.main.GavinsMod;
import com.peasenet.util.KeyBindUtils;
import com.peasenet.util.RenderUtils;

/**
 * @author gt3ch1
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
public class ModFullBright extends Mod {

    public ModFullBright() {
        super(Type.FULL_BRIGHT, Type.Category.RENDER, KeyBindUtils.registerKeyBindForType(Type.FULL_BRIGHT));
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
