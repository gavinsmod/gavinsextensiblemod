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
import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Settings;
import com.peasenet.mods.Type;
import com.peasenet.util.math.PointD;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static com.peasenet.main.GavinsMod.VERSION;

/**
 * @author gt3ch1
 * @version 6/13/2022
 * The main menu for the mod.
 */
public class GuiMainMenu extends Screen {

    /**
     * A list of gui children to render.
     */
    private final ArrayList<Gui> guis;

    /**
     * Creates a new main menu with a list of guis to display.
     *
     * @param guis - The list of guis to display.
     */
    public GuiMainMenu(ArrayList<Gui> guis) {
        super(Text.literal("Gavin's Mod " + VERSION));
        this.guis = guis;
        // Create a plain gui at the top right corner
        var width = getTitle().getString().length() * 5;
        Gui g = new Gui(new PointD(10, 1), width + 2, 10, getTitle());
        guis.add(g);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert this.client != null;
        var tr = this.client.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        for (var gui : guis) {
            if (gui instanceof GuiDropdown) {
                gui.setBackground(Settings.CategoryColor);
            }
            gui.render(matrixStack, tr);
        }

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (var gui : guis) {
            if (gui.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        guis.forEach(gui -> gui.mouseScrolled(mouseX, mouseY, scroll));
        return super.mouseScrolled(mouseX, mouseY, scroll);
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
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        GavinsMod.setEnabled(Type.MOD_GUI, false);
        super.close();
    }

    /**
     * Resets all guis to their default position.
     */
    public void reset() {
        for (var gui : guis) {
            gui.resetPosition();
        }
    }
}
