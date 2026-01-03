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

package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mixinterface.IMinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

/**
 * @author GT3CH1
 * @version 5/18/2022
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraftClient implements IMinecraftClient {

    /**
     * The current FPS, as reported by the game.
     */
    @Shadow
    private static int fps;

    /**
     * The interaction manager for the player.
     */
    @Shadow
    public MultiPlayerGameMode gameMode;


    /**
     * The options for the game.
     */
    @Shadow
    @Final
    public Options options;

    /**
     * The textRenderer used to draw text.
     */
    @Shadow
    @Final
    public Font font;

    /**
     * The renderer used for the world.
     */
    @Shadow
    @Final
    public LevelRenderer levelRenderer;

    /**
     * Whether chunk culling is enabled.
     */
    @Shadow
    public boolean smartCull;

    /**
     * The world the player is in.
     */
    @Shadow
    public ClientLevel level;

    /**
     * The directory where minecraft is running from.
     */
    @Shadow
    @Final
    public File gameDirectory;

    /**
     * What is currently being looked at.
     */
    @Shadow
    public HitResult hitResult;

    /**
     * The player.
     */
    @Shadow
    public LocalPlayer player;


    /**
     * The current cool down from using an item.
     */
    @Shadow
    private int rightClickDelay;

    /**
     * The game window.
     */
    @Final
    @Shadow
    private Window window;

    /**
     * Mixin for whether ambient occlusion is enabled.
     *
     * @param ci - ignored
     */
    @Inject(at = @At(value = "HEAD"), method = "useAmbientOcclusion()Z", cancellable = true)
    private static void isXrayOrFullBright(CallbackInfoReturnable<Boolean> ci) {
        boolean disabled = GavinsMod.isEnabled("xray") || GavinsMod.isEnabled("fullbright");
        ci.setReturnValue(!disabled);
        ci.cancel();
    }

    /**
     * Sets the current item use cooldown.
     *
     * @param cooldown - the new cooldown
     */
    @Override
    public void setItemUseCooldown(int cooldown) {
        rightClickDelay = cooldown;
    }

    /**
     * Gets the current FPS.
     *
     * @return - the current FPS
     */
    @Override
    public int getFps() {
        return fps;
    }


    /**
     * Gets the GameOptions
     *
     * @return GameOptions
     */
    @Override
    public Options getOptions() {
        return options;
    }

    /**
     * Gets the player interaction manager.
     *
     * @return the player interaction manager
     */
    @Override
    public MultiPlayerGameMode getPlayerInteractionManager() {
        return gameMode;
    }

    /**
     * Gets the text renderer.
     *
     * @return - the text renderer
     */
    @Override
    public Font getTextRenderer() {
        return font;
    }

    /**
     * Gets the world.
     *
     * @return - the world
     */
    @Override
    public ClientLevel getWorld() {
        return level;
    }

    /**
     * Sets the screen.
     *
     * @param s - the screen to set
     */
    @Shadow
    public abstract void setScreen(Screen s);

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    /**
     * Gets the renderer for the world.
     *
     * @return the world renderer
     */
    @Override
    public LevelRenderer getWorldRenderer() {
        return levelRenderer;
    }

    /**
     * Sets whether chunk culling is enabled.
     *
     * @param b - whether chunk culling is enabled
     */
    @Override
    public void setChunkCulling(boolean b) {
        smartCull = b;
    }

    /**
     * Gets the game window.
     *
     * @return the window
     */
    @Override
    public Window getWindow() {
        return window;
    }

    /**
     * Gets the run directory of the game.
     *
     * @return the run directory
     */
    @Override
    public File getRunDirectory() {
        return gameDirectory;
    }

    /**
     * Gets the current crosshair target.
     *
     * @return the crosshair target
     */
    @Override
    public HitResult crosshairTarget() {
        return hitResult;
    }

    /**
     * Gets the current player.
     *
     * @return the player
     */
    @Override
    public LocalPlayer getPlayer() {
        return player;
    }

    @Override
    public RenderBuffers getBufferBuilderStorage() {
        return renderBuffers;
    }
}
