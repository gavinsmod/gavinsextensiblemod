package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the player to jump as if they were pressing the jump key.
 */
public class ModAutoJump extends Mod {
    public ModAutoJump() {
        super(Mods.AUTO_JUMP, Mods.Category.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.AUTO_JUMP));
    }
}
