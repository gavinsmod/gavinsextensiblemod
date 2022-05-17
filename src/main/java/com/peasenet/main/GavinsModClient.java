package com.peasenet.main;

import com.peasenet.mods.Mod;
import com.peasenet.mods.Mods;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.ClientGameSession;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.AoMode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gt3ch1
 * @version 5/15/2022
 */
public class GavinsModClient implements ClientModInitializer {
    public static KeyBinding fastMineKeybinding;
    public static KeyBinding fullBrightKeybinding;

    @Override
    public void onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized");
        AtomicReference<AoMode> prevAoMode = new AtomicReference<>((AoMode) AoMode.OFF);
        AtomicReference<Boolean> prevCulling = new AtomicReference<>(false);
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            for(Mod m : GavinsMod.mods){
                m.onTick();
            }
        });
    }

    public static MinecraftClient getMinecraftClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {
        return getMinecraftClient().player;
    }
}
