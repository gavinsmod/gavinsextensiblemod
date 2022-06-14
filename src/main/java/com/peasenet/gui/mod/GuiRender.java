package com.peasenet.gui.mod;

import com.peasenet.gui.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for render mods as a dropdown.
 */
public class GuiRender extends GuiDropdown {

    /**
     * Creates a new render dropdown.
     */
    public GuiRender() {
        this(new PointD(220, 10), 62, 10, Text.translatable("key.gavinsmod.gui.render"));
    }

    /**
     * Creates a new render dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiRender(PointD position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.RENDER);
        setBackground(Colors.DARK_GREEN);
    }
}
