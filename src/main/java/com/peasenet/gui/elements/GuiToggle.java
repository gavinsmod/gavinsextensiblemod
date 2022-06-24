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
 * @version 6/17/2022
 * A simple toggleable gui element.
 */
public class GuiToggle extends GuiClick {

    public void setState(boolean on) {
        isOn = on;
    }

    /**
     * Gets whether the toggle is on.
     */
    private boolean isOn;

    public void setCallback(SettingsCallback callback) {
        this.callback = callback;
    }

    private SettingsCallback callback;

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
     * Gets whether the toggle is on.
     *
     * @return Whether the toggle is on.
     */
    public boolean isOn() {
        return isOn;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button) && !isHidden()) {
            isOn = !isOn;
            if (callback != null) {
                callback.callback();
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, TextRenderer tr) {
        if (isOn())
            setBackground(Settings.EnabledColor);
        else
            setBackground(Settings.BackgroundColor);
        super.render(matrixStack, tr);
    }
}
