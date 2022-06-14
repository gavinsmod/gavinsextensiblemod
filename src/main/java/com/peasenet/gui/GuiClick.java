package com.peasenet.gui;

import com.peasenet.util.math.PointD;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * Creates a GUI that allows the user to toggle mods on and off by clicking.
 */
public class GuiClick extends Gui {

    /**
     * Creates a new GUI menu.
     *
     * @param width  - The width of the gui.
     * @param height - The height of the gui.
     * @param title  - The title of the gui.
     */
    public GuiClick(PointD position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    /**
     * Handles clicks on the gui.
     *
     * @param mouseX The x coordinate of the mouse.
     * @param mouseY The y coordinate of the mouse.
     * @param button The mouse button that was clicked.
     */
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        // check if mouseX and mouseY are within the bounds of the gui.
        return (mouseX >= getX() && mouseX <= getX2() && mouseY >= getY() && mouseY <= getY2());
    }
}
