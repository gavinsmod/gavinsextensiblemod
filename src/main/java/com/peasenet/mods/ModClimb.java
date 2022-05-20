package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

public class ModClimb extends Mod {
    public ModClimb() {
        super(Mods.CLIMB, ModCategory.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.CLIMB));
    }
}
