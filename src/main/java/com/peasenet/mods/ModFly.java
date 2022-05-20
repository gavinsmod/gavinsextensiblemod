package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModFly extends Mod {
    public ModFly() {
        super(Mods.FLY, ModCategory.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.FLY));
    }
}
