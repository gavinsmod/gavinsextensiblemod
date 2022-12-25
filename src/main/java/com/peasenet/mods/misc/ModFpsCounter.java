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

package com.peasenet.mods.misc;

import com.peasenet.gavui.math.BoxD;
import com.peasenet.gavui.math.PointD;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.listeners.InGameHudRenderListener;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A mod that renders the current frames per second in the top right corner of the screen.
 */
public class ModFpsCounter extends Mod implements InGameHudRenderListener {
    public ModFpsCounter() {
        super(Type.MOD_FPS_COUNTER);
        SubSetting fpsSetting = new SubSetting(100, 10, "gavinsmod.settings.misc.fpscolors");

        ToggleSetting fpsColors = new ToggleSetting("gavinsmod.settings.misc.fpscolors.enabled");
        fpsColors.setCallback(() -> {
            GavinsMod.fpsColorConfig.setColorsEnabled(fpsColors.getValue());
        });
        fpsColors.setValue(GavinsMod.fpsColorConfig.isColorsEnabled());

        ColorSetting fpsSlowColor = new ColorSetting("gavinsmod.settings.misc.fps.color.slow");
        fpsSlowColor.setCallback(() -> {
            GavinsMod.fpsColorConfig.setSlowFps(fpsSlowColor.getColor());
        });
        fpsSlowColor.setColor(GavinsMod.fpsColorConfig.getSlowFps());

        ColorSetting fpsOkColor = new ColorSetting("gavinsmod.settings.misc.fps.color.ok");
        fpsOkColor.setCallback(() -> {
            GavinsMod.fpsColorConfig.setOkFps(fpsOkColor.getColor());
        });
        fpsOkColor.setColor(GavinsMod.fpsColorConfig.getOkFps());

        ColorSetting fpsFastColor = new ColorSetting("gavinsmod.settings.misc.fps.color.fast");
        fpsFastColor.setCallback(() -> {
            GavinsMod.fpsColorConfig.setFastFps(fpsFastColor.getColor());
        });
        fpsFastColor.setColor(GavinsMod.fpsColorConfig.getFastFps());

        fpsSetting.add(fpsColors);
        fpsSetting.add(fpsSlowColor);
        fpsSetting.add(fpsOkColor);
        fpsSetting.add(fpsFastColor);
        addSetting(fpsSetting);

    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(InGameHudRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(InGameHudRenderListener.class, this);
    }

    @Override
    public void onRenderInGameHud(MatrixStack matrixStack, float delta) {
        if (GavinsMod.isEnabled(Type.MOD_GUI) || GavinsMod.isEnabled(Type.SETTINGS) || !isActive()) return;
        drawFpsOverlay(matrixStack);
    }

    /**
     * Draws the FPS overlay if enabled.
     *
     * @param matrixStack - The matrix stack to use.
     */
    private void drawFpsOverlay(MatrixStack matrixStack) {
        var textRenderer = getClient().getTextRenderer();
        var fps = GavinsModClient.getMinecraftClient().getFps();
        var fpsString = "FPS: " + fps;
        var xCoordinate = GavinsModClient.getMinecraftClient().getWindow().getScaledWidth() - (fpsString.length() * 5 + 2);
        var box = new BoxD(new PointD(xCoordinate - 2, 0), fpsString.length() * 5 + 4, 12);
        var maximumFps = GavinsModClient.getMinecraftClient().getOptions().getMaxFps().getValue();
        var color = GavUISettings.getColor("gui.color.foreground");
        var cfg = GavinsMod.fpsColorConfig;
        var colorEnabled = cfg.isColorsEnabled();
        var fastColor = cfg.getFastFps();
        var okColor = cfg.getOkFps();
        var slowFps = cfg.getSlowFps();
        if (colorEnabled) {
            if (fps >= maximumFps * 0.85) color = fastColor;
            else if (fps > maximumFps * 0.45 && fps < maximumFps * 0.85) color = okColor;
            else color = slowFps;
        }
        RenderUtils.drawBox((GavUISettings.getColor("gui.color.background")).getAsFloatArray(), box, matrixStack, 0.5f);
        textRenderer.draw(matrixStack, Text.literal(fpsString), xCoordinate, 2, color.getAsInt());
    }
}
