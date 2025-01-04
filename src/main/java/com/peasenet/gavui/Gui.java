/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.gavui;

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GuiUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author gt3ch1
 * @version 2/28/2023
 * The base class for all gui elements.
 */
public class Gui {

    /**
     * The gui that was clicked.
     */
    protected static Gui clickedGui;
    /**
     * The original position of the gui.
     */
    protected final BoxF defaultPosition;
    /**
     * A randomly generated UUID for this gui.
     */
    private final UUID uuid = UUID.randomUUID();
    /**
     * The list of buttons(mods) in this dropdown.
     */
    protected ArrayList<Gui> children = new ArrayList<>();
    /**
     * The title of the gui.
     */
    protected Text title;
    protected String translationKey;
    /**
     * The symbol to be drawn to the left of the end of the box (like a checkbox, empty box, or arrow).
     */
    char symbol;
    /**
     * The offset used for the symbol (x).
     */
    int symbolOffsetX = -10;
    /**
     * The offset used for the symbol (y).
     */
    int symbolOffsetY = 1;
    private float transparency;
    private String settingKey;
    /**
     * Whether this element is a parent.
     */
    private boolean isParent = false;
    /**
     * The box that contains the gui.
     */
    private BoxF box;
    /**
     * The background color of the gui.
     */
    private Color backgroundColor = Colors.INDIGO;
    /**
     * Whether this gui is currently being dragged.
     */
    private boolean dragging;
    /**
     * Whether this gui is currently hidden.
     */
    private boolean hidden;
    /**
     * Whether this gui has been shrunk to fit a scrollbar.
     */
    private boolean shrunkForScroll = false;
    /**
     * Whether this gui is currently being hovered over.
     */
    private boolean hoverable = true;

    /**
     * Whether this GUI has a border to be drawn.
     */
    private boolean drawBorder = true;

    /**
     * Creates a new GUI menu.
     *
     * @param topLeft - The top left corner of the gui.
     * @param width   - The width of the gui.
     * @param height  - The height of the gui.
     * @param title   - The title of the gui.
     */
    public Gui(PointF topLeft, int width, int height, Text title) {
        box = new BoxF(topLeft, width, height);
        defaultPosition = BoxF.copy(box);
        this.title = title;
        dragging = false;
    }

    public Gui(GuiBuilder builder) {
        this.title = builder.getTitle();
        var w = builder.getWidth();
        if (title != null && MinecraftClient.getInstance().textRenderer != null)
            w = Math.max(w, MinecraftClient.getInstance().textRenderer.getWidth(title));

        box = new BoxF(builder.getTopLeft(), w, builder.getHeight());
        defaultPosition = BoxF.copy(box);
        dragging = false;
        this.translationKey = builder.getTranslationKey();
        setPosition(builder.getTopLeft());
        setBackground(builder.getBackgroundColor());
        setHoverable(builder.isHoverable());
        setSymbol(builder.getSymbol());
        setHidden(builder.isHidden());
        setHoverable(builder.isHoverable());
        setTransparency(builder.getTransparency());
        setDrawBorder(builder.getDrawBorder());
    }

    public static Gui getClickedGui() {
        return clickedGui;
    }

    public boolean getDrawBorder() {
        return drawBorder;
    }

    private void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public float getTransparency() {
        if (this.transparency == -1) {
            return GavUI.getAlpha();
        }
        return transparency;
    }

    /**
     * Sets the transparency of the gui. If set to -1, it will use the transparency of GavUI (set in the config).
     *
     * @param transparency - The transparency of the gui.
     */
    public void setTransparency(float transparency) {
        this.transparency = transparency;
        if (transparency == -1) {
            return;
        }
        if (transparency < 0) {
            this.transparency = 0f;
        }
        if (transparency > 1f) {
            this.transparency = 1f;
        }

    }

    public Text getTitle() {
        return title;
    }

    /**
     * Sets the title of the gui.
     *
     * @param title - The title of the gui.
     */
    public void setTitle(Text title) {
        this.title = title;
    }

    /**
     * Whether this gui should be brightened when hovered.
     *
     * @return Whether this gui should be brightened when hovered.
     */
    public boolean isHoverable() {
        return hoverable;
    }

