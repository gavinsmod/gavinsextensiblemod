/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Settings;
import com.peasenet.mods.Type;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
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

    /**
     * Draws the GUI text overlay if enabled, and if the main menu is not open.
     *
     * @param matrixStack - The matrix stack to use.
     */
    private void drawTextOverlay(MatrixStack matrixStack) {
        var textRenderer = GavinsModClient.getMinecraftClient().getTextRenderer();
        var startingPoint = new PointD(0, 0);
        int currX = (int) (startingPoint.x() + 2);
        AtomicInteger currY = new AtomicInteger((int) (startingPoint.y() + 2));
        drawFpsOverlay(matrixStack, textRenderer);
        if (GavinsMod.isEnabled(Type.MOD_GUI) || !GavinsMod.isEnabled(Type.MOD_GUI_TEXT_OVERLAY) || GavinsMod.isEnabled(Type.SETTINGS))
            return;
        // only get active mods, and mods that are not gui type.
        var mods = GavinsMod.getModsForTextOverlay();
        var modsCount = (int) GavinsMod.getModsForTextOverlay().count();
        if (modsCount == 0)
            return;
        // get the mod with the longest name.
        var longestModName = mods.max(Comparator.comparingInt(mod -> mod.getName().length())).get().getName().length();
        var box = new BoxD(startingPoint, longestModName * 6 + 6, modsCount * 12);
        RenderUtils.drawBox(Settings.BackgroundColor.getAsFloatArray(), box, matrixStack);
        mods = GavinsMod.getModsForTextOverlay();
        AtomicInteger modCounter = new AtomicInteger();
        mods.forEach(mod -> {
            textRenderer.draw(matrixStack, Text.translatable(mod.getTranslationKey()), currX, currY.get(), Settings.ForegroundColor.getAsInt());
            if (modsCount > 1 && modCounter.get() < modsCount - 1) {
                RenderUtils.drawSingleLine(Settings.ForegroundColor.getAsFloatArray(), currX - 1, currY.get() + 9, longestModName * 6 + 5, currY.get() + 9, matrixStack);
            }
            currY.addAndGet(12);
            modCounter.getAndIncrement();
        });
    }

    /**
     * Draws the FPS overlay if enabled.
     *
     * @param matrixStack  - The matrix stack to use.
     * @param textRenderer - The text renderer to use.
     */
    private void drawFpsOverlay(MatrixStack matrixStack, TextRenderer textRenderer) {
        if (!GavinsMod.isEnabled(Type.MOD_FPS_COUNTER))
            return;
        var fps = GavinsModClient.getMinecraftClient().getFps();
        var fpsString = "FPS: " + fps;
        var xCoordinate = GavinsModClient.getMinecraftClient().getWindow().getScaledWidth() - (fpsString.length() * 5 + 2);
        var box = new BoxD(new PointD(xCoordinate - 2, 0), fpsString.length() * 5 + 4, 12);
        var maximumFps = GavinsModClient.getMinecraftClient().getOptions().getMaxFps().getValue();
        var color = Settings.ForegroundColor;
        // if fps is within 5% of the maximum, use green.
        if (fps > maximumFps * 0.95)
            color = Settings.FastFpsColor;
        else if (fps > maximumFps * 0.45 && fps < maximumFps * 0.75)
            color = Settings.OkFpsColor;
        else
            color = Settings.SlowFpsColor;

        RenderUtils.drawBox(Settings.BackgroundColor.getAsFloatArray(), box, matrixStack);
        textRenderer.draw(matrixStack, Text.literal(fpsString), xCoordinate, 2, color.getAsInt());
    }
}
