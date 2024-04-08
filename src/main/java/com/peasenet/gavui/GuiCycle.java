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

import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GavUISettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 01/07/2023
 * A gui element that cycles through a list of settings when clicked on.
 * A callback is called when clicked on.
 */
public class GuiCycle extends GuiClick {

    /**
     * The size of the cycle.
     */
    private int cycleSize;

    /**
     * The current index of the cycle.
     */
    private int currentIndex;

    /**
     * Creates a new gui that can go through a given amount of cycles.
     *
     * @param width     - The width of the gui.
     * @param height    - The height of the gui.
     * @param title     - The title of the gui.
     * @param cycleSize - The size of the cycle.
     */
    public GuiCycle(int width, int height, Text title, int cycleSize) {
        super(new PointF(0, 0), width, height, title);
        setCycleSize(cycleSize);
    }

    public GuiCycle(GuiBuilder builder) {
        super(builder);
        setCycleSize(builder.getCycleSize());
        setCurrentIndex(builder.getCurrentCycleIndex());
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int increment = button == 0 ? 1 : -1;
        if (mouseWithinGui(mouseX, mouseY)) {
            // move the cycle index by the increment, wrapping around if necessary
            currentIndex = (currentIndex + increment);
            // y is modulo not working
            currentIndex = currentIndex % cycleSize;
            if (GavUISettings.getBool("gui.sound")) {
                MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 1.0F, 1.0F);
            }
            if (onClick != null)
                onClick.callback();
            if (callback != null)
                callback.callback();

            return true;
        }
        return false;
    }

    /**
     * Sets the maximum amount of times this element can be clicked before wrapping around.
     *
     * @param cycleSize - The size of the cycle.
     */
    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    /**
     * Gets the current index of the cycle.
     *
     * @return The current index of the cycle.
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Sets the index of the cycle.
     *
     * @param currentIndex - The index of the cycle.
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
