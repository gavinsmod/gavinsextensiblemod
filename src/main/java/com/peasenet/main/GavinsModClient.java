package com.peasenet.main;

import com.peasenet.mods.Mod;
import com.peasenet.util.RenderUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;

import java.util.HashSet;

/**
 * @author gt3ch1
 * @version 5/15/2022
 */
public class GavinsModClient implements ClientModInitializer {

    public static HashSet<Box> boxesToRender = new HashSet<>();
    public static boolean boxesRendered = false;

    @Override
    public void onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized");
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            for (Mod m : GavinsMod.mods) {
                m.onTick();
            }
        });
        WorldRenderEvents.END.register(RenderUtils::onRender);
    }

    public static MinecraftClient getMinecraftClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {
        return getMinecraftClient().player;
    }

    public static void AddBox(Box box) {
        if (!boxesRendered)
            boxesToRender.add(box);
    }
}
