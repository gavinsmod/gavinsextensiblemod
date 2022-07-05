/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.gui.elements.Gui;
import com.peasenet.main.Settings;
import com.peasenet.util.math.PointD;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A parent class that holds all that is needed to render an in game gui.
 */
public class GuiElement extends Screen {

    /**
     * The screen to go back to when this screen is closed.
     */
    protected Screen parent;

    /**
     * The box that contains the menu title in the top left corner of the screen.
     */
    public final Gui titleBox;
    /**
     * A list of gui children to render.
     */
    public ArrayList<Gui> guis = new ArrayList<>();

    /**
     * Creates a new GUI menu with the given title.
     *
     * @param title - The title.
     */
    public GuiElement(Text title) {
        super(title);
        titleBox = new Gui(new PointD(10, 1), title.getContent().toString().length() * 2, 10, title);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        guis.forEach(gui -> gui.mouseScrolled(mouseX, mouseY, scroll));
        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Gui g : guis)
            if (g.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (var gui : guis) {
            if (gui.isDragging()) {
                gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
                return true;
            }
            if (!gui.isDragging() && gui.mouseWithinGui(mouseX, mouseY)) {
                gui.setDragging(true);
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        guis.forEach(g -> g.setDragging(false));
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert client != null;
        var tr = client.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        guis.forEach(gui -> {
            gui.setBackground(Settings.getColor("gui.color.category"));
            gui.render(matrixStack, tr, mouseX, mouseY, delta);
        });
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    /**
     * Resets all child guis to their default positions.
     */
    public void reset() {
        guis.forEach(Gui::resetPosition);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
