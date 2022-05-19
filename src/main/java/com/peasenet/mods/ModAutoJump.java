package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;
//TODO: This is broken. Player just jumps forever.
public class ModAutoJump extends Mod {
    public ModAutoJump() {
        super(ModType.AUTO_JUMP, ModCategory.MOVEMENT, KeyBindUtils.registerKeyBindForType(ModType.AUTO_JUMP));
    }
}
