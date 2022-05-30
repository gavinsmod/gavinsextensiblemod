package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the player to mine very quickly.
 */
public class ModFastMine extends Mod {
    public ModFastMine() {
        super(Type.FAST_MINE, Type.Category.MOVEMENT, KeyBindUtils.registerKeyBindForType(Type.FAST_MINE));
    }
}
