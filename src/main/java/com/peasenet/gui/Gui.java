package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.Point;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Gui {

    /**
     * The X axis increment.
     */
    private static final int X_INCREMENT = 75;
    /**
     * The Y axis increment.
     */
    private static final int Y_INCREMENT = 12;
    // The top left corner of the gui.
    private Point position;
    // The width of the dropdown.
    private int width;
    // The height of the dropdown.
    private int height;
    // The title of the dropdown.
    private Text title;

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
    }

    /**
     * Gets the x coordinate for the top left corner of the dropdown.
     *
     * @return The x coordinate for the top left corner of the dropdown.
     */
    public int getX() {
        return position.x;
    }

    /**
     * Sets the x coordinate for the top left corner of the dropdown.
     *
     * @param x The x coordinate for the top left corner of the dropdown.
     */
    public void setX(int x) {
        this.position.x = x;
    }

    /**
     * Gets the y coordinate for the top left corner of the dropdown.
     *
     * @return The y coordinate for the top left corner of the dropdown.
     */
    public int getY() {
        return position.y;
    }

    /**
     * Sets the y coordinate for the top left corner of the dropdown.
     *
     * @param y The y coordinate for the top left corner of the dropdown.
     */
    public void setY(int y) {
        this.position.y = y;
    }

    /**
     * Gets the x coordinate for the bottom right corner of the dropdown.
     *
     * @return The x coordinate for the bottom right corner of the dropdown.
     */
    public int getX2() {
        return position.x + width;
    }

    /**
     * Gets the y coordinate for the bottom right corner of the dropdown.
     *
     * @return The y coordinate for the bottom right corner of the dropdown.
     */
    public int getY2() {
        return position.y + height;
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
     * Sets the width of the dropdown.
     *
     * @param width The width of the dropdown.
     */
    public void setWidth(int width) {
        this.width = width;
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
     * Sets the height of the dropdown.
     *
     * @param height The height of the dropdown.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the title of the dropdown.
     */
    public Text getTitle() {
        return title;
    }

    /**
     * Sets the title of the dropdown.
     */
    public void setTitle(Text title) {
        this.title = title;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    protected void incrementY() {
        this.position.y += Y_INCREMENT;
    }

    protected void incrementX() {
        this.position.x += X_INCREMENT;
    }

    /**
     * Renders the clickable ui
     *
     * @param matrixStack The matrix stack used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     */
    public void render(MatrixStack matrixStack, TextRenderer tr) {
        // Draw the title.
        drawBox(Colors.RED.getAsFloatArray(), getX(), getY(), getX2(), getY2(), matrixStack);
        tr.draw(matrixStack, title, getX() + 2, getY() + 2, 0xFFFFFF);
        drawOutline(Colors.WHITE.getAsFloatArray(), getX(), getY(), getX2(), getY2(), matrixStack);

    }

    /**
     * Draws all the mods in a category.
     *
     * @param matrixStack The matrix stack used to draw boxes on screen..
     * @param tr          The text render to use to draw text.
     * @param category    The category to draw.
     */
    protected void drawModsInCategory(MatrixStack matrixStack, TextRenderer tr, Type.Category category) {
        int yt1 = getX() + Y_INCREMENT;
        int yt2 = getY() + Y_INCREMENT;
        for (Mod mod : GavinsMod.getModsInCategory(category)) {
            drawBox(mod.isActive() ? Colors.GREEN.getAsFloatArray() : Colors.DARK_GRAY.getAsFloatArray(), getX(), yt1, getX2(), yt2, matrixStack);
            tr.draw(matrixStack, Text.translatable(mod.getTranslationKey()), getX() + 2, getY() + 2, 0xffffff);
            drawOutline(Colors.BLACK.getAsFloatArray(), getX(), yt1, getX2(), yt2, matrixStack);
            yt1 += Y_INCREMENT;
            yt2 += Y_INCREMENT;
        }
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
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP,
                VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        Tessellator.getInstance().draw();
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }
}
