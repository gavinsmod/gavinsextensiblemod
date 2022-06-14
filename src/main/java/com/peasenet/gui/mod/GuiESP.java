package com.peasenet.gui.mod;

import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for ESP mods as a drop down.
 */
public class GuiESP extends GuiDropdown {

    /**
     * Creates a new ESP dropdown.
     */
    public GuiESP() {
        this(new PointD(10, 110), 60, 10, Text.translatable("key.gavinsmod.gui.esps"));
    }

    /**
     * Creates a new ESP dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiESP(PointD position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.ESPS);
        setBackground(Colors.PURPLE);
    }
}
