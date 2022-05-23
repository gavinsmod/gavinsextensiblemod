package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;
//TODO: This is broken. Player just jumps forever.
public class ModAutoJump extends Mod {
    public ModAutoJump() {
        super(Mods.AUTO_JUMP, Mods.Category.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.AUTO_JUMP));
    }
}
