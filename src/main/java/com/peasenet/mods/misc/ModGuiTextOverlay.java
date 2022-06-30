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
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A mod that shows the currently active mods in the top left screen.
 */
public class ModGuiTextOverlay extends Mod {
    public ModGuiTextOverlay() {
        super(Type.MOD_GUI_TEXT_OVERLAY);

        //NOTE: This isn't really the best place for this, but it works for now. this is for chat message toggles.
        ToggleSetting chatMessage = new ToggleSetting("chatMessage", "gavinsmod.settings.misc.messages");
        chatMessage.setValue(Settings.getBool("chatMessage"));
        addSetting(chatMessage);
    }

    @Override
    public void onRenderInGameHud(MatrixStack matrixStack, float delta) {
        if (GavinsMod.isEnabled(Type.MOD_GUI) || GavinsMod.isEnabled(Type.SETTINGS))
            return;
        drawTextOverlay(matrixStack);
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
        RenderUtils.drawBox((Settings.getColor("backgroundColor")).getAsFloatArray(), box, matrixStack);
        mods = GavinsMod.getModsForTextOverlay();
        AtomicInteger modCounter = new AtomicInteger();
        mods.forEach(mod -> {
            textRenderer.draw(matrixStack, Text.translatable(mod.getTranslationKey()), currX, currY.get(), (Settings.getColor("foregroundColor")).getAsInt());
            if (modsCount > 1 && modCounter.get() < modsCount - 1) {
                RenderUtils.drawSingleLine((Settings.getColor("foregroundColor")).getAsFloatArray(), currX - 1, currY.get() + 9, longestModName * 6 + 5, currY.get() + 9, matrixStack);
            }
            currY.addAndGet(12);
            modCounter.getAndIncrement();
        });
    }
}
