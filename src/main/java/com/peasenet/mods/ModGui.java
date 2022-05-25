package com.peasenet.mods;

import com.peasenet.gui.GuiMainMenu;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.KeyBindUtils;

/**
 * @author gt3ch1
 * @version 5/24/2022
 * A mod that allows the player to see a menu of all the mods by pressing the keybind.
 */
public class ModGui extends Mod{
    public ModGui() {
        super(Mods.MOD_GUI, Mods.Category.GUI, KeyBindUtils.registerKeyBindForType(Mods.MOD_GUI));
    }

    @Override
    public void onEnable() {
        GavinsModClient.getMinecraftClient().setScreen(new GuiMainMenu(GavinsMod.gui));
        isEnabled = false;
    }

    @Override
    public void onDisable() {
        GavinsModClient.getMinecraftClient().setScreen(new GuiMainMenu(GavinsMod.gui));
        isEnabled = false;
    }

    @Override
    public void toggle() {
        GavinsModClient.getMinecraftClient().setScreen(new GuiMainMenu(GavinsMod.gui));
        isEnabled = false;
    }
}
