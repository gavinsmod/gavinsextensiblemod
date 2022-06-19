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

import com.peasenet.mods.Type;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A simple dropdown gui element
 */
public class GuiDropdown extends GuiDraggable {

    /**
     * The list of buttons(mods) in this dropdown.
     */
    protected final ArrayList<Gui> buttons = new ArrayList<>();
    /**
     * The category of the dropdown.
     */
    protected Type.Category category;
    /**
     * Whether the dropdown is open.
     */
    private boolean isOpen;

    /**
     * Creates a new dropdown like UI element.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiDropdown(PointD position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    public void addElement(Gui button) {
        if (buttons.isEmpty()) {
            button.setPosition(new PointD(getX(), getY2() + 1));
            buttons.add(button);
            return;
        }
        // get last button
        Gui lastButton = buttons.get(buttons.size() - 1);
        var lastY = lastButton.getY2();
        // set new button position
        button.setPosition(new PointD(getX(), lastY + 1));
        buttons.add(button);
    }

    /**
     * Gets whether the dropdown is open.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Gets the category of the dropdown
     *
     * @return The category of the dropdown
     */
    public Type.Category getCategory() {
        return category;
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        // For each mod in the category, render it right below the title.
        super.render(matrices, tr);
        if (!isOpen())
            return;
        buttons.forEach(button -> button.render(matrices, tr));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check if the mouse is within the bounds of the dropdown.
        if (super.mouseClicked(mouseX, mouseY, button)) {
            // If the dropdown is open, close it.
            toggleMenu();
            return true;
        }
        if (isOpen()) {
            // If the dropdown is open, check if the mouse is within the bounds of any of the buttons.
            for (Gui g : buttons) {
                if (g.mouseClicked(mouseX, mouseY, button))
                    return true;
            }
        }
        return false;
    }


    /**
     * Toggles the dropdown.
     */
    private void toggleMenu() {
        isOpen = !isOpen;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // calculate the offset between the mouse position and the top left corner of the gui
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
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
    private void resetDropdownsLocation() {
        // copy buttons to a new array
        ArrayList<Gui> newButtons = new ArrayList<>(buttons);
        buttons.clear();
        newButtons.forEach(this::addElement);
    }
}
