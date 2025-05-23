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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.hit.HitResult;
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
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements IMinecraftClient {

    /**
     * The current FPS, as reported by the game.
     */
    @Shadow
    private static int currentFps;

    /**
     * The interaction manager for the player.
     */
    @Shadow
    public ClientPlayerInteractionManager interactionManager;


    /**
     * The options for the game.
     */
    @Shadow
    @Final
    public GameOptions options;

    /**
     * The textRenderer used to draw text.
     */
    @Shadow
    @Final
    public TextRenderer textRenderer;

    /**
     * The renderer used for the world.
     */
    @Shadow
    @Final
    public WorldRenderer worldRenderer;

    /**
     * Whether chunk culling is enabled.
     */
    @Shadow
    public boolean chunkCullingEnabled;

    /**
     * The world the player is in.
     */
    @Shadow
    public ClientWorld world;

    /**
     * The directory where minecraft is running from.
     */
    @Shadow
    @Final
    public File runDirectory;

    /**
     * What is currently being looked at.
     */
    @Shadow
    public HitResult crosshairTarget;

    /**
     * The player.
     */
    @Shadow
    public ClientPlayerEntity player;

    /**
     * The Entity Renderer Dispatcher.
     */
    @Final
    @Shadow
    private EntityRenderDispatcher entityRenderDispatcher;

    @Final
    @Shadow
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    /**
     * The current cool down from using an item.
     */
    @Shadow
    private int itemUseCooldown;

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
    @Inject(at = @At(value = "HEAD"), method = "isAmbientOcclusionEnabled()Z", cancellable = true)
    private static void isXrayOrFullBright(CallbackInfoReturnable<Boolean> ci) {
        boolean disabled = GavinsMod.isEnabled("xray") || GavinsMod.isEnabled("fullbright");
        ci.setReturnValue(!disabled);
        ci.cancel();
    }

    /**
     * The network message handler.
     */
    @Shadow
    public abstract MessageHandler getMessageHandler();

    /**
     * Sets the current item use cooldown.
     *
     * @param cooldown - the new cooldown
     */
    @Override
    public void setItemUseCooldown(int cooldown) {
        itemUseCooldown = cooldown;
    }

    /**
     * Gets the current FPS.
     *
     * @return - the current FPS
     */
    @Override
    public int getFps() {
        return currentFps;
    }


    /**
     * Gets the GameOptions
     *
     * @return GameOptions
     */
    @Override
    public GameOptions getOptions() {
        return options;
    }

    /**
     * Gets the player interaction manager.
     *
     * @return the player interaction manager
     */
    @Override
    public ClientPlayerInteractionManager getPlayerInteractionManager() {
        return interactionManager;
    }

    /**
     * Gets the text renderer.
     *
     * @return - the text renderer
     */
    @Override
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    /**
     * Gets the world.
     *
     * @return - the world
     */
    @Override
    public ClientWorld getWorld() {
        return world;
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
    private BufferBuilderStorage bufferBuilders;

    /**
     * Gets the renderer for the world.
     *
     * @return the world renderer
     */
    @Override
    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    /**
     * Sets whether chunk culling is enabled.
     *
     * @param b - whether chunk culling is enabled
     */
    @Override
    public void setChunkCulling(boolean b) {
        chunkCullingEnabled = b;
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
        return runDirectory;
    }

    /**
     * Gets the current crosshair target.
     *
     * @return the crosshair target
     */
    @Override
    public HitResult crosshairTarget() {
        return crosshairTarget;
    }

    /**
     * Gets the entity render dispatcher.
     *
     * @return the entity render dispatcher
     */
    @Override
    public EntityRenderDispatcher getEntityRenderDispatcher() {
        return entityRenderDispatcher;
    }

    @Override
    public BlockEntityRenderDispatcher getBlockEntityRenderDispatcher() {
        return blockEntityRenderDispatcher;
    }

    /**
     * Gets the current player.
     *
     * @return the player
     */
    @Override
    public ClientPlayerEntity getPlayer() {
        return player;
    }

    @Override
    public BufferBuilderStorage getBufferBuilderStorage() {
        return bufferBuilders;
    }
}
