/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
import com.peasenet.gavui.Gui;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.main.GavinsMod;
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
     * The box that contains the menu title in the top left corner of the screen.
     */
    public Gui titleBox;

    private Gui selectedGui;

    /**
     * A list of gui children to render.
     */
    public ArrayList<Gui> guis = new ArrayList<>();
    /**
     * The screen to go back to when this screen is closed.
     */
    protected Screen parent;

    /**
     * Creates a new GUI menu with the given title.
     *
     * @param title - The title.
     */
    public GuiElement(Text title) {
        super(title);
    }

    @Override
    public void init() {
        super.init();
        titleBox = new Gui(new PointF(10, 1), textRenderer.getWidth(title) + 4, 10, title);
        var clientWidth = client.getWindow().getScaledWidth();
        var clientHeight = client.getWindow().getScaledHeight();
        //TODO: Maybe make this a background?
        Gui overlay = new Gui(new PointF(0, 0), clientWidth + 1, clientHeight, Text.of(""));
        overlay.setBackground(Colors.BLACK);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        guis.forEach(gui -> gui.mouseScrolled(mouseX, mouseY, scroll));
        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Gui g : guis)
            if (g.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (var gui : guis) {
            if (gui.isDragging() && gui.equals(selectedGui)) {
                gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
                GavinsMod.LOGGER.info("Dragging " + gui.getUUID());
                return true;
            }
            if (!gui.isDragging() && gui.mouseWithinGui(mouseX, mouseY)) {
                GavinsMod.LOGGER.info("Dragging " + gui.getUUID());
                gui.setDragging(true);
                selectedGui = gui;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        guis.forEach(g -> g.setDragging(false));
        selectedGui = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert client != null;
        var tr = client.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.enableBlend();
//        overlay.render(matrixStack, tr, mouseX, mouseY, delta);
        guis.forEach(gui -> {
            if (gui.isParent())
                gui.setBackground(GavUISettings.getColor("gui.color.category"));
            gui.render(matrixStack, tr, mouseX, mouseY, delta);
        });
        if (titleBox != null) {
            titleBox.setBackground(GavUISettings.getColor("gui.color.background"));
            titleBox.render(matrixStack, tr, mouseX, mouseY, delta);
        }
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
