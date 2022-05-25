package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the player to fly.
 */
public class ModFly extends Mod {
    public ModFly() {
        super(Mods.FLY, Mods.Category.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.FLY));
    }
}
