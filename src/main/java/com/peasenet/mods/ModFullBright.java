package com.peasenet.mods;

import net.minecraft.client.option.AoMode;
import net.minecraft.client.option.KeyBinding;

public class ModFullBright extends Mod {

    private AoMode prevAoMode;

    public ModFullBright(ModType type, KeyBinding keyBinding) {
        super(type, keyBinding);
    }

    @Override
    public void activate() {
        prevAoMode = getOptions().ao;
        isEnabled = true;
        getOptions().ao = AoMode.OFF;
        getClient().worldRenderer.reload();
        onEnable();
    }

    @Override
    public void deactivate() {
        getOptions().ao = prevAoMode;
        getClient().worldRenderer.reload();
        isEnabled = false;
        onDisable();
    }
}
