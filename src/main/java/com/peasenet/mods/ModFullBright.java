package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the client to see very clearly in the absence of a light source.
 */
public class ModFullBright extends Mod {


    public ModFullBright() {
        super(Mods.FULL_BRIGHT, ModCategory.RENDER, KeyBindUtils.registerKeyBindForType(Mods.FULL_BRIGHT));
    }

    @Override
    public void activate() {
        isEnabled = true;
        getClient().worldRenderer.reload();
        onEnable();
    }

    @Override
    public void deactivate() {
        getClient().worldRenderer.reload();
        isEnabled = false;
        onDisable();
    }
}
