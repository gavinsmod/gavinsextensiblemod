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
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * A gui element that allows the user to drag, drop, and scroll through a list of items.
 * By default, this element will show four children at once. There are (totalChildren/toBeShown) pages of children.
 */
public class GuiScroll extends GuiDropdown {

    /**
     * The maximum number of children that can be displayed at once.
     */
    private int maxChildren;
    private int defaultMaxChildren = 4;

    /**
     * The total number of pages that can be drawn.
     */
    private int numPages;

    /**
     * The current page.
     */
    private int page;

    /**
     * Creates a new scroll like UI element.
     *
     * @param position    - The position of the element.
     * @param width       - The width of the element.
     * @param height      - The height of the element.
     * @param title       - The title of the element.
     * @param maxChildren - The maximum number of children that can be displayed.
     * @param children    - The children of the element.
     */
    public GuiScroll(PointF position, int width, int height, Text title, int maxChildren, ArrayList<Gui> children) {
        super(position, width, height, title);
        for (Gui gui : children) {
            gui.setWidth(getWidth());
            this.children.add(gui);
            if (getDirection() == Direction.RIGHT)
                gui.setPosition(new PointF(getX2() + 14, getY2() + (this.children.size()) * 12));
        }
        this.defaultMaxChildren = maxChildren;
        this.maxChildren = maxChildren;
        this.maxChildren = Math.min(children.size(), this.maxChildren);
        numPages = (int) Math.ceil((double) children.size() / (double) maxChildren);
        page = 0;
    }


    /**
     * Creates a new scroll like UI element.
     *
     * @param position - The position of the element.
     * @param width    - The width of the element.
     * @param height   - The height of the element.
     * @param title    - The title of the element.
     */
    public GuiScroll(PointF position, int width, int height, Text title) {
        super(position, width, height, title);
        maxChildren = 4;
        numPages = (int) Math.ceil((double) children.size() / (double) maxChildren);
        page = 0;
    }

    /**
     * Creates a new scroll like UI element, at the default point of (0,0).
     *
     * @param width  - The width.
     * @param height - The height.
     * @param title  - The title.
     */
    public GuiScroll(int width, int height, Text title) {
        this(new PointF(0, 0), width, height, title);
    }

    public GuiScroll(GuiBuilder builder) {
        super(builder);
        for (Gui gui : builder.getChildren()) {
            gui.setWidth(getWidth());
            this.children.add(gui);
            if (getDirection() == Direction.RIGHT)
                gui.setPosition(new PointF(getX2() + 14, getY2() + (this.children.size()) * 12));
        }
        this.setDirection(builder.getDirection());
        this.defaultMaxChildren = builder.getDefaultMaxChildren();
        this.maxChildren = builder.getMaxChildren();
        this.maxChildren = Math.min(children.size(), this.maxChildren);
        numPages = (int) Math.ceil((double) children.size() / (double) maxChildren);
        page = 0;
    }

    @Override
    public void render(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta) {
        if (isHidden()) return;
        var bg = getBackgroundColor();

        if (isParent())
            bg = GavUI.parentColor();
        else
            bg = GavUI.backgroundColor();
        var childHasMouse = children.stream().anyMatch(gui -> gui.mouseWithinGui(mouseX, mouseY));
        if (mouseWithinGui(mouseX, mouseY) && !childHasMouse) {
            bg = bg.brighten(0.5f);
        }
        GuiUtil.drawBox(bg, getBox(), drawContext.getMatrices(), GavUI.getAlpha());
        var textColor = frozen() ? GavUI.frozenColor() : GavUI.textColor();
        //TODO: Color similarity
        if (title != null) {
            if (textColor.similarity(bg) < 0.2f) {
                textColor = textColor.invert();
                if (textColor.similarity(bg) < 0.2f)
                    textColor = Colors.WHITE;
            }
        }
        drawText(drawContext, tr, title, getX() + 2, getY() + 1.5f, textColor);
        renderSymbol(drawContext, tr, textColor);
        if (getDrawBorder())
            GuiUtil.drawOutline(GavUI.borderColor(), getBox(), drawContext.getMatrices());
        if (!isOpen()) return;
        resetChildPos();

        if (page < 0) page = 0;
        if (page >= numPages) page = numPages - 1;

        for (int i = 0; i < children.size(); i++)
            renderChildren(drawContext, tr, mouseX, mouseY, delta, i);
    }

