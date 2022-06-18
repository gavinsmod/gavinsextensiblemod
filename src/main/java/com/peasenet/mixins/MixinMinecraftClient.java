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

package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.mixinterface.IMinecraftClient;
import com.peasenet.mods.Type;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gt3ch1
 * @version 5/18/2022
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements IMinecraftClient {

    @Shadow
    private static int currentFps;

    @Shadow
    public ClientPlayerInteractionManager interactionManager;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow
    @Final
    public TextRenderer textRenderer;

    @Shadow
    @Final
    public WorldRenderer worldRenderer;

    @Shadow
    public boolean chunkCullingEnabled;

    @Shadow
    public ClientWorld world;

    @Shadow
    protected int attackCooldown;

    @Shadow
    private int itemUseCooldown;

    @Final
    @Shadow
    private Window window;

    @Inject(at = @At(value = "HEAD"), method = "isAmbientOcclusionEnabled()Z", cancellable = true)
    private static void isXrayOrFullBright(CallbackInfoReturnable<Boolean> ci) {
        boolean disabled = GavinsMod.isEnabled(Type.XRAY) || GavinsMod.isEnabled(Type.FULL_BRIGHT);
        ci.setReturnValue(!disabled);
        ci.cancel();
    }

    @Override
    public void setItemUseCooldown(int cooldown) {
        itemUseCooldown = cooldown;
    }

    @Override
    public int getFps() {
        return currentFps;
    }

    @Override
    public ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    @Override
    public GameOptions getOptions() {
        return options;
    }

    @Override
    public ClientPlayerInteractionManager getPlayerInteractionManager() {
        return interactionManager;
    }

    @Override
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    @Override
    public ClientWorld getWorld() {
        return world;
    }

    @Shadow
    public abstract void setScreen(Screen s);

    @Override
    public WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    @Override
    public void setChunkCulling(boolean b) {
        chunkCullingEnabled = b;
    }

    @Override
    public Window getWindow() {
        return window;
    }

}
