package com.peasenet.gui.mod;

import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for combat mods as a dropdown.
 */
public class GuiCombat extends GuiDropdown {

    /**
     * Creates a new combat dropdown.
     */
    public GuiCombat() {
        this(new PointD(80, 10), 50, 10, Text.translatable("key.gavinsmod.gui.combat"));
    }

    /**
     * Creates a new combat dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiCombat(PointD position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.COMBAT);
        setBackground(Colors.DARK_RED);
    }
}
