/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.peasenet.mixinterface

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.multiplayer.chat.ChatListener
import net.minecraft.client.Options
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import com.mojang.blaze3d.platform.Window
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.phys.HitResult
import java.io.File

/**
 * @author GT3CH1
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
    val options: Options

    /**
     * Gets the player interaction manager.
     *
     * @return The player interaction manager.
     */
    fun getPlayerInteractionManager(): MultiPlayerGameMode

    /**
     * Gets the text renderer of the client.
     *
     * @return The text renderer.
     */
    val textRenderer: Font

    /**
     * Gets the world that the player is currently in.
     *
     * @return The current world.
     */
    fun getWorld(): ClientLevel

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
    val worldRenderer: LevelRenderer

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
    fun getPlayer(): LocalPlayer


    /**
     * Gets the message handler.
     *
     * @return The message handler.
     */
    val messageHandler: ChatListener

    companion object {
        /**
         * Gets the player.
         *
         * @return The player
         */
        val player: LocalPlayer? = null
    }

    fun getBufferBuilderStorage(): RenderBuffers
}