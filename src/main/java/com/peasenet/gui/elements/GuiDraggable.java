package com.peasenet.gui.elements;

import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * A draggable ui element.
 */
public class GuiDraggable extends GuiClick {

    /**
     * Creates a new GUI draggable object.
     *
     * @param position - The position of the draggable object.
     * @param width    - The width of the gui.
     * @param height   - The height of the gui.
     * @param title    - The title of the gui.
     */
    public GuiDraggable(PointD position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        setPosition(new PointD(mouseX - (getWidth() / 2), mouseY - (getHeight() / 2)));
        return true;
    }
}
