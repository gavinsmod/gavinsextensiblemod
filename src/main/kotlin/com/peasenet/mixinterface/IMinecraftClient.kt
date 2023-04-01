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
package com.peasenet.mixinterface

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.client.network.message.MessageHandler
import net.minecraft.client.option.GameOptions
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.Window
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.hit.HitResult
import java.io.File

/**
 * @author gt3ch1
 * @version 03-02-2023
 * A mixin interface for the Minecraft client to allow access for certain members that are not public.
 */
interface IMinecraftClient {
    /**
     * Sets the cooldown time for using any item.
     *
     * @param cooldown - The cooldown time.
     */
    fun setItemUseCooldown(cooldown: Int)

    /**
     * The current frames per second.
     *
     * @return The current frames per second.
     */
    fun getFps(): Int

    /**
     * Gets the current options for the game.
     *
     * @return The current options for the game.
     */
    val options: GameOptions

    /**
     * Gets the player interaction manager.
     *
     * @return The player interaction manager.
     */
    fun getPlayerInteractionManager(): ClientPlayerInteractionManager

    /**
     * Gets the text renderer of the client.
     *
     * @return The text renderer.
     */
    val textRenderer: TextRenderer

    /**
     * Gets the world that the player is currently in.
     *
     * @return The current world.
     */
    fun getWorld(): ClientWorld

    /**
     * Sets the current in game screen to the given parameter
     *
     * @param s - The screen to change to.
     */
    fun setScreen(s: Screen?)

    /**
     * Gets the current world renderer.
     *
     * @return The world renderer.
     */
    val worldRenderer: WorldRenderer

    /**
     * Enables or disables chunk culling (rendering only chunks that are in the viewport).
     *
     * @param b - Whether to enable or disable chunk culling.
     */
    fun setChunkCulling(b: Boolean)

    /**
     * Gets the current game window.
     *
     * @return The game window.
     */
    val window: Window

    /**
     * Gets the run-directory of the game.
     *
     * @return The run-directory of the game.
     */
    val runDirectory: File

    /**
     * Gets the target from the crosshair.
     *
     * @return The target from the crosshair.
     */
    fun crosshairTarget(): HitResult
    fun getPlayer(): ClientPlayerEntity


    /**
     * Gets the entity render dispatcher.
     *
     * @return The entity render dispatcher.
     */
    val entityRenderDispatcher: EntityRenderDispatcher

    /**
     * Gets the message handler.
     *
     * @return The message handler.
     */
    val messageHandler: MessageHandler

    companion object {
        /**
         * Gets the player.
         *
         * @return The player
         */
        val player: ClientPlayerEntity? = null
    }
}