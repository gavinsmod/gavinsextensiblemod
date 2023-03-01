/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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

import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.gavui.util.GuiUtil;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.listeners.InGameHudRenderListener;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gt3ch1
 * @version 01/07/2023
 * A mod that shows the currently active mods in the top left screen.
 */
public class ModGuiTextOverlay extends Mod implements InGameHudRenderListener {
    public ModGuiTextOverlay() {
        super(Type.MOD_GUI_TEXT_OVERLAY);

        //NOTE: This isn't really the best place for this, but it works for now. this is for chat message toggles.
        ToggleSetting chatMessage = new ToggleSetting("gavinsmod.settings.misc.messages");
        chatMessage.setCallback(() -> miscConfig.setMessages(chatMessage.getValue()));
        chatMessage.setValue(miscConfig.isMessages());
        addSetting(chatMessage);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(InGameHudRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        em.unsubscribe(InGameHudRenderListener.class, this);
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
        var startingPoint = new PointF(0.5f, 0.5f);
        int currX = (int) (startingPoint.x() + 2);
        AtomicInteger currY = new AtomicInteger((int) (startingPoint.y() + 2));
        if (GavinsMod.isEnabled(Type.MOD_GUI) || !GavinsMod.isEnabled(Type.MOD_GUI_TEXT_OVERLAY) || GavinsMod.isEnabled(Type.SETTINGS))
            return;
        // only get active mods, and mods that are not gui type.
        var mods = GavinsMod.getModsForTextOverlay().toList();
        var modsCount = (int) GavinsMod.getModsForTextOverlay().count();
        if (modsCount == 0)
            return;
        // get the mod with the longest name.
        var longestModName = 0;
        for (Mod mod : mods) {
            longestModName = Math.max(textRenderer.getWidth(I18n.translate(mod.getTranslationKey())), longestModName);
        }
        var box = new BoxF(startingPoint, longestModName + 4, modsCount * 10 + 0.5f);
        GuiUtil.drawBox((GavUISettings.getColor("gui.color.background")), box, matrixStack, GavUISettings.getFloat("gui.alpha"));
        GuiUtil.drawOutline((GavUISettings.getColor("gui.color.border")), box, matrixStack);
        AtomicInteger modCounter = new AtomicInteger();
        for (Mod mod : mods) {
            textRenderer.draw(matrixStack, Text.translatable(mod.getTranslationKey()), currX, currY.get(), (GavUISettings.getColor("gui.color.foreground")).getAsInt());
            if (modsCount > 1 && modCounter.get() < modsCount - 1) {
                var p1 = new PointF(0.5f, (float) currY.get() + 8.5f);
                var p2 = new PointF(longestModName + 4.5f, currY.get() + 8.5f);
                GuiUtil.renderSingleLine((GavUISettings.getColor("gui.color.border")), p1, p2, matrixStack, 1f);
            }
            currY.addAndGet(10);
            modCounter.getAndIncrement();
        }
    }
}
