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

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A mod that renders the current frames per second in the top right corner of the screen.
 */
public class ModFpsCounter extends Mod {
    public ModFpsCounter() {
        super(Type.MOD_FPS_COUNTER);
        ToggleSetting fpsColors = new ToggleSetting("fpsColors", "key.gavinsmod.settings.fpscolors");
        ColorSetting fpsSlowColor = new ColorSetting("slowFpsColor", "The color of the FPS counter when it is below the slow threshold.", "key.gavinsmod.settings.slowfps");
        ColorSetting fpsOkColor = new ColorSetting("okFpsColor", "The color of the FPS counter when it is between the slow and fast thresholds.", "key.gavinsmod.settings.okfps");
        ColorSetting fpsFastColor = new ColorSetting("fastFpsColor", "The color of the FPS counter when it is above the fast threshold.", "key.gavinsmod.settings.fastfps");
        addSetting(fpsColors);
        addSetting(fpsSlowColor);
        addSetting(fpsOkColor);
        addSetting(fpsFastColor);
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
        var color = Settings.getColor("foregroundColor");
        if (Settings.getBool("fpsColors")) {
            if (fps >= maximumFps * 0.85) color = Settings.getColor("fastFpsColor");
            else if (fps > maximumFps * 0.45 && fps < maximumFps * 0.85)
                color = Settings.getColor("okFpsColor");
            else color = Settings.getColor("slowFpsColor");
        }
        RenderUtils.drawBox((Settings.getColor("backgroundColor")).getAsFloatArray(), box, matrixStack);
        textRenderer.draw(matrixStack, Text.literal(fpsString), xCoordinate, 2, color.getAsInt());
    }
}
