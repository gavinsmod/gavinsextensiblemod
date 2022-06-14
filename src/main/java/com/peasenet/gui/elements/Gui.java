package com.peasenet.gui.elements;

import com.peasenet.util.RenderUtils;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * The base class for all gui elements.
 */
public class Gui {

    /**
     * The original position of the gui.
     */
    protected final BoxD defaultPosition;

    /**
     * The title of the gui.
     */
    private final Text title;

    /**
     * The box that contains the gui.
     */
    private BoxD box;

    /**
     * The background color of the gui.
     */
    private Color backgroundColor;

    /**
     * Whether this gui is currently being dragged.
     */
    private boolean dragging;

    /**
     * Creates a new GUI menu.
     *
     * @param width  - The width of the gui.
     * @param height - The height of the gui.
     * @param title  - The title of the gui.
     */
    public Gui(PointD topLeft, int width, int height, Text title) {
        this.box = new BoxD(topLeft, width, height);
        this.defaultPosition = BoxD.copy(box);
        this.title = title;
        backgroundColor = Colors.BLACK;
    }

    /**
     * Sets the background color to the given color.
     *
     * @param color - The color to set the background to.
     */
    public void setBackground(Color color) {
        backgroundColor = color;
    }

    /**
     * Gets the x coordinate for the top left corner of the dropdown.
     *
     * @return The x coordinate for the top left corner of the dropdown.
     */
    public double getX() {
        return box.getTopLeft().x();
    }

    /**
     * Gets the y coordinate for the top left corner of the dropdown.
     *
     * @return The y coordinate for the top left corner of the dropdown.
     */
    public double getY() {
        return box.getTopLeft().y();
    }

    /**
     * Gets the x coordinate for the bottom right corner of the dropdown.
     *
     * @return The x coordinate for the bottom right corner of the dropdown.
     */
    public double getX2() {
        return box.getBottomRight().x();
    }

    /**
     * Gets the y coordinate for the bottom right corner of the dropdown.
     *
     * @return The y coordinate for the bottom right corner of the dropdown.
     */
    public double getY2() {
        return box.getBottomRight().y();
    }

    /**
     * Gets the width of the dropdown.
     *
     * @return The width of the dropdown.
     */
    public double getWidth() {
        return box.getWidth();
    }

    /**
     * Gets the height of the dropdown.
     *
     * @return The height of the dropdown.
     */
    public double getHeight() {
        return box.getHeight();
    }

    /**
     * Renders the clickable ui
     *
     * @param matrixStack The matrix stack used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     */
    public void render(MatrixStack matrixStack, TextRenderer tr) {
        RenderUtils.drawBox(backgroundColor.getAsFloatArray(), (int) getX(), (int) getY(), (int) getX2(), (int) getY2(), matrixStack);
        tr.draw(matrixStack, title, (int) getX() + 2, (int) getY() + 2, 0xFFFFFF);
        RenderUtils.drawOutline(Colors.WHITE.getAsFloatArray(), (int) getX(), (int) getY(), (int) getX2(), (int) getY2(), matrixStack);
    }


    /**
     * Checks whether the mouse is clicked.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was clicked.
     * @return Whether the mouse was clicked.
     */
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Checks whether the mouse was dragged.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param button - The button that was dragged.
     * @param deltaX - The change in x coordinate.
     * @param deltaY - The change in y coordinate.
     * @return Whether the mouse was dragged.
     */
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    /**
     * Whether the current window is being dragged.
     *
     * @return True if the current window is being dragged.
     */
    public boolean isDragging() {
        return dragging;
    }

    /**
     * Sets whether the current window is being dragged.
     *
     * @param dragging - Whether the current window is being dragged.
     */
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Resets the position to the default position.
     */
    public void resetPosition() {
        this.box = BoxD.copy(defaultPosition);
    }

    /**
     * Gets the current location of the top left corner of the gui.
     *
     * @return The current location of the top left corner of the gui.
     */
    public PointD getPosition() {
        return box.getTopLeft();
    }

    /**
     * Sets the top left corner of the gui element to the given point.
     *
     * @param position - The point to set the top left corner of the gui element to.
     */
    public void setPosition(PointD position) {
        this.box.setTopLeft(position);
    }
}
