package com.peasenet.gui.elements;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.Settings;
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
            var gui = new GuiToggle(new PointD(x, y + 2), (int) getWidth(), (int) getHeight(), Text.translatable(mod.getTranslationKey()));
            gui.setCallback(mod::toggle);
            addElement(gui);
        }
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        super.render(matrices, tr);
        var mods = GavinsMod.getModsInCategory(getCategory());
        for (int i = 0; i < mods.size(); i++) {
            var mod = mods.get(i);
            var gui = buttons.get(i);
            if(gui instanceof GuiToggle)
                ((GuiToggle) gui).setState(mod.isActive());
            gui.setBackground(mod.isActive() ? Settings.EnabledColor : Settings.BackgroundColor);
        }
    }
}
