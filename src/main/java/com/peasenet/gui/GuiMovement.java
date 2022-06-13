package com.peasenet.gui;

import com.peasenet.mods.Type;
import com.peasenet.util.math.Point;
import net.minecraft.text.Text;

public class GuiMovement extends GuiDropdown {

    public GuiMovement() {
        this(new Point(10, 10));
    }

    public GuiMovement(Point position) {
        this(position, 50, 10, Text.translatable("key.gavinsmod.gui.movement"));
    }

    public GuiMovement(Point position, int width, int height, Text title) {
        super(position, width, height, title, Type.Category.MOVEMENT);
    }
}
