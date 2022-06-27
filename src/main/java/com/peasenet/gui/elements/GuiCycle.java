/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */


package com.peasenet.gui.elements;

import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Settings;
import com.peasenet.util.SettingsCallback;
import com.peasenet.util.math.PointD;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/27/2022
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

    private SettingsCallback callback;

    /**
     * Creates a new GUI menu.
     *
     * @param position - The position of the menu.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiCycle(PointD position, int width, int height, Text title, int cycleSize) {
        super(position, width, height, title);
        setCycleSize(cycleSize);
    }

    public GuiCycle(int width, int height, Text title, int cycleSize) {
        super(new PointD(0, 0), width, height, title);
        setCycleSize(cycleSize);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int increment = button == 0 ? 1 : -1;
        if (mouseWithinGui(mouseX, mouseY)) {
            // move the cycle index by the increment, wrapping around if necessary
            currentIndex = (currentIndex + increment);
            // y is modulo not working
            if (currentIndex < 0) {
                currentIndex = cycleSize - 1;
            } else if (currentIndex >= cycleSize) {
                currentIndex = 0;
            }
            if (Settings.getBool("guiSounds")) {
                GavinsModClient.getMinecraftClient().getPlayer().playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
            }
            callback.callback();
            // play button click sound

            return true;
        }
        return false;
    }

    public int getCycleSize() {
        return cycleSize;
    }

    public void setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setCallback(SettingsCallback callback) {
        this.callback = callback;
    }
}
