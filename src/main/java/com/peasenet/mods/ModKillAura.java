package com.peasenet.mods;

import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/23/2022
 */
public class ModKillAura extends Mod {
    public ModKillAura() {
        super(Mods.KILL_AURA, Mods.Category.COMBAT, KeyBindUtils.registerKeyBindForType(Mods.KILL_AURA));
    }
}
