package com.peasenet.gui.elements;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.Settings;
import com.peasenet.mods.Type;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.BoxD;
import com.peasenet.util.math.PointD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiModScroll extends GuiModCategory {

    private final int maxButtons;
    private final int numPages;
    private int scrollIndex;

    public GuiModScroll(PointD position, int width, int height, Text title, Type.Category category) {
        this(position, width, height, title, category, 4);
    }

    /**
     * Creates a new dropdown like UI element.
     *
     * @param position - The position of the dropdown.
     * @param width    - The width of the dropdown.
     * @param height   - The height of the dropdown.
     * @param title    - The title of the dropdown.
     */
    public GuiModScroll(PointD position, int width, int height, Text title, Type.Category category, int maxButtons) {
        super(position, width, height, title, category);
        this.maxButtons = Math.min(buttons.size(), maxButtons);
        this.numPages = buttons.size() - maxButtons;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        if (!super.mouseWithinGui(x, y)) return false;
        if (scroll > 0) {
            scrollUp();
        } else {
            scrollDown();
        }
        return true;
    }

    @Override
    public void render(MatrixStack matrices, TextRenderer tr) {
        // For each mod in the category, render it right below the title.
        RenderUtils.drawBox(getBackgroundColor().getAsFloatArray(), (int) getX(), (int) getY(), (int) getX2(), (int) getY2() + 1, matrices);
        tr.draw(matrices, this.title, (int) getX() + 2, (int) getY() + 2, Settings.ForegroundColor.getAsInt());
        RenderUtils.drawOutline(Colors.WHITE.getAsFloatArray(), (int) getX(), (int) getY(), (int) getX2(), (int) getY2() + 1, matrices);
        if (!isOpen())
            return;
        // render mods between scroll index and max  buttons
        var mods = GavinsMod.getModsInCategory(category);
        buttons.forEach(Gui::hide);
        for (int i = scrollIndex; i < scrollIndex + maxButtons; i++) {
            if (i >= buttons.size())
                break;
            buttons.get(i).render(matrices, tr);
            var mod = mods.get(i);
            var gui = buttons.get(i);
            gui.show();
            // update location of gui
            gui.setPosition(new PointD(getX(), (gui.getHeight() + 2) * (i - scrollIndex) + (getY() + getHeight()) + 2));
            if (shouldDrawScrollBar())
                gui.setWidth(getWidth() - 5.5);
            if (gui instanceof GuiToggle)
                ((GuiToggle) gui).setState(mod.isActive());
            gui.setBackground(mod.isActive() ? Settings.EnabledColor : Settings.BackgroundColor);
        }
        if (shouldDrawScrollBar()) {
            drawScrollBox(matrices);
            drawScrollBar(matrices);
        }
    }


    private boolean shouldDrawScrollBar() {
        return buttons.size() > maxButtons;
    }

    private void drawScrollBox(MatrixStack matrices) {
        var scrollBoxX = getX() + getWidth() - 4;
        var scrollBoxY = (getY2()) + 2;
        var scrollBoxHeight = getScrollBoxHeight();
        RenderUtils.drawBox(Colors.BLACK.getAsFloatArray(), new BoxD(new PointD(scrollBoxX, scrollBoxY), 4, scrollBoxHeight), matrices);
        RenderUtils.drawOutline(Settings.ForegroundColor.getAsFloatArray(), new BoxD(new PointD(scrollBoxX, scrollBoxY), 4, scrollBoxHeight), matrices);
    }

    private void drawScrollBar(MatrixStack matrices) {
        var scrollBoxHeight = getScrollBoxHeight();
        // set scrollbarY to 1/maxButtons of the scrollBoxHeight
        var scrollBarY = (int) (scrollBoxHeight * (scrollIndex / (double) numPages)) + getY2() + 3;
        var scrollBarX = getX() + getWidth() - 2;
        int scrollBarY2 = (int) ((scrollBarY) + (scrollBoxHeight / numPages));
        RenderUtils.drawBox(Colors.WHITE.getAsFloatArray(), (int) scrollBarX - 1, (int) scrollBarY, (int) scrollBarX + 1, scrollBarY2 - 2, matrices);
    }

    private double getScrollBoxHeight() {
        return (maxButtons) * getHeight() + maxButtons + (maxButtons - 1);
    }

    protected void scrollUp() {
        if (scrollIndex > 0) {
            scrollIndex--;
        }
    }

    protected void scrollDown() {
        if (scrollIndex < buttons.size() - 1 - maxButtons) {
            scrollIndex++;
        }
    }
}
