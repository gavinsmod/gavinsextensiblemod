package com.peasenet.gui.mod;

import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for movement mods as a dropdown.
 */
public class GuiMovement extends GuiDropdown {

    /**
     * Creates a new movement dropdown.
     */
    public GuiMovement() {
        this(new PointD(10, 10), 60, 10, Text.translatable("key.gavinsmod.gui.movement"));
    }

    /**
     * Creates a new movement dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiMovement(PointD position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.MOVEMENT);
        setBackground(Colors.BLUE);
    }
}
