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

package com.peasenet.gui.elements;

import com.peasenet.main.Settings;
import com.peasenet.util.SettingsCallback;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A simple toggleable gui element.
 */
public class GuiToggle extends GuiClick {

    /**
     * The callback method to be called when the element is clicked.
     */
    private SettingsCallback callback;

    /**
     * Gets whether the toggle is on.
     */
    private boolean isOn;
    /**
     * The callback method to be called when the element is rendered.
     */
    private SettingsCallback renderCallback;

    /**
     * Creates a new GUI menu.
     *
     * @param position - The position of the menu.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiToggle(PointD position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    /**
     * Sets the current state of this toggle element.
     *
     * @param on - the new state of this toggle element.
     */
    public void setState(boolean on) {
        isOn = on;
    }

    /**
     * Sets the callback method to be called when the toggle is clicked.
     *
     * @param callback - The callback method.
     */
    public void setCallback(SettingsCallback callback) {
        this.callback = callback;
    }

    /**
     * The callback method to be called when the element is rendered.
     *
     * @param callback - The callback method.
     */
    public void setRenderCallback(SettingsCallback callback) {
        this.renderCallback = callback;
    }

    /**
     * Gets whether the toggle is on.
     *
     * @return Whether the toggle is on.
     */
    public boolean isOn() {
        return isOn;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!super.mouseClicked(mouseX, mouseY, button) || isHidden()) return false;

        isOn = !isOn;
        if (callback != null) callback.callback();
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, TextRenderer tr) {
        if (isHidden()) return;
        symbol = isOn ? '\u2611' : '\u2610';
        if (renderCallback != null) renderCallback.callback();
        if (isOn()) setBackground(Settings.getColor("gui.color.enabled"));
        else setBackground(Settings.getColor("gui.color.background"));
        super.render(matrixStack, tr);
        tr.draw(matrixStack, String.valueOf(symbol), (int) getX2() + symbolOffsetX, (int) getY() + symbolOffsetY, (Settings.getColor("gui.color.foreground")).getAsInt());
    }
}
