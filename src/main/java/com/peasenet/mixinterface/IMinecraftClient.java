/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mixinterface;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;

import java.io.File;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A mixin interface for the Minecraft client to allow access for certain members that are not public.
 */
public interface IMinecraftClient {

    /**
     * Sets the cooldown time for using any item.
     *
     * @param cooldown - The cooldown time.
     */
    void setItemUseCooldown(int cooldown);


    /**
     * The current frames per second.
     *
     * @return The current frames per second.
     */
    int getFps();

    /**
     * Gets the current options for the game.
     *
     * @return The current options for the game.
     */
    GameOptions getOptions();

    /**
     * Gets the player.
     *
     * @return The player
     */
    ClientPlayerEntity getPlayer();

    /**
     * Gets the player interaction manager.
     *
     * @return The player interaction manager.
     */
    ClientPlayerInteractionManager getPlayerInteractionManager();

    /**
     * Gets the text renderer of the client.
     *
     * @return The text renderer.
     */
    TextRenderer getTextRenderer();

    /**
     * Gets the world that the player is currently in.
     *
     * @return The current world.
     */
    ClientWorld getWorld();

    /**
     * Sets the current in game screen to the given parameter
     *
     * @param s - The screen to change to.
     */
    void setScreen(Screen s);

    /**
     * Gets the current world renderer.
     *
     * @return The world renderer.
     */
    WorldRenderer getWorldRenderer();

    /**
     * Enables or disables chunk culling (rendering only chunks that are in the viewport).
     *
     * @param b - Whether to enable or disable chunk culling.
     */
    void setChunkCulling(boolean b);

    /**
     * Gets the current game window.
     *
     * @return The game window.
     */
    Window getWindow();

    File getRunDirectory();

    HitResult crosshairTarget();

    EntityRenderDispatcher getEntityRenderDispatcher();

    MessageHandler getMessageHandler();
}
