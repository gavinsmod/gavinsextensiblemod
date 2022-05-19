package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModClimb extends Mod {
    public ModClimb() {
        super(ModType.CLIMB, ModCategory.MOVEMENT, KeyBindUtils.registerKeyBindForType(ModType.CLIMB));
    }
}
