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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * A simple dropdown gui element
 */
public class GuiDropdown extends GuiDraggable {

    /**
     * Whether the dropdown is open.
     */
    private boolean isOpen;
    /**
     * The direction in which this element will "drop" to.
     */
    private Direction direction = Direction.DOWN;

    /**
     * Creates a new dropdown like UI element.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiDropdown(PointF position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    public GuiDropdown(GuiBuilder builder) {
        super(builder);
        this.setOpen(builder.isOpen());

    }

    /**
     * Gets whether the dropdown is open.
     *
     * @return Whether the dropdown is open.
     */
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public void render(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        updateSymbol();
        var textColor = frozen() ? GavUISettings.getColor("gui.color.frozen") : GavUISettings.getColor("gui.color.foreground");
        drawText(drawContext, tr, String.valueOf(symbol), getX2() + symbolOffsetX, getY() + symbolOffsetY, textColor);
        super.render(drawContext, tr, mouseX, mouseY, delta);
        if (!isOpen()) return;
        var toRender = children.stream().filter(child -> !child.isHidden());
        // convert toRender to ArrayList
        var toRenderList = new ArrayList<>(toRender.toList());
        for (int i = 0; i < toRenderList.size(); i++) {
            var child = toRenderList.get(i);
            switch (getDirection()) {
                case DOWN -> child.setPosition(new PointF(getX(), getY2() + 2 + (i * 12)));
                case RIGHT -> child.setPosition(new PointF(getX2() + 8, getY() + (i * 12)));
            }
            child.render(drawContext, tr, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (isOpen()) {
            // If the dropdown is open, check if the mouse is within the bounds of any of the buttons.
            for (Gui g : children) {
                if (g.mouseClicked(mouseX, mouseY, button) && !g.isHidden()) {
                    for (Gui child : children) {
                        if (child instanceof GuiDropdown dropdown && !child.equals(g))
                            if (dropdown.isOpen()) dropdown.toggleMenu();
                    }
                    g.show();
                    return true;
                }
            }
        }
        // Check if the mouse is within the bounds of the dropdown.
        if (super.mouseClicked(mouseX, mouseY, button)) {
            // If the dropdown is open, close it.
            toggleMenu();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseWithinGui(double mouseX, double mouseY) {
        var inMain = super.mouseWithinGui(mouseX, mouseY);
        if (isOpen()) {
            for (Gui g : children) {
                if (g.mouseWithinGui(mouseX, mouseY) && !g.isHidden()) return true;
            }
        }
        return inMain;
    }

    /**
     * Toggles the dropdown.
     */
    protected void toggleMenu() {
        isOpen = !isOpen;
        if (GavUISettings.getBool("gui.sound")) {
            if (isOpen) MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5f, 1);
            else MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5f, 1);
        }
        for (Gui g : children) {
            if (!isOpen) g.hide();
            else g.show();
        }

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // calculate the offset between the mouse position and the top left corner of the gui
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            for (var children : children) {
                if (children.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
            isOpen = false;
            children.forEach(Gui::hide);
            resetDropdownsLocation();
            return true;
        }
        return false;
    }

    @Override
    public void resetPosition() {
        super.resetPosition();
        resetDropdownsLocation();
        isOpen = false;
    }

    /**
     * Resets the position of all the mods in the dropdown.
     */
    protected void resetDropdownsLocation() {
        // copy buttons to a new array
        for (Gui element : children) {
            if (getDirection() == Direction.RIGHT) {
                element.setPosition(new PointF(getX2() + 12, getY2() + (children.size()) * 12));
            }
        }

    }

    /**
     * Gets the direction that this dropdown will be displayed in.
     *
     * @return The direction that this dropdown will be displayed in.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction that this dropdown will be displayed in.
     *
     * @param direction - The direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void addElement(Gui element) {
        children.add(element);
        if (getDirection() == Direction.RIGHT) {
            element.setPosition(new PointF(getX2() + 12, getY2() + (children.size()) * 12));
        }
        element.setWidth(getWidth());
    }

    /**
     * Sets the symbol for the dropdown based off of what direction it is displayed in.
     */
    protected void updateSymbol() {
        symbol = ' ';
        symbolOffsetX = -10;
        symbolOffsetY = 2;
        if (!isOpen()) {
            switch (getDirection()) {
                case RIGHT -> {
                    symbol = '\u25B6';
                    symbolOffsetX = -8;
                }
                case DOWN -> {
                    symbol = '\u25BC';
                    symbolOffsetY = 3;
                    symbolOffsetX = -8;
                }
            }
        }
    }


    /**
     * A direction that represents which way the dropdown will be displayed.
     */
    public enum Direction {
        DOWN,
        RIGHT
    }
}
