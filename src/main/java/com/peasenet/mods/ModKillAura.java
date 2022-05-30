package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/23/2022
 */
public class ModKillAura extends Mod {
    public ModKillAura() {
        super(Type.KILL_AURA, Type.Category.COMBAT, KeyBindUtils.registerKeyBindForType(Type.KILL_AURA));
    }
}
