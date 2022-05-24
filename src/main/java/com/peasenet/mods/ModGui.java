package com.peasenet.mods;

import com.peasenet.gui.GuiClick;
import com.peasenet.gui.GuiMainMenu;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.KeyBindUtils;

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

    }
}
