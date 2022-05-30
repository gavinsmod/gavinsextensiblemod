package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/21/2022
 * A mod that allows the player to jump as if they were pressing the jump key.
 */
public class ModAutoJump extends Mod {
    public ModAutoJump() {
        super(Type.AUTO_JUMP, Type.Category.MOVEMENT, KeyBindUtils.registerKeyBindForType(Type.AUTO_JUMP));
    }
}
