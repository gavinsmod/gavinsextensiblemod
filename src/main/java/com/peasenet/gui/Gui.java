package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.Point;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * The base class for all gui elements.
 */
public class Gui {

    /**
     * The top left corner of the gui.
     */
    private Point position;

    /**
     * The width of the gui.
     */
    private final int width;

    /**
     * The height of the gui.
     */
    private final int height;

    /**
     * The title of the gui.
     */
    private final Text title;

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
    public Gui(Point topLeft, int width, int height, Text title) {
        this.position = topLeft;
        this.width = width;
        this.height = height;
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
    public int getX() {
        return position.x();
    }

    /**
     * Gets the y coordinate for the top left corner of the dropdown.
     *
     * @return The y coordinate for the top left corner of the dropdown.
     */
    public int getY() {
        return position.y();
    }

    /**
     * Gets the x coordinate for the bottom right corner of the dropdown.
     *
     * @return The x coordinate for the bottom right corner of the dropdown.
     */
    public int getX2() {
        return position.x() + width;
    }

    /**
     * Gets the y coordinate for the bottom right corner of the dropdown.
     *
     * @return The y coordinate for the bottom right corner of the dropdown.
     */
    public int getY2() {
        return position.y() + height;
    }

    /**
     * Gets the width of the dropdown.
     *
     * @return The width of the dropdown.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the dropdown.
     *
     * @return The height of the dropdown.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the top left corner of the gui element to the given point.
     *
     * @param position - The point to set the top left corner of the gui element to.
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Renders the clickable ui
     *
     * @param matrixStack The matrix stack used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     */
    public void render(MatrixStack matrixStack, TextRenderer tr) {
        drawBox(backgroundColor.getAsFloatArray(), getX(), getY(), getX2(), getY2(), matrixStack);
        tr.draw(matrixStack, title, getX() + 2, getY() + 2, 0xFFFFFF);
        drawOutline(Colors.WHITE.getAsFloatArray(), getX(), getY(), getX2(), getY2(), matrixStack);
    }

    /**
     * Draws a box on screen.
     *
     * @param acColor     The color of the box as a 4 point float array.
     * @param xt1         The x coordinate of the top left corner of the box.
     * @param yt1         The y coordinate of the top left corner of the box.
     * @param xt2         The x coordinate of the bottom right corner of the box.
     * @param yt2         The y coordinate of the bottom right corner of the box.
     * @param matrixStack The matrix stack used to draw boxes on screen.
     */
    protected void drawBox(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 0.5f);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
        Tessellator.getInstance().draw();
    }

    /**
     * Draws a box outline on screen.
     *
     * @param acColor     The color of the box as a 4 point float array.
     * @param xt1         The x coordinate of the top left corner of the box.
     * @param yt1         The y coordinate of the top left corner of the box.
     * @param xt2         The x coordinate of the bottom right corner of the box.
     * @param yt2         The y coordinate of the bottom right corner of the box.
     * @param matrixStack The matrix stack used to draw boxes on screen.
     */
    protected void drawOutline(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 1.0F);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        Tessellator.getInstance().draw();
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
}
