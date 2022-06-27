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
import com.peasenet.gui.elements.GuiScroll;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.Setting;
import com.peasenet.util.math.PointD;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A settings gui to control certain features of the mod.
 */
public class GuiSettings extends Screen {

    /**
     * A list of gui children to render.
     */
    private final ArrayList<Gui> guis;

    /**
     * The tracer dropdown
     */
    GuiScroll tracerDropdown;

    /**
     * The esp dropdown
     */
    GuiScroll espDropdown;

    /**
     * The render dropdown
     */
    GuiScroll renderDropdown;

    /**
     * The miscellaneous dropdown
     */
    GuiScroll miscDropdown;

    /**
     * The dropdown containing gui settings.
     */
    GuiScroll guiDropdown;

    /**
     * Creates a new GUI settings screen.
     */
    public GuiSettings() {
        super(Text.translatable("key.gavinsmod.gui.settings"));
        tracerDropdown = new GuiScroll(new PointD(120, 110), 115, 10, Text.translatable("key.gavinsmod.settings.tracers"));
        espDropdown = new GuiScroll(new PointD(10, 110), 100, 10, Text.translatable("key.gavinsmod.settings.esps"));
        renderDropdown = new GuiScroll(new PointD(10, 10), 90, 10, Text.translatable("key.gavinsmod.settings.render"));
        miscDropdown = new GuiScroll(new PointD(110, 10), 105, 10, Text.translatable("key.gavinsmod.gui.misc"));
        guiDropdown = new GuiScroll(new PointD(245, 110), 100, 10, Text.translatable("key.gavinsmod.gui"));
        addSettings(tracerDropdown, Type.Category.TRACERS);
        addSettings(espDropdown, Type.Category.ESPS);
        addSettings(renderDropdown, Type.Category.RENDER);
        addSettings(miscDropdown, Type.Category.MISC);
        addSettings(guiDropdown, Type.Category.GUI);

        guis = new ArrayList<>();
        guis.add(tracerDropdown);
        guis.add(espDropdown);
        guis.add(renderDropdown);
        guis.add(miscDropdown);
        guis.add(guiDropdown);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert this.client != null;
        var tr = this.client.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        tr.draw(matrixStack, Text.translatable("key.gavinsmod.gui.settings"), 10, 1, (Settings.getColor("foregroundColor")).getAsInt());
        guis.forEach(gui -> {
            gui.setBackground(Settings.getColor("categoryColor"));
            gui.render(matrixStack, tr);
        });
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Gui g : guis)
            if (g.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        for (Gui g : guis)
            if (g.mouseScrolled(x, y, scroll)) return true;
        return super.mouseScrolled(x, y, scroll);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        GavinsMod.setEnabled(Type.SETTINGS, false);
        super.close();
    }

    /**
     * Creates the esp settings scroll gui.
     *
     * @param parent   - The parent gui.
     * @param category - The category of mod types to add to the parent gui.
     */
    private void addSettings(Gui parent, Type.Category category) {
        var modList = new ArrayList<Mod>();
        // get all mods in esp category and have settings then add them to espDropdown
        Mods.getMods().stream().filter(m -> m.getCategory() == category && m.hasSettings()).forEach(modList::add);
        for (Mod m : modList) {
            var modSettings = m.getSettings();
            for (Setting s : modSettings) {
                s.getGui().setWidth(parent.getWidth());
                parent.addElement(s.getGui());
            }
        }
    }
}
