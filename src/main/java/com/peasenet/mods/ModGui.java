package com.peasenet.mods;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A mod that allows the player to see a menu of all the mods by pressing the keybind.
 */
public class ModGui extends Mod{
    public ModGui() {
        super(Type.MOD_GUI, Type.Category.GUI, KeyBindUtils.registerKeyBindForType(Type.MOD_GUI));
    }

    @Override
    public void activate() {
        GavinsModClient.getMinecraftClient().setScreen(GavinsMod.gui);
    }
}
