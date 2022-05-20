package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.LiteralText;

public abstract class Mod implements IMod {

    public KeyBinding keyBinding;
    private final Mods type;
    private final ModCategory category;
    public boolean isEnabled = false;

    public Mod(Mods type, ModCategory category, KeyBinding keyBinding) {
        this.type = type;
        this.category = category;
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
        if (keyBinding.wasPressed()) {
            if (isEnabled) {
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

    public void afterEntities(WorldRenderContext context) {

    }

    public void toggle() {
        if (isActive()) {
            deactivate();
        } else {
            activate();
        }
    }

    public static void sendMessage(String message) {
        GavinsModClient.getPlayer().sendMessage(new LiteralText(message), false);
    }

    protected static MinecraftClient getClient() {
        return GavinsModClient.getMinecraftClient();
    }

    protected static GameOptions getOptions() {
        return getClient().options;
    }

    public ModCategory getCategory() {
        return category;
    }

    public String getTranslationKey() {
        return type.getTranslationKey();
    }

    public String getName() {
        return type.getName();
    }

}
