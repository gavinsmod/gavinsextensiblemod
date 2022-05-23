package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
public class ModFullBright extends Mod {

    public ModFullBright() {
        super(Mods.FULL_BRIGHT, Mods.Category.RENDER, KeyBindUtils.registerKeyBindForType(Mods.FULL_BRIGHT));
    }

    @Override
    public void activate() {
        getClient().worldRenderer.reload();
        super.activate();
    }

    @Override
    public void deactivate() {
        getClient().worldRenderer.reload();
        super.deactivate();
    }
}
