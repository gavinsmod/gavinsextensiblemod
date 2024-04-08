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

package com.peasenet.gavui;

import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.gavui.util.callbacks.GuiCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * Creates a GUI that allows the user to toggle mods on and off by clicking.
 */
public class GuiClick extends Gui {

    /**
     * The callback used when the user clicks on the GUI.
     */
    protected GuiCallback callback;

    protected GuiCallback onClick;


    /**
     * Creates a new GUI menu.
     *
     * @param position - The position of the menu.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiClick(PointF position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    public GuiClick(GuiBuilder builder) {
        super(builder);
        this.setCallback(builder.getCallback());
    }

    /**
     * Sets the callback of this gui.
     *
     * @param callback - The callback.
     */
    public void setCallback(GuiCallback callback) {
        this.callback = callback;
    }

    public void setOnClick(GuiCallback onClick) {
        this.onClick = onClick;
    }

    /**
     * Handles clicks on the gui.
     *
     * @param mouseX The x coordinate of the mouse.
     * @param mouseY The y coordinate of the mouse.
     * @param button The mouse button that was clicked.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        // check if mouseX and mouseY are within the bounds of the gui.
        var inGui = mouseWithinGui(mouseX, mouseY) && !isHidden();
        if (inGui && GavUISettings.getBool("gui.sound"))
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1);
        if (inGui && !isHidden()) {
            if (onClick != null)
                onClick.callback();
            if (callback != null)
                callback.callback();
        }

        return inGui;
    }
}
