/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
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

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.callbacks.GuiCallback;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 05/18/2023
 * A builder for all GavUI GUIs.
 */
public class GuiBuilder {
    private float width;
    private float height;
    private PointF topLeft = new PointF(0, 0);
    private boolean hoverable = true;
    private boolean hidden;
    private boolean frozen;
    private boolean isOpen;
    private boolean isOn;

    private boolean drawBorder = true;

    private Text title;
    private Gui parent;
    private char symbol = '\0';
    private Color backgroundColor;
    private BoxF boxF;
    private BoxF defaultPosition;
    private GuiCallback callback;
    private GuiCallback renderCallback;
    private int cycleSize;
    private int currentCycleIndex;
    private int maxChildren;
    private int defaultMaxChildren;
    private float slideValue;
    private String settingKey;
    private ArrayList<Gui> children;
    private String translationKey;
    private float transparency = -1;
    private GuiDropdown.Direction direction = GuiDropdown.Direction.DOWN;

    public GuiDropdown.Direction getDirection() {
        return direction;
    }

    public GuiBuilder setDirection(GuiDropdown.Direction direction) {
        this.direction = direction;
        return this;
    }

    public float getTransparency() {
        return transparency;
    }

    public GuiBuilder setTransparency(float transparency) {
        this.transparency = transparency;
        return this;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public ArrayList<Gui> getChildren() {
        return children;
    }

    public GuiBuilder setChildren(@NotNull ArrayList<Gui> children) {
        this.children = children;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public GuiBuilder setWidth(float width) {
        this.width = width;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public boolean getDrawBorder() {
        return drawBorder;
    }

    public GuiBuilder setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
        return this;
    }

    public GuiBuilder setWidth(int width) {
        this.width = width;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public float getHeight() {
        return height;
    }

    public GuiBuilder setHeight(float height) {
        this.height = height;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public GuiBuilder setHeight(int height) {
        this.height = height;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public PointF getTopLeft() {
        return topLeft;
    }

    public GuiBuilder setTopLeft(PointF point) {
        topLeft = point;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public GuiBuilder setGuiSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public boolean isHoverable() {
        return hoverable;
    }

    public GuiBuilder setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
        return this;
    }

    public boolean isHidden() {
        return hidden;
    }

    public GuiBuilder setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public GuiBuilder setFrozen(boolean frozen) {
        this.frozen = frozen;
        return this;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isOn() {
        return isOn;
    }

    public Text getTitle() {
        return title;
    }

    public GuiBuilder setTitle(String translationKey) {
        if (translationKey == null || translationKey.isEmpty())
            return this;
        this.title = Text.translatable(translationKey);
        this.translationKey = translationKey;
        return this;
    }

    public GuiBuilder setTranslationKey(String translationKey) {
        setTitle(translationKey);
        return this;
    }

    public GuiBuilder setTitle(Text title) {
        this.title = title;
        return this;
    }

    public Gui getParent() {
        return parent;
    }

    public GuiBuilder setParent(Gui parent) {
        this.parent = parent;
        return this;
    }

    public char getSymbol() {
        return symbol;
    }

    public GuiBuilder setSymbol(char symbol) {
        this.symbol = symbol;
        return this;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public GuiBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public BoxF getBoxF() {
        return boxF;
    }

    public BoxF getDefaultPosition() {
        return defaultPosition;
    }

    public GuiBuilder setDefaultPosition(BoxF defaultPosition) {
        this.defaultPosition.from(defaultPosition);
        return this;
    }

    public GuiCallback getCallback() {
        return callback;
    }

    public GuiBuilder setCallback(GuiCallback callback) {
        this.callback = callback;
        return this;
    }

    public GuiCallback getRenderCallback() {
        return renderCallback;
    }

    public GuiBuilder setRenderCallback(GuiCallback renderCallback) {
        this.renderCallback = renderCallback;
        return this;
    }

    public int getCycleSize() {
        return cycleSize;
    }

    public GuiBuilder setCycleSize(int cycleSize) {
        this.cycleSize = cycleSize;
        return this;
    }

    public int getCurrentCycleIndex() {
        return currentCycleIndex;
    }

    public GuiBuilder setCurrentCycleIndex(int currentCycleIndex) {
        this.currentCycleIndex = currentCycleIndex;
        return this;
    }

    public int getMaxChildren() {
        return maxChildren;
    }

    public GuiBuilder setMaxChildren(int maxChildren) {
        this.maxChildren = maxChildren;
        return this;
    }

    public int getDefaultMaxChildren() {
        return defaultMaxChildren;
    }

    public GuiBuilder setDefaultMaxChildren(int defaultMaxChildren) {
        this.defaultMaxChildren = defaultMaxChildren;

        return this;
    }

    public float getSlideValue() {
        return slideValue;
    }

    public GuiBuilder setSlideValue(float slideValue) {
        this.slideValue = slideValue;
        return this;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public GuiBuilder setSettingKey(String settingKey) {
        this.settingKey = settingKey;
        return this;
    }

    public GuiBuilder setTopLeft(int x, int y) {
        topLeft = new PointF(x, y);
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }

    public GuiBuilder setTopLeft(float x, float y) {
        topLeft = new PointF(x, y);
        this.boxF = new BoxF(topLeft, width, height);
        return this;
    }


    public GuiBuilder setMidPoint(PointF midPoint) {
        this.boxF.setMiddle(midPoint);
        return this;
    }

    public GuiBuilder setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        return this;
    }

    public GuiBuilder setIsOn(boolean isOn) {
        this.isOn = isOn;
        return this;
    }

    public GuiSlider buildSlider() {
        validate();
        return new GuiSlider(this);
    }

    public GuiCycle buildCycle() {
        validate();
        return new GuiCycle(this);
    }

    public GuiToggle buildToggle() {
        validate();
        return new GuiToggle(this);
    }

    public GuiScroll buildScroll() {
        validate();
        return new GuiScroll(this);
    }

    public Gui build() {
        validate();
        return new Gui(this);
    }

    public GuiClick buildClick() {
        validate();
        return new GuiClick(this);
    }

    private void validate() {
        if (topLeft == null)
            topLeft = new PointF(0, 0);
        if (width == 0)
            width = 100;
        if (height == 0)
            height = 10;
        if (children == null)
            children = new ArrayList<>();
    }
}