    /**
     * Whether this gui should be brightened when hovered.
     *
     * @param hoverable Whether this gui should be brightened when hovered.
     */
    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
    }

    /**
     * Whether this element is a parent element.
     *
     * @return True if this element is a parent.
     */
    public boolean isParent() {
        return isParent;
    }

    /**
     * Sets whether this element is a parent.
     *
     * @param parent - True if we want this element to be a parent.
     */
    public void setParent(boolean parent) {
        isParent = parent;
    }

    /**
     * Sets the symbol that should be drawn on the right side of the gui.
     *
     * @param symbol - The symbol to draw.
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Clears all children from this gui.
     */
    public void clearChildren() {
        children = new ArrayList<>();
    }

    /**
     * Gets the children of this gui.
     *
     * @return The children of this gui.
     */
    public ArrayList<Gui> getChildren() {
        return children;
    }

    /**
     * Adds an child element to this gui.
     *
     * @param child - The child element to add.
     */
    public void addElement(Gui child) {
        if (children.isEmpty()) {
            child.setPosition(new PointF(getX2() + 100, getY2() + 1));
            children.add(child);
            return;
        }
        // get last gui
        Gui lastButton = children.get(children.size() - 1);
        var lastY = lastButton.getY2();
        // set new gui position
        child.setPosition(new PointF(getX(), lastY + 2));
        children.add(child);
    }

    /**
     * Gets whether this gui is currently hidden.
     *
     * @return Whether this gui is currently hidden.
     */
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        if (hidden) {
            if (this.hasChildren()) {
                children.forEach(Gui::hide);
            }
        } else {
            if (this.hasChildren()) {
                children.forEach(Gui::show);
            }
        }
        this.hidden = hidden;
    }

    /**
     * Hides this gui.
     */
    public void hide() {
        setHidden(true);
    }

    /**
     * Shows this gui.
     */
    public void show() {
        setHidden(false);
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
    public float getX() {
        return box.getX1();
    }

    /**
     * Gets the y coordinate for the top left corner of the dropdown.
     *
     * @return The y coordinate for the top left corner of the dropdown.
     */
    public float getY() {
        return box.getY1();
    }

    /**
     * Gets the x coordinate for the bottom right corner of the dropdown.
     *
     * @return The x coordinate for the bottom right corner of the dropdown.
     */
    public float getX2() {
        return box.getX2();
    }

    /**
     * Gets the y coordinate for the bottom right corner of the dropdown.
     *
     * @return The y coordinate for the bottom right corner of the dropdown.
     */
    public float getY2() {
        return box.getY2();
    }

    /**
     * Gets the width of the dropdown.
     *
     * @return The width of the dropdown.
     */
    public float getWidth() {
        return box.getWidth();
    }

    /**
     * Sets the width of the gui.
     *
     * @param width - The width of the gui.
     */
    public void setWidth(float width) {
        box = new BoxF(box.getTopLeft(), width, box.getHeight());
    }

    /**
     * Shrinks the gui to fit a scrollbar.
     *
     * @param parent - The parent gui.
     */
    public void shrinkForScrollbar(Gui parent) {
        if (shrunkForScroll && this.getWidth() == parent.getWidth()) return;
        if (this.getWidth() == parent.getWidth()) this.setWidth(getWidth() - 6);
        shrunkForScroll = true;
    }

    /**
     * Gets the height of the dropdown.
     *
     * @return The height of the dropdown.
     */
    public float getHeight() {
        return box.getHeight();
    }

    /**
     * Renders the clickable ui
     *
     * @param drawContext The draw context used to draw boxes on screen.
     * @param tr          The text render to use to draw text
     * @param mouseX      The x coordinate of the mouse.
     * @param mouseY      The y coordinate of the mouse.
     * @param delta       The change in time since the last render.
     */
    public void render(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        if (isHidden()) return;
        var matrixStack = drawContext.getMatrices();
        var bg = backgroundColor;
        if (bg == null)
            bg = GavUI.backgroundColor();
        if (mouseWithinGui(mouseX, mouseY) && hoverable)
            bg = bg.brighten(0.25f);
        GuiUtil.drawBox(bg, getBox(), matrixStack, getTransparency());
        var textColor = GavUI.textColor();
        if (title != null) {
            if (textColor.similarity(bg) < 0.3f) {
                textColor = textColor.invert();
                if (textColor.similarity(bg) < 0.3f)
                    textColor = Colors.WHITE;
            }
            drawText(drawContext, tr, title, getX() + 2, getY() + 1.5f, textColor);
        }
        drawSymbol(drawContext, tr, textColor);
        if (this.drawBorder)
            GuiUtil.drawOutline(GavUI.borderColor(), box, matrixStack);
        renderChildren(drawContext, tr, mouseX, mouseY, delta);
    }

    /**
     * Draws the GUI symbol.
     *
     * @param drawContext - The draw context to use.
     * @param tr          - The text renderer to use.
     */
    private void drawSymbol(DrawContext drawContext, TextRenderer tr, Color color) {
        if (symbol != '\0')
            drawText(drawContext, tr, Text.of(String.valueOf(symbol)), getX2() - 9f, getY() + 1.5f, color, false);
    }

    /**
     * Renders the children of this gui.
     *
     * @param drawContext - The draw context to use to draw the children.
     * @param tr          - The text renderer to use to draw the children.
     * @param mouseX      - The x coordinate of the mouse.
     * @param mouseY      - The y coordinate of the mouse.
     * @param delta       - The change in time since the last render.
     */
    private void renderChildren(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        if (!hasChildren()) return;
        for (Gui c : children)
            c.render(drawContext, tr, mouseX, mouseY, delta);
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
     * Checks whether the mouse was scrolled
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @param amount - The amount of scroll.
     * @return Whether the mouse was scrolled.
     */
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    /**
     * Whether the current window is being dragged.`
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
        for (Gui child : children)
            child.setDragging(dragging);
        clickedGui = null;
    }

    public BoxF getBox() {
        return box;
    }

    /**
     * Resets the position to the default position.
     */
    public void resetPosition() {
        box = BoxF.copy(defaultPosition);
    }

    /**
     * Gets the current location of the top left corner of the gui.
     *
     * @return The current location of the top left corner of the gui.
     */
    public PointF getPosition() {
        return box.getTopLeft();
    }

    /**
     * Sets the top left corner of the gui element to the given point.
     *
     * @param position - The point to set the top left corner of the gui element to.
     */
    public void setPosition(PointF position) {
        box.setTopLeft(position);
    }

    /**
     * Sets the middle of the gui element to the given point.
     *
     * @param position - The point to set the middle of the gui element to.
     */
    public void setMidPoint(PointF position) {
        box.setMiddle(position);
    }

    /**
     * Gets whether the mouse coordinates are within the bounds of the gui.
     *
     * @param mouseX - The x coordinate of the mouse.
     * @param mouseY - The y coordinate of the mouse.
     * @return Whether the mouse coordinates are within the bounds of the gui.
     */
    public boolean mouseWithinGui(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX <= getX2() && mouseY >= getY() && mouseY <= getY2() && !isHidden();
    }

    /**
     * Gets the background color of the gui.
     *
     * @return The background color of the gui.
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Whether this element has children.
     *
     * @return True if this element has children.
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void setShrunkForScrollbar(boolean b) {
        shrunkForScroll = b;
        if (hasChildren()) for (Gui c : children)
            c.setShrunkForScrollbar(b);
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Gui g) {
            return g.getUUID().equals(uuid);
        }
        return false;
    }


    /**
     * Sets the default position of the gui.
     *
     * @param newDefaultPosition - The new default position of the gui.
     */
    public void setDefaultPosition(BoxF newDefaultPosition) {
        defaultPosition.from(newDefaultPosition);
    }

    //NOTE: I wrote this so that I don't have to deal with the API in drawContext and I can just use the textRenderer directly.
    protected void drawText(DrawContext drawContext, TextRenderer textRenderer, Text text, float x, float y, Color color, boolean shadow) {
        drawContext.drawText(textRenderer, text.asOrderedText(), (int)x, (int)y, color.getAsInt(), shadow);
    }

    protected void drawText(DrawContext drawContext, TextRenderer textRenderer, String text, float x, float y, Color color) {
        drawText(drawContext, textRenderer, Text.of(text), x, y, color, false);
    }

    protected void drawText(DrawContext drawContext, TextRenderer textRenderer, Text text, float x, float y, Color color) {
        drawText(drawContext, textRenderer, text, x, y, color, false);
    }

    public String getTranslationKey() {
        return this.translationKey;
    }
}
