package com.peasenet.mixins;

import com.peasenet.gui.MainScreen;
import com.peasenet.main.GavinsModClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderGui(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Screen screen = GavinsModClient.getMinecraftClient().currentScreen;
        if (!(screen instanceof AbstractInventoryScreen))
            return;
        GavinsModClient.getMinecraftClient().setScreen(new MainScreen(null));
    }
}
