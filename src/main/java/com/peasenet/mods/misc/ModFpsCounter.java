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

import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A mod that renders the current frames per second in the top right corner of the screen.
 */
public class ModFpsCounter extends Mod {
    public ModFpsCounter() {
        super(Type.MOD_FPS_COUNTER);
        SubSetting fpsSetting = new SubSetting(50, 10, "gavinsmod.settings.misc.fpscolors");
        ToggleSetting fpsColors = new ToggleSetting("misc.fps.color.enabled", "gavinsmod.settings.misc.fpscolors.enabled");
        ColorSetting fpsSlowColor = new ColorSetting("misc.fps.color.slow", "gavinsmod.settings.misc.fps.color.slow");
        ColorSetting fpsOkColor = new ColorSetting("misc.fps.color.ok", "gavinsmod.settings.misc.fps.color.ok");
        ColorSetting fpsFastColor = new ColorSetting("misc.fps.color.fast", "gavinsmod.settings.misc.fps.color.fast");
        fpsSetting.add(fpsColors);
        fpsSetting.add(fpsSlowColor);
        fpsSetting.add(fpsOkColor);
        fpsSetting.add(fpsFastColor);
        fpsSetting.getGui().setDirection(GuiDropdown.Direction.RIGHT);
        addSetting(fpsSetting);

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
        var color = Settings.getColor("gui.color.foreground");
        if (Settings.getBool("misc.fps.color.enabled")) {
            if (fps >= maximumFps * 0.85) color = Settings.getColor("misc.fps.color.fast");
            else if (fps > maximumFps * 0.45 && fps < maximumFps * 0.85) color = Settings.getColor("misc.fps.color.ok");
            else color = Settings.getColor("misc.fps.color.slow");
        }
        RenderUtils.drawBox((Settings.getColor("gui.color.background")).getAsFloatArray(), box, matrixStack);
        textRenderer.draw(matrixStack, Text.literal(fpsString), xCoordinate, 2, color.getAsInt());
    }
}
