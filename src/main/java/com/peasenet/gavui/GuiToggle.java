/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.gavui;

import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.callbacks.GuiCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * @author GT3CH1
 * @version 7/1/2022
 * A simple toggleable gui element.
 */
public class GuiToggle extends GuiClick {

    /**
     * The callback method to be called when the element is clicked.
     */
    private GuiCallback callback;

    /**
     * Gets whether the toggle is on.
     */
    private boolean isOn;
    /**
     * The callback method to be called when the element is rendered.
     */
    private GuiCallback renderCallback;

    /**
     * Creates a new GUI menu.
     *
     * @param position - The position of the menu.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiToggle(PointF position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    public GuiToggle(int width, int height) {
        super(new PointF(0, 0), width, height, Text.of("none"));
    }

    public GuiToggle(GuiBuilder builder) {
        super(builder);
        this.setState(builder.isOn());
        this.setRenderCallback(builder.getRenderCallback());
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
    public void setCallback(GuiCallback callback) {
        this.callback = callback;
    }

    /**
     * The callback method to be called when the element is rendered.
     *
     * @param callback - The callback method.
     */
    public void setRenderCallback(GuiCallback callback) {
        renderCallback = callback;
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
    public void render(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        if (isHidden())
            return;
        symbol = isOn ? '\u2611' : '\u2610';
        if (renderCallback != null)
            renderCallback.callback();
        if (isOn()) setBackground(GavUI.enabledColor());
        else setBackground(GavUI.backgroundColor());
        super.render(drawContext, tr, mouseX, mouseY, delta);
    }
}
