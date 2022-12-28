/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.main;

import com.peasenet.mixinterface.IClientPlayerEntity;
import com.peasenet.mixinterface.IMinecraftClient;
import com.peasenet.mods.Mod;
import com.peasenet.util.RenderUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.LightType;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * The main part of the mod that handles checking mods.
 */
public class GavinsModClient implements ClientModInitializer {

    /**
     * Gets the minecraft client.
     *
     * @return The minecraft client.
     */
    public static IMinecraftClient getMinecraftClient() {
        return (IMinecraftClient) MinecraftClient.getInstance();
    }

    /**
     * Gets the minecraft client player.
     *
     * @return The minecraft client player.
     */
    public static IClientPlayerEntity getPlayer() {
        return (IClientPlayerEntity) MinecraftClient.getInstance().player;
    }

    @Override
    public void onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized");
        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (getPlayer() == null || getMinecraftClient() == null) return;
            for (Mod m : Mods.getMods()) {
                m.checkKeybinding();
                if (m.isActive() || m.isDeactivating()) m.onTick();
            }
            checkAutoFullBright();
        });
        WorldRenderEvents.AFTER_ENTITIES.register(RenderUtils::afterEntities);
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RenderUtils::beforeBlockOutline);
    }

    /**
     * Checks if the auto full bright feature is enabled.
     */
    private void checkAutoFullBright() {
        if (!GavinsMod.fullbrightConfig.isAutoFullBright()) return;
        var skyBrightness = getMinecraftClient().getWorld().getLightLevel(LightType.SKY, getPlayer().getBlockPos().up());
        var blockBrightness = getMinecraftClient().getWorld().getLightLevel(LightType.BLOCK, getPlayer().getBlockPos().up());
        var currTime = getMinecraftClient().getWorld().getTimeOfDay() % 24000;
        var shouldBeFullBright = (currTime >= 13000 || currTime <= 100 || skyBrightness <= 2) && blockBrightness <= 2;
        shouldBeFullBright = shouldBeFullBright || getPlayer().isSubmergedInWater();
        if (shouldBeFullBright && !Mods.getMod("fullbright").isActive()) Mods.getMod("fullbright").activate();
        else if (Mods.getMod("fullbright").isActive() && !shouldBeFullBright) Mods.getMod("fullbright").deactivate();
    }
}
