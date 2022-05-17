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
    public static KeyBinding flyKeybinding;
    public static KeyBinding fastMineKeybinding;
    public static KeyBinding xrayKeybinding;
    public static KeyBinding fullBrightKeybinding;

    @Override
    public void onInitializeClient() {
        flyKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fly", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, "category.gavinsmod.test"));
        fastMineKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fastmine", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.gavinsmod.test"));
        fullBrightKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.gavinsmod.fullbright", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category.gavinsmod.test"));

        GavinsMod.LOGGER.info("GavinsMod keybinding initialized");
        AtomicReference<AoMode> prevAoMode = new AtomicReference<>((AoMode) AoMode.OFF);
        AtomicReference<Boolean> prevCulling = new AtomicReference<>(false);
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            for(Mod m : GavinsMod.mods){
                m.onTick();
            }
            if (flyKeybinding.wasPressed()) {
                Mods.flyEnabled = !Mods.flyEnabled;
                assert getPlayer() != null;
                getPlayer().sendMessage(new LiteralText(Mods.flyEnabled ? "Fly mode enabled" : "Fly mode disabled"), false);
            }

            if (fastMineKeybinding.wasPressed()) {
                Mods.fastMine = !Mods.fastMine;
                assert client.player != null;
                getPlayer().sendMessage(new LiteralText(Mods.fastMine ? "Fast mine mode enabled" : "Fast mine mode disabled"), false);
            }

//            if (xrayKeybinding.wasPressed()) {
//                // Need to set smooth lighting to false to get the xray effect.
//                if (!GavinsMod.XRay.isActive()) {
//                    prevAoMode.set(client.options.ao);
//                    prevCulling.set(client.chunkCullingEnabled);
//                    getMinecraftClient().options.ao = AoMode.OFF;
//                    getMinecraftClient().chunkCullingEnabled = false;
//                    GavinsMod.XRay.activate();
//                } else {
//                    getMinecraftClient().options.ao = prevAoMode.get();
//                    getMinecraftClient().chunkCullingEnabled = prevCulling.get();
//                    GavinsMod.XRay.deactivate();
//                }
//                getMinecraftClient().worldRenderer.reload();
//            }

            if (fullBrightKeybinding.wasPressed()) {
                if (!Mods.fullBrightEnabled) {
                    prevAoMode.set(client.options.ao);
                    getMinecraftClient().options.ao = AoMode.OFF;
                } else {
                    getMinecraftClient().options.ao = prevAoMode.get();
                }

                Mods.fullBrightEnabled = !Mods.fullBrightEnabled;
                getMinecraftClient().worldRenderer.reload();
                if (getPlayer() != null)
                    getPlayer().sendMessage(new LiteralText(Mods.fullBrightEnabled ? "Fullbright mode enabled" : "Fullbright mode disabled"), false);
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
