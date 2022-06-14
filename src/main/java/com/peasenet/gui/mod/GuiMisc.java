package com.peasenet.gui.mod;

import com.peasenet.gui.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.Point;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for miscellaneous mods as a dropdown.
 */
public class GuiMisc extends GuiDropdown {

    /**
     * Creates a new misc dropdown.
     */
    public GuiMisc() {
        this(new Point(140, 10), 70, 10, Text.translatable("key.gavinsmod.gui.misc"));
    }

    /**
     * Creates a new misc dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiMisc(Point position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.MISC);
        setBackground(Colors.GRAY);
    }
}
