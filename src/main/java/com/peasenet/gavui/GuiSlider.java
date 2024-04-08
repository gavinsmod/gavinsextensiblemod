/*
 * MIT License
 *
 * Copyright (c) 2022-2024.
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

import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GuiUtil;
import com.peasenet.gavui.util.callbacks.GuiCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * A gui that allows for controlling a value with a slider.
 *
 * @author gt3ch1
 * @version 01/07/2022
 */
public class GuiSlider extends Gui {

    /**
     * Callback to be called when the slider is moved.
     */
    GuiCallback callback;

    /**
     * The current value of the slider.
     */
    float value;

    /**
     * Creates a new GUI menu.
     *
     * @param topLeft - The top left corner of the gui.
     * @param width   - The width of the gui.
     * @param height  - The height of the gui.
     * @param title   - The title of the gui.
     */
    public GuiSlider(PointF topLeft, int width, int height, Text title) {
        super(topLeft, width, height, title);
    }

    public GuiSlider(GuiBuilder builder) {
        super(builder);
        if (builder.getCallback() != null)
            setCallback(builder.getCallback());
        setValue(builder.getSlideValue());
    }

    /**
     * Sets the callback for when the slider is moved.
     *
     * @param callback - The callback to be called.
     */
    public void setCallback(GuiCallback callback) {
        this.callback = callback;
    }

    /**
     * Gets the current value of the slider.
     *
     * @return The current value of the slider.
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the current value of the slider.
     *
     * @param value - The value to set the slider to.
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * Sets the current value of the slider based on the mouse position.
     *
     * @param mouseX - The x position of the mouse.
     */
    private void setValue(double mouseX) {
        value = (float) ((mouseX - getX()) / (getWidth() - 2));
        value = Math.max(0, Math.min(1, value));
        // round to 2 decimal places
        value = Math.round(value * 100) / 100f;
        if (callback != null)
            callback.callback();
    }

    @Override
    public void render(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        super.render(drawContext, tr, mouseX, mouseY, delta);
        if (!isHidden())
            drawTickMark(drawContext);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (clickedGui != null && !clickedGui.equals(this))
            return false;
        if (this.equals(clickedGui) && !isHidden()) {
            setValue(mouseX);
            return true;
        }
        if ((button == 0 && (mouseWithinGui(mouseX, mouseY)))) {
            setValue(mouseX);
            clickedGui = this;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && mouseWithinGui(mouseX, mouseY) && !isHidden()) {
            setValue(mouseX);
            clickedGui = this;
            GavUI.LOGGER.info("Clicked on slider " + getUUID());
            return true;
        }
        return false;
    }

    /**
     * Draws the tick mark on the slider.
     *
     * @param drawContext - The draw matrix to draw on.
     */
    private void drawTickMark(DrawContext drawContext) {
        var box = new BoxF(new PointF(((getX()) + ((getWidth() - 1) * value)), getY()), 1, getHeight());
        GuiUtil.drawBox(Colors.WHITE, box, drawContext.getMatrices(), 0.75f);
    }
}
