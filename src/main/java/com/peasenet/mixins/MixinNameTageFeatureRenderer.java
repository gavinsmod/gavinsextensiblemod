package com.peasenet.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.peasenet.config.render.HealthTagConfig;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.util.ChatCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

/**
 *
 * @author GT3CH1
 * @version 12-24-2025
 * @since 12-24-2025
 */
@Mixin(NameTagFeatureRenderer.class)
public class MixinNameTageFeatureRenderer {
    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    void test(SubmitNodeCollection submitNodeCollection, MultiBufferSource.BufferSource bufferSource, Font font, CallbackInfo ci) {
        if (Mods.isActive(ChatCommand.HealthTag)) {
            var backgorundColor = getSettings().getBackgroundColor().getAsInt(getSettings().getAlpha());
            NameTagFeatureRenderer.Storage storage = submitNodeCollection.getNameTagSubmits();
            storage.nameTagSubmitsSeethrough.sort(Comparator.comparing(SubmitNodeStorage.NameTagSubmit::distanceToCameraSq).reversed());
            for (SubmitNodeStorage.NameTagSubmit nameTagSubmit : storage.nameTagSubmitsSeethrough) {
                font.drawInBatch(nameTagSubmit.text(), nameTagSubmit.x(), nameTagSubmit.y(), nameTagSubmit.color(), false, nameTagSubmit.pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, backgorundColor, nameTagSubmit.lightCoords());
            }
            for (SubmitNodeStorage.NameTagSubmit nameTagSubmit : storage.nameTagSubmitsNormal) {
                font.drawInBatch(nameTagSubmit.text(), nameTagSubmit.x(), nameTagSubmit.y(), nameTagSubmit.color(), false, nameTagSubmit.pose(), bufferSource, Font.DisplayMode.NORMAL, backgorundColor, nameTagSubmit.lightCoords());
            }
            ci.cancel();
        }
//        original.call(instance);
    }
    private HealthTagConfig getSettings() {
        return Settings.INSTANCE.getConfig(ChatCommand.HealthTag);
    }

}
