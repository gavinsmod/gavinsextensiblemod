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
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author gt3ch1, ate47
 * Credit: <a href="https://github.com/ate47/Xray/">https://github.com/ate47/Xray/</a>
 * @version 5/18/2022
 */
@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient implements IMinecraftClient {

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
    ClientWorld world;
    @Shadow
    private int fpsCounter;
    @Shadow
    private int attackCooldown;
    @Shadow
    private int itemUseCooldown;

    @Inject(at = @At(value = "HEAD"), method = "isAmbientOcclusionEnabled()Z", cancellable = true)
    private static void ixXrayOrFullbright(CallbackInfoReturnable<Boolean> ci) {
        boolean disabled = GavinsMod.isEnabled(Type.XRAY) || GavinsMod.isEnabled(Type.FULL_BRIGHT);
        ci.setReturnValue(!disabled);
        ci.cancel();
    }

    @Override
    public int getAttackCooldown() {
        return attackCooldown;
    }

    @Override
    public void setAttackCooldown(int cooldown) {
        attackCooldown = cooldown;
    }

    @Override
    public int getItemUseCooldown() {
        return itemUseCooldown;
    }

    @Override
    public void setItemUseCooldown(int cooldown) {
        itemUseCooldown = cooldown;
    }

    @Override
    public int getFps() {
        return fpsCounter;
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
}
