package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

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
//        getClient().worldRenderer.reload();
        super.activate();
    }

    @Override
    public void deactivate() {
//        getClient().worldRenderer.reload();
        super.deactivate();
    }
}
