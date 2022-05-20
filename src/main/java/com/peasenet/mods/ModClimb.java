package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * A mod that allows the player to climb up walls despite a lack of ladders.
 */
public class ModClimb extends Mod {
    public ModClimb() {
        super(Mods.CLIMB, ModCategory.MOVEMENT, KeyBindUtils.registerKeyBindForType(Mods.CLIMB));
    }
}
