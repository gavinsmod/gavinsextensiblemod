package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModFly extends Mod {
    public ModFly() {
        super(ModType.FLY, ModCategory.TICKABLE, KeyBindUtils.registerKeyBindForType(ModType.FLY));
    }
}
