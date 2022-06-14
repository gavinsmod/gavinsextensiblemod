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

import com.peasenet.main.GavinsMod;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static com.peasenet.mods.Type.Category;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A simple dropdown gui element
 */
public class GuiDropdown extends GuiDraggable {

    /**
     * The list of buttons(mods) in this dropdown.
     */
    private final ArrayList<GuiClick> buttons = new ArrayList<>();
    /**
     * Whether the dropdown is open.
     */
    private boolean isOpen;
    /**
     * The category of the dropdown.
     */
    private Category category;

    /**
     * Creates a new dropdown like UI element.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     * @param category - The category of the dropdown.
     */
    public GuiDropdown(PointD position, int width, int height, Text title, Category category) {
        super(position, width, height, title);
        setCategory(category);
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
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the dropdown
     */
    public void setCategory(Category category) {
        this.category = category;
        var mods = GavinsMod.getModsInCategory(category);
        if (mods == null)
            return;
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var x = getX();
            var y = getY2() + i * 10;
            buttons.add(new GuiClick(new PointD(x, y + 2), (int) getWidth(), (int) getHeight(), Text.translatable(mod.getTranslationKey())));
        }
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        // For each mod in the category, render it right below the title.
        super.render(matrices, tr);
        if (!isOpen())
            return;
        for (int i = 0; i < buttons.size(); i++) {
            var button = buttons.get(i);
            var mod = GavinsMod.getModsInCategory(category).get(i);
            if (mod.isActive())
                button.setBackground(Colors.GREEN);
            else
                button.setBackground(Colors.BLACK);
            button.render(matrices, tr);
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        // Check if the mouse is within the bounds of the dropdown.
        if (mouseX >= getX() && mouseX <= getX2() && mouseY >= getY() && mouseY <= getY2()) {
            // If the dropdown is open, close it.
            toggleMenu();
            return true;
        }
        // Check if the mouse is within the bounds of the mods.
        return toggleSelectedMod(mouseX, mouseY, button);
    }

    /**
     * Toggles the mod that the mouse is currently hovering over, if applicable.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was clicked.
     * @return Whether the mouse was clicked on a mod.
     */
    private boolean toggleSelectedMod(double mouseX, double mouseY, int button) {
        var mods = GavinsMod.getModsInCategory(category);
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var gui = buttons.get(i);
            if (isOpen && gui.mouseClicked(mouseX, mouseY, button)) {
                mod.toggle();
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
            resetDropdownsLocation(getPosition());
            return true;
        }
        return false;
    }

    @Override
    public void resetPosition() {
        super.resetPosition();
        isOpen = false;
        resetDropdownsLocation(defaultPosition.getTopLeft());
    }

    /**
     * Resets the position of all the mods in the dropdown.
     *
     * @param newPoint - The new position of the dropdown.
     */
    private void resetDropdownsLocation(PointD newPoint) {
        for (int i = 0; i < buttons.size(); i++) {
            var gui = buttons.get(i);
            var x = newPoint.x();
            var y = newPoint.y() + (i + 1) * 10 + 4;
            gui.setPosition(new PointD(x, y));
        }
    }
}
