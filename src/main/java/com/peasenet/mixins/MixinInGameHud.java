package com.peasenet.mixins;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gt3ch1
 * @version 6/9/2022
 * A mixin that allows modding of the in game hud (ie, overlays, extra text, etc.)
 */
@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
    private void mixin(MatrixStack matrixStack, float delta, CallbackInfo ci) {
        drawTextOverlay(matrixStack);
    }


    @Inject(at = @At("HEAD"), method = "renderOverlay(Lnet/minecraft/util/Identifier;F)V", cancellable = true)
    private void antiPumpkin(Identifier texture, float opacity, CallbackInfo ci) {
        if (Objects.equals(texture, new Identifier("textures/misc/pumpkinblur.png")) && GavinsMod.isEnabled(Type.ANTI_PUMPKIN)) {
            ci.cancel();
        }
    }

    private void drawTextOverlay(MatrixStack matrixStack) {
        var textRenderer = GavinsModClient.getMinecraftClient().textRenderer;
        int currX = 10;
        if (GavinsMod.isEnabled(Type.MOD_GUI) || !GavinsMod.isEnabled(Type.MOD_GUI_TEXT_OVERLAY))
            return;
        AtomicInteger currY = new AtomicInteger(10);
        // only get active mods, and mods that are not gui type.
        GavinsMod.mods.stream().filter(mod -> mod.isActive() && mod.getCategory() != Type.Category.GUI).sorted(Comparator.comparing(Mod::getName))
                .forEach(mod -> {
                    textRenderer.drawWithShadow(matrixStack, Text.translatable(mod.getTranslationKey()), currX, currY.get(), 0xFFFFFF);
                    currY.addAndGet(12);
                });
    }
}
