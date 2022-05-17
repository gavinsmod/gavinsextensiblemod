package com.peasenet.main;

import com.peasenet.mods.Mods;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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
    public static KeyBinding flyKeybinding;
    public static KeyBinding fastMineKeybinding;
    public static KeyBinding xrayKeybinding;
    public static KeyBinding fullBrightKeybinding;

    @Override
    public void onInitializeClient() {
        flyKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fly", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, "category.gavinsmod.test"));
        fastMineKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fastmine", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.gavinsmod.test"));
        xrayKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.xray", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "category.gavinsmod.test"));
        fullBrightKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fullbright", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category.gavinsmod.test"));

        GavinsMod.LOGGER.info("GavinsMod keybinding initialized");

        AtomicReference<AoMode> prevAoMode = new AtomicReference<>((AoMode) AoMode.OFF);
        AtomicReference<Boolean> prevCulling = new AtomicReference<>(false);
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {

            if (flyKeybinding.wasPressed()) {
                Mods.flyEnabled = !Mods.flyEnabled;
                assert client.player != null;
                client.player.sendMessage(new LiteralText(Mods.flyEnabled ? "Fly mode enabled" : "Fly mode disabled"), false);
            }

            if (fastMineKeybinding.wasPressed()) {
                Mods.fastMine = !Mods.fastMine;
                assert client.player != null;
                client.player.sendMessage(new LiteralText(Mods.fastMine ? "Fast mine mode enabled" : "Fast mine mode disabled"), false);
            }

            if (xrayKeybinding.wasPressed()) {
                // Need to set smooth lighting to false to get the xray effect.
                if (!Mods.xrayEnabled) {
                    prevAoMode.set(client.options.ao);
                    prevCulling.set(client.chunkCullingEnabled);
                    client.options.ao = AoMode.OFF;
                    client.chunkCullingEnabled = false;
                } else {
                    client.options.ao = prevAoMode.get();
                    client.chunkCullingEnabled = prevCulling.get();
                }

                Mods.xrayEnabled = !Mods.xrayEnabled;

                client.worldRenderer.reload();
                if (client.player != null)
                    client.player.sendMessage(new LiteralText(Mods.xrayEnabled ? "Xray mode enabled" : "Xray mode disabled"), false);
            }

            if (fullBrightKeybinding.wasPressed()) {
                if (!Mods.fullBrightEnabled) {
                    prevAoMode.set(client.options.ao);
                    client.options.ao = AoMode.OFF;
                } else {
                    client.options.ao = prevAoMode.get();
                }

                Mods.fullBrightEnabled = !Mods.fullBrightEnabled;
                client.worldRenderer.reload();
                if (client.player != null)
                    client.player.sendMessage(new LiteralText(Mods.fullBrightEnabled ? "Fullbright mode enabled" : "Fullbright mode disabled"), false);
            }
        });
    }
}
