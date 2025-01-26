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
import net.minecraft.text.Text;

/**
 * @author GT3CH1
 * @version 6/28/2022
 * A draggable ui element.
 */
public class GuiDraggable extends GuiClick {

    /**
     * Whether this element is frozen.
     */
    private boolean frozen = false;

    /**
     * Creates a new GUI draggable object.
     *
     * @param position - The position of the draggable object.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiDraggable(PointF position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    public GuiDraggable(GuiBuilder builder) {
        super(builder);
        this.setFrozen(builder.isFrozen());
    }

    /**
     * Gets whether the dropdown is frozen, meaning it cannot be moved.
     *
     * @return Whether the dropdown is frozen.
     */
    public boolean frozen() {
        return frozen;
    }

    /**
     * Sets whether the dropdown is frozen, meaning it cannot be moved.
     *
     * @param frozen Whether the dropdown is frozen.
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (frozen())
            return false;
        var childOverride = false;
        setMidPoint(new PointF(mouseX, mouseY));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            setFrozen(!frozen());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