    private void renderSymbol(DrawContext drawContext, TextRenderer tr, Color textColor) {
        updateSymbol();

        var s = String.valueOf(symbol);
        var x = getX2() + symbolOffsetX;
        var y = getY() + symbolOffsetY;

        switch (getDirection()) {
            case DOWN -> drawText(drawContext, tr, s, x, (y - 1.0f), textColor);
            case RIGHT -> drawText(drawContext, tr, s, x, (getY() + 1.5f), textColor);
            default -> {
            }
        }
    }

    private void renderChildren(DrawContext drawContext, TextRenderer tr, int mouseX, int mouseY, float delta, int i) {
        var child = children.get(i);
        var matrixStack = drawContext.getMatrices();
        if (i < page * maxChildren || i >= (page + 1) * maxChildren) {
            child.hide();
        } else {
            child.show();
        }
        if (shouldDrawScrollBar()) {
            child.shrinkForScrollbar(this);
            drawScrollBox(matrixStack);
            drawScrollBar(matrixStack);
        }
        // NOTE: Why was this ever here? This doesn't make sense. This is overwriting the background color of the children.
//        if (!child.isParent() && !(child instanceof GuiCycle)) {
//            child.setBackground(GavUI.backgroundColor());
//        }
        child.render(drawContext, tr, mouseX, mouseY, delta);
    }


    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        if (hasChildren() && isOpen()) {
            for (var gui : children) {
                if (!gui.isHidden() && gui.mouseWithinGui(x, y) && gui instanceof GuiScroll gs) {
                    if (gs.isOpen()) gui.mouseScrolled(x, y, scroll);
                    else doScroll(scroll);
                    return true;
                }
            }
        }
        if (mouseWithinGui(x, y)) doScroll(scroll);
        return super.mouseScrolled(x, y, scroll);
    }

    /**
     * Scrolls the list either up or down based off the given scroll value. A positive value will scroll up,
     * where a negative value will scroll down.
     *
     * @param scroll - The scroll value.
     */
    private void doScroll(double scroll) {
        if (!isOpen()) return;
        if (scroll > 0) scrollUp();
        else scrollDown();
        resetChildPos();
    }

    /**
     * Resets all the children's positions.
     */
    private void resetChildPos() {
        var modIndex = 0;
        for (int i = page * maxChildren; i < page * maxChildren + maxChildren; i++) {
            if (i >= children.size()) break;
            var gui = children.get(i);
            if (gui instanceof GuiDraggable) ((GuiScroll) gui).resetChildPos();
            switch (getDirection()) {
                case DOWN -> gui.setPosition(new PointF(getX(), getY2() + 2 + (modIndex * 12)));
                case RIGHT -> gui.setPosition(new PointF(getX2() + 7, getY() + (modIndex * 12)));
            }
            modIndex++;
        }
    }

    /**
     * Scrolls the page "up" by one.
     */
    protected void scrollUp() {
        if (page > 0) page--;
    }

    /**
     * Scrolls the page "down" by one.
     */
    protected void scrollDown() {
        if (page < numPages - 1) page++;
    }

    /**
     * Whether the scrollbar should be drawn.
     *
     * @return Whether the scrollbar should be drawn.
     */
    public boolean shouldDrawScrollBar() {
        return children.size() > this.maxChildren;
    }

    /**
     * Draws the box that contains the scrollbar.
     *
     * @param matrixStack - The matrix stack.
     */
    private void drawScrollBox(MatrixStack matrixStack) {
        float scrollBoxX = getX2() - 5f;
        float scrollBoxY = (getY2()) + 2f;
        float scrollBoxHeight = getScrollBoxHeight();
        if (getDirection() == Direction.RIGHT) {
            scrollBoxX = children.get(page * maxChildren).getX2() + 1;
            scrollBoxY = getY();
        }
        var box = new BoxF(new PointF(scrollBoxX, scrollBoxY), 5, scrollBoxHeight);
        GuiUtil.drawBox(GavUI.backgroundColor(), box, matrixStack, GavUI.getAlpha());
        GuiUtil.drawOutline(GavUI.borderColor(), box, matrixStack);
    }

    /**
     * Draws the scrollbar.
     *
     * @param matrixStack - The matrix stack.
     */
    private void drawScrollBar(MatrixStack matrixStack) {
        float scrollBoxHeight = getScrollBoxHeight();
        float scrollBarY = (scrollBoxHeight * (page / (float) numPages)) + getY2() + 3;
        float scrollBarX = getX2() - 4;
        float scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)));
        if (getDirection() == Direction.RIGHT) {
            // set scrollbarY to (1/page) * scrollBoxHeight
            scrollBarY = (scrollBoxHeight * (page / (float) numPages)) + getY() + 1;
            scrollBarX = children.get(page * maxChildren).getX2() + 2;
            scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)));
        }
        var box = new BoxF(new PointF(scrollBarX, scrollBarY), 3, scrollBarY2 - scrollBarY - 2f);
        GuiUtil.drawBox(Colors.WHITE, box, matrixStack);
    }

    /**
     * Gets the height of the scrollbox.
     * a    *
     *
     * @return The height of the scrollbox.
     */
    private float getScrollBoxHeight() {
        return (maxChildren - 1) * 12 + 10;
    }

    @Override
    public void addElement(Gui gui) {
        gui.setWidth(getWidth());
        children.add(gui);
        if (getDirection() == Direction.RIGHT)
            gui.setPosition(new PointF(getX2() + 14, getY2() + (children.size()) * 12));

        maxChildren = Math.min(children.size(), defaultMaxChildren);
        numPages = (int) Math.ceil((double) children.size() / (double) maxChildren);
        if (shouldDrawScrollBar())
            children.forEach(c -> c.shrinkForScrollbar(this));

    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (isHidden()) return false;
        if (mouseWithinGui(x, y)) {
            if (clickedOnChild(x, y, button)) return true;
            if (button == 1 && isParent()) {
                setFrozen(!frozen());
                return true;
            }
            toggleMenu();
            return true;
        }
        return clickedOnScrollBar(x, y);
    }

    boolean clickedOnScrollBar(double x, double y) {
        for (Gui child : children)
            if (child instanceof GuiScroll c && c.clickedOnScrollBar(x, y)) return true;
        return (shouldDrawScrollBar() && clickedOnScrollBox(x, y));
    }

    private boolean clickedOnScrollBox(double x, double y) {
        var scrollBoxX = getX() + getWidth() - 5;
        var scrollBoxY = (getY2()) + 2;
        var scrollBoxHeight = getScrollBoxHeight();
        if (getDirection() == Direction.RIGHT) {
            scrollBoxX = children.get(page * maxChildren).getX2() + 0;
            scrollBoxY = getY();
        }
        if (x >= scrollBoxX && x <= scrollBoxX + 5 && y >= scrollBoxY && y <= scrollBoxY + scrollBoxHeight) {
            var scrollBarY = (scrollBoxHeight * (page / (double) numPages)) + getY2() + 3;
            var scrollBarX = children.get(page * maxChildren).getX2() + 1;
            var scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)));
            if (getDirection() == Direction.RIGHT) {
                // set scrollbarY to (1/page) * scrollBoxHeight
                scrollBarY = (scrollBoxHeight * (page / (double) numPages)) + getY() + 1;
                scrollBarX = children.get(page * maxChildren).getX2() + 1;
                scrollBarY2 = ((scrollBarY) + (scrollBoxHeight / (numPages)));
            }
            if (x >= scrollBarX && x <= scrollBarX + 3 && y >= scrollBarY && y <= scrollBarY2) {
                // clicked on the scrollbar
                return true;
            }
            if (y < scrollBarY - 5) {
                // clicked above the scrollbar
                scrollUp();
                resetChildPos();
                return true;
            }
            if (y > scrollBarY2 - 5) {
                // clicked below the scrollbar
                scrollDown();
                resetChildPos();
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the given mouse coordinates are within the bounds of the child elements. This will return true if
     * a single child is clicked on.
     *
     * @param x      - The mouse x coordinate.
     * @param y      - The mouse y coordinate.
     * @param button - The mouse button.
     * @return True if a child is clicked on, false otherwise.
     */
    private boolean clickedOnChild(double x, double y, int button) {
        for (int i = page * maxChildren; i < page * maxChildren + maxChildren; i++) {
            if (i >= children.size()) break;
            var gui = children.get(i);
            if (gui.isHidden()) return false;
            if (gui.mouseClicked(x, y, button)) {
                for (Gui child : children) {
                    if (child instanceof GuiDropdown dropdown && !child.equals(gui) && dropdown.isOpen()) {
                        dropdown.toggleMenu();
                    }
                    if (child instanceof GuiScroll scroll) {
                        scroll.clickedOnScrollBar(x, y);
                    }
                }
                gui.show();
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!isOpen() && !isParent()) return false;
        for (Gui child : children) {
            if (child.isHidden()) continue;
            if (child.equals(Gui.getClickedGui()) && Gui.getClickedGui().mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            } else if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
        }
        if (frozen() || !isParent()) return false;
        // get if the mouse is within the title bar
        if (mouseX >= getX() && mouseX <= getX2() && mouseY >= getY() && mouseY <= getY() + 12 || isDragging()) {
            setMidPoint(new PointF(mouseX, mouseY));
            setOpen(false);
            children.forEach(Gui::hide);
            resetDropdownsLocation();
            return true;
        }
        return false;
    }
}