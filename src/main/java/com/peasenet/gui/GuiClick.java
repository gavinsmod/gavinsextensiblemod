package com.peasenet.gui;

import com.peasenet.util.math.Point;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 5/24/2022
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
    public GuiClick(Point position, int width, int height, Text title) {
        super(position, width, height, title);
    }

    /**
     * Renders the clickable ui
     *
     * @param matrixStack The matrix stack used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     * @param title       The title of the ui.
     */
//    public void render(MatrixStack matrixStack, TextRenderer tr, Text title) {

    // Draw title

//        drawBox(Colors.BLACK.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
//        tr.draw(matrixStack, title, xt1 + 2, yt1 + 2, 0xFFFFFF);
//        drawOutline(Colors.WHITE.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);


//        // Draw each of the mod categories. There should be 4 categories to a row.
//        for (Type.Category category : Type.Category.values()) {
//            // skip the GUI category
//            if (category == Type.Category.GUI) continue;
//
//            // Draw the category name
//            drawBox(Colors.DARK_RED.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
//            tr.draw(matrixStack, Text.translatable(category.getTranslationKey()), xt1 + 2, yt1 + 2, 0xFFFFFF);
//            drawOutline(Colors.BLACK.getAsFloatArray(), xt1, yt1, xt2, yt2, matrixStack);
//
//            // Draw the checkboxes for each mod in the category
//            drawModsInCategory(matrixStack, tr, xt1, yt1, xt2, yt2, category);
//            xt1 += X_INCREMENT + 5;
//            xt2 += X_INCREMENT + 5;
//        }
//    }


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
