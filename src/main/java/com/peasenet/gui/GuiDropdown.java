package com.peasenet.gui;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.Point;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiDropdown extends GuiDraggable {


    // Whether the dropdown is open.
    private boolean isOpen;

    private Type.Category category;

    public GuiDropdown(Point position, int width, int height, Text title, Type.Category category) {
        super(position, width, height, title);
        this.category = category;
    }

    /**
     * Gets whether the dropdown is open.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Opens the dropdown.
     */
    public void open() {
        isOpen = true;
    }

    /**
     * Closes the dropdown.
     */
    public void close() {
        isOpen = false;
    }

    /**
     * Gets the category of the dropdown
     *
     * @return The category of the dropdown
     */
    public Type.Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the dropdown
     */
    public void setCategory(Type.Category category) {
        this.category = category;
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        super.render(matrices, tr);
        // For each mod in the category, render it right below the title.
        if (!isOpen())
            return;
        var mods = GavinsMod.getModsInCategory(category);
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var text = mod.getName();
            var x = getX();
            var y = getY2() + i * 10;
            var width = getWidth();
            var height = getHeight();
            var color = mod.isActive() ? 0xFFFFFF : 0xFF0000;
            var backgroundColor = mod.isActive() ? Colors.GREEN.getAsFloatArray() : Colors.BLACK.getAsFloatArray();
            drawBox(backgroundColor, x, y, x + width, y + height, matrices);
            tr.draw(matrices, text, x + 2, y + 2, color);
            drawOutline(Colors.WHITE.getAsFloatArray(), x, y, x + width, y + height, matrices);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        // Check if the mouse is within the bounds of the dropdown.
        if (mouseX >= getX() && mouseX <= getX2() && mouseY >= getY() && mouseY <= getY2()) {
            // If the dropdown is open, close it.
            if (isOpen) {
                close();
            } else {
                // Otherwise, open it.
                open();
            }
            return true;
        }
        // Check if the mouse is within the bounds of any of the mods.
        var mods = GavinsMod.getModsInCategory(category);
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var x = getX();
            var y = getY2() + i * 10;
            var width = getWidth();
            var height = getHeight();
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                mod.toggle();
                return true;
            }
        }
        return false;
    }
}
