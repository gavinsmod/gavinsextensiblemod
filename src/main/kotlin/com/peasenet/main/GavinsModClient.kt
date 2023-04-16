/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
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
package com.peasenet.main

import com.peasenet.mixinterface.IClientPlayerEntity
import com.peasenet.mixinterface.IMinecraftClient
import com.peasenet.util.RenderUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.AfterEntities
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.BeforeBlockOutline
import net.minecraft.client.MinecraftClient
import net.minecraft.util.hit.HitResult
import net.minecraft.world.LightType

/**
 * @author gt3ch1
 * @version 04-11-2023
 * The main part of the mod that handles checking mods.
 */
class GavinsModClient : ClientModInitializer {
    override fun onInitializeClient() {
        GavinsMod.LOGGER.info("GavinsMod keybinding initialized")
        ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick {
            if (player == null) return@StartTick
            for (m in Mods.mods) {
                m.checkKeybinding()
                if (m.isActive || m.isDeactivating) m.onTick()
            }
            checkAutoFullBright()
        })
        WorldRenderEvents.AFTER_ENTITIES.register(AfterEntities { context: WorldRenderContext ->
            RenderUtils.afterEntities(
                context
            )
        })
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(BeforeBlockOutline { renderContext: WorldRenderContext, hitResult: HitResult? ->
            RenderUtils.beforeBlockOutline(
                renderContext,
                hitResult
            )
        })
    }

    /**
     * Checks if the auto full bright feature is enabled.
     */
    private fun checkAutoFullBright() {
        if (!GavinsMod.fullbrightConfig.autoFullBright) return
        val skyBrightness = minecraftClient.getWorld().getLightLevel(LightType.SKY, player!!.getBlockPos().up())
        val blockBrightness = minecraftClient.getWorld().getLightLevel(LightType.BLOCK, player!!.getBlockPos().up())
        val currTime = minecraftClient.getWorld().timeOfDay % 24000
        var shouldBeFullBright = (currTime >= 13000 || currTime <= 100 || skyBrightness <= 2) && blockBrightness <= 2
        shouldBeFullBright = shouldBeFullBright || player!!.isSubmergedInWater()
        if (shouldBeFullBright && !Mods.getMod("fullbright").isActive) Mods.getMod("fullbright")
            .activate() else if (Mods.getMod("fullbright").isActive && !shouldBeFullBright) Mods.getMod("fullbright")
            .deactivate()
    }

    companion object {
        val minecraftClient: IMinecraftClient
            /**
             * Gets the minecraft client.
             *
             * @return The minecraft client.
             */
            get() = MinecraftClient.getInstance() as IMinecraftClient
        val player: IClientPlayerEntity?
            /**
             * Gets the minecraft client player.
             *
             * @return The minecraft client player.
             */
            get() = MinecraftClient.getInstance().player as IClientPlayerEntity?
    }
}