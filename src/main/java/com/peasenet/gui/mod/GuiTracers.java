package com.peasenet.gui.mod;

import com.peasenet.gui.GuiDropdown;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a new gui for tracer mods as a dropdown.
 */
public class GuiTracers extends GuiDropdown {

    /**
     * Creates a new tracer dropdown.
     */
    public GuiTracers() {
        this(new PointD(80, 110), 82, 10, Text.translatable("key.gavinsmod.gui.tracers"));
    }

    /**
     * Creates a new tracer dropdown.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiTracers(PointD position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.TRACERS);
        setBackground(Colors.CYAN);
    }
}
