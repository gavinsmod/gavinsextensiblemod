package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;

public abstract class Mod implements IMod {

    public KeyBinding keyBinding;
    private final ModType type;

    public boolean isEnabled = false;

    public Mod(ModType type, KeyBinding keyBinding) {
        this.type = type;
        this.keyBinding = keyBinding;
    }

    public void onEnable() {
        sendMessage(type.getName() + " enabled");
    }

    public void onDisable() {
        sendMessage(type.getName() + " disabled");
    }

    public void onTick() {
        checkKeybinding();
    }

    protected void checkKeybinding() {
        if(keyBinding.wasPressed()) {
            if(isEnabled) {
                deactivate();
            } else {
                activate();
            }
        }
    }

    public boolean isActive() {
        return isEnabled;
    }

    public void activate() {
        isEnabled = true;
        onEnable();
    }

    public void deactivate() {
        isEnabled = false;
        onDisable();
    }

    public static void sendMessage(String message) {
        GavinsModClient.getPlayer().sendMessage(new LiteralText(message), false);
    }

    protected static MinecraftClient getClient(){
        return GavinsModClient.getMinecraftClient();
    }

    protected static GameOptions getOptions(){
        return getClient().options;
    }
}
