package com.peasenet.gui.elements;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiModCategory extends GuiDropdown {
    public GuiModCategory(PointD position, int width, int height, Text title, Type.Category category) {
        super(position, width, height, title);
        setCategory(category);
    }

    /**
     * Toggles the mod that the mouse is currently hovering over, if applicable.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @return Whether the mouse was clicked on a mod.
     */
    private boolean toggleSelectedMod(double mouseX, double mouseY) {
        if (!isOpen()) return false;
        var mods = GavinsMod.getModsInCategory(getCategory());
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var gui = buttons.get(i);
            if (gui.mouseWithinGui(mouseX, mouseY)) {
                mod.toggle();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            if (button == 0)
                return toggleSelectedMod(mouseX, mouseY);
            else
                //TODO: implement options screen on right click.
                return false;
        }
        return false;
    }

    /**
     * Sets the category of the dropdown
     */
    public void setCategory(Type.Category category) {
        this.category = category;
        var mods = GavinsMod.getModsInCategory(category);
        if (mods == null)
            return;
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var x = getX();
            var y = getY2() + i * 10;
            addElement(new GuiToggle(new PointD(x, y + 2), (int) getWidth(), (int) getHeight(), Text.translatable(mod.getTranslationKey())));
        }
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        super.render(matrices, tr);
        var mods = GavinsMod.getModsInCategory(getCategory());
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var gui = buttons.get(i);
            gui.setBackground(mod.isActive() ? Colors.GREEN : Colors.BLACK);
        }
    }
}
