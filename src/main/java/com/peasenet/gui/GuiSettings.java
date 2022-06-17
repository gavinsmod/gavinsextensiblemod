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
import com.peasenet.gui.elements.GuiCycle;
import com.peasenet.gui.elements.GuiDropdown;
import com.peasenet.gui.elements.GuiToggle;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Settings;
import com.peasenet.mods.Type;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/17/2022
 * A settings gui to control certain features of the mod.
 */
public class GuiSettings extends Screen {

    /**
     * A list of gui children to render.
     */
    private final ArrayList<Gui> guis;

    private final GuiCycle chestTracer;
    private final GuiCycle chestEsp;
    private final GuiCycle hostileMobTracer;
    private final GuiCycle hostileMobEsp;
    private final GuiCycle peacefulMobTracer;
    private final GuiCycle peacefulMobEsp;
    private final GuiCycle itemTracer;
    private final GuiCycle itemEsp;
    private final GuiCycle playerTracer;
    private final GuiCycle playerEsp;
    private final GuiCycle slowFps;
    private final GuiCycle okFps;
    private final GuiCycle fastFps;
    private final GuiCycle backgroundColor;
    private final GuiCycle foregroundColor;
    private final GuiCycle enabledColor;
    private final GuiToggle fullbrightFade;
    private final GuiToggle fpsColors;

    public GuiSettings() {
        super(Text.translatable("key.gavinsmod.gui.settings"));
        GuiDropdown tracerDropdown = new GuiDropdown(new PointD(110, 110), 110, 10, Text.translatable("key.gavinsmod.settings.tracers"));

        GuiDropdown espDropdown = new GuiDropdown(new PointD(10, 110), 90, 10, Text.translatable("key.gavinsmod.settings.esps"));
        GuiDropdown renderDropdown = new GuiDropdown(new PointD(10, 10), 90, 10, Text.translatable("key.gavinsmod.settings.render"));
        GuiDropdown miscDropdown = new GuiDropdown(new PointD(110, 10), 95, 10, Text.translatable("key.gavinsmod.gui.misc"));
        GuiDropdown fpsColorDropdown = new GuiDropdown(new PointD(172, 21), 95, 10, Text.translatable("key.gavinsmod.settings.fps.color"));

        /**
         * TRACERS AND ESPS
         */
        // Chest tracers
        chestTracer = new GuiCycle(110, 10, Text.translatable("key.gavinsmod.chesttracer"), Colors.COLORS.length);
        chestTracer.setBackground(Settings.ChestTracerColor);
        chestTracer.setCallback(() -> {
            Settings.ChestTracerColor = Colors.COLORS[chestTracer.getCurrentIndex()];
            chestTracer.setBackground(Settings.ChestTracerColor);
        });
        chestTracer.setCurrentIndex(Colors.getColorIndex(Settings.ChestTracerColor));

        // Chest Esps
        chestEsp = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.chestesp"), Colors.COLORS.length);
        chestEsp.setBackground(Settings.ChestEspColor);
        chestEsp.setCallback(() -> {
            Settings.ChestEspColor = Colors.COLORS[chestEsp.getCurrentIndex()];
            chestEsp.setBackground(Settings.ChestEspColor);
        });
        chestEsp.setCurrentIndex(Colors.getColorIndex(Settings.ChestEspColor));

        // Hostile Mob Tracers
        hostileMobTracer = new GuiCycle(110, 10, Text.translatable("key.gavinsmod.hostilemobtracer"), Colors.COLORS.length);
        hostileMobTracer.setBackground(Settings.HostileMobTracerColor);
        hostileMobTracer.setCallback(() -> {
            Settings.HostileMobTracerColor = Colors.COLORS[hostileMobTracer.getCurrentIndex()];
            hostileMobTracer.setBackground(Settings.HostileMobTracerColor);
        });
        hostileMobTracer.setCurrentIndex(Colors.getColorIndex(Settings.HostileMobTracerColor));

        // Hostile Mob Esps
        hostileMobEsp = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.hostilemobesp"), Colors.COLORS.length);
        hostileMobEsp.setBackground(Settings.HostileMobEspColor);
        hostileMobEsp.setCallback(() -> {
            Settings.HostileMobEspColor = Colors.COLORS[hostileMobEsp.getCurrentIndex()];
            hostileMobEsp.setBackground(Settings.HostileMobEspColor);
        });
        hostileMobEsp.setCurrentIndex(Colors.getColorIndex(Settings.HostileMobEspColor));

        // Peaceful Mob Tracers
        peacefulMobTracer = new GuiCycle(110, 10, Text.translatable("key.gavinsmod.peacefulmobtracer"), Colors.COLORS.length);
        peacefulMobTracer.setBackground(Settings.PeacefulMobTracerColor);
        peacefulMobTracer.setCallback(() -> {
            Settings.PeacefulMobTracerColor = Colors.COLORS[peacefulMobTracer.getCurrentIndex()];
            peacefulMobTracer.setBackground(Settings.PeacefulMobTracerColor);
        });
        peacefulMobTracer.setCurrentIndex(Colors.getColorIndex(Settings.PeacefulMobTracerColor));

        // Peaceful Mob Esps
        peacefulMobEsp = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.peacefulmobesp"), Colors.COLORS.length);
        peacefulMobEsp.setBackground(Settings.PeacefulMobEspColor);
        peacefulMobEsp.setCallback(() -> {
            Settings.PeacefulMobEspColor = Colors.COLORS[peacefulMobEsp.getCurrentIndex()];
            peacefulMobEsp.setBackground(Settings.PeacefulMobEspColor);
        });
        peacefulMobEsp.setCurrentIndex(Colors.getColorIndex(Settings.PeacefulMobEspColor));

        // Item Tracers
        itemTracer = new GuiCycle(110, 10, Text.translatable("key.gavinsmod.entityitemtracer"), Colors.COLORS.length);
        itemTracer.setBackground(Settings.ItemTracerColor);
        itemTracer.setCallback(() -> {
            Settings.ItemTracerColor = Colors.COLORS[itemTracer.getCurrentIndex()];
            itemTracer.setBackground(Settings.ItemTracerColor);
        });
        itemTracer.setCurrentIndex(Colors.getColorIndex(Settings.ItemTracerColor));

        // Item Esps
        itemEsp = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.entityitemesp"), Colors.COLORS.length);
        itemEsp.setBackground(Settings.ItemEspColor);
        itemEsp.setCallback(() -> {
            Settings.ItemEspColor = Colors.COLORS[itemEsp.getCurrentIndex()];
            itemEsp.setBackground(Settings.ItemEspColor);
        });
        itemEsp.setCurrentIndex(Colors.getColorIndex(Settings.ItemEspColor));

        // Player Tracers
        playerTracer = new GuiCycle(110, 10, Text.translatable("key.gavinsmod.entityplayertracer"), Colors.COLORS.length);
        playerTracer.setBackground(Settings.PlayerTracerColor);
        playerTracer.setCallback(() -> {
            Settings.PlayerTracerColor = Colors.COLORS[playerTracer.getCurrentIndex()];
            playerTracer.setBackground(Settings.PlayerTracerColor);
        });
        playerTracer.setCurrentIndex(Colors.getColorIndex(Settings.PlayerTracerColor));

        // Player Esps
        playerEsp = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.entityplayeresp"), Colors.COLORS.length);
        playerEsp.setBackground(Settings.PlayerEspColor);
        playerEsp.setCallback(() -> {
            Settings.PlayerEspColor = Colors.COLORS[playerEsp.getCurrentIndex()];
            playerEsp.setBackground(Settings.PlayerEspColor);
        });

        /*
         * RENDER SETTINGS
         */
        fullbrightFade = new GuiToggle(new PointD(10, 10), 90, 10, Text.translatable("key.gavinsmod.settings.gammafade"));
        fullbrightFade.setCallback(() -> Settings.FullbrightFade = !Settings.FullbrightFade);
        fullbrightFade.setState(Settings.FullbrightFade);

        /*
         * Misc settings
         */
        slowFps = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.settings.slowfps"), Colors.COLORS.length);
        slowFps.setBackground(Settings.SlowFpsColor);
        slowFps.setCallback(() -> {
            Settings.SlowFpsColor = Colors.COLORS[slowFps.getCurrentIndex()];
            slowFps.setBackground(Settings.SlowFpsColor);
        });

        okFps = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.settings.okfps"), Colors.COLORS.length);
        okFps.setBackground(Settings.OkFpsColor);
        okFps.setCallback(() -> {
            Settings.OkFpsColor = Colors.COLORS[okFps.getCurrentIndex()];
            okFps.setBackground(Settings.OkFpsColor);
        });

        fastFps = new GuiCycle(90, 10, Text.translatable("key.gavinsmod.settings.fastfps"), Colors.COLORS.length);
        fastFps.setBackground(Settings.FastFpsColor);
        fastFps.setCallback(() -> {
            Settings.FastFpsColor = Colors.COLORS[fastFps.getCurrentIndex()];
            fastFps.setBackground(Settings.FastFpsColor);
        });

        fpsColors = new GuiToggle(new PointD(10, 10), 95, 10, Text.translatable("key.gavinsmod.settings.fpscolors"));
        fpsColors.setCallback(() -> Settings.FpsColors = !Settings.FpsColors);
        fpsColors.setState(Settings.FpsColors);

        backgroundColor = new GuiCycle(95, 10, Text.translatable("key.gavinsmod.settings.backgroundcolor"), Colors.COLORS.length);
        backgroundColor.setBackground(Settings.BackgroundColor);
        backgroundColor.setCallback(() -> {
            Settings.BackgroundColor = Colors.COLORS[backgroundColor.getCurrentIndex()];
            backgroundColor.setBackground(Settings.BackgroundColor);
        });
        backgroundColor.setCurrentIndex(Colors.getColorIndex(Settings.BackgroundColor));

        foregroundColor = new GuiCycle(95, 10, Text.translatable("key.gavinsmod.settings.foregroundcolor"), Colors.COLORS.length);
        foregroundColor.setBackground(Settings.ForegroundColor);
        foregroundColor.setCallback(() -> {
            Settings.ForegroundColor = Colors.COLORS[foregroundColor.getCurrentIndex()];
            foregroundColor.setBackground(Settings.ForegroundColor);
        });
        foregroundColor.setCurrentIndex(Colors.getColorIndex(Settings.ForegroundColor));

        enabledColor = new GuiCycle(95, 10, Text.translatable("key.gavinsmod.settings.enabledcolor"), Colors.COLORS.length);
        enabledColor.setBackground(Settings.EnabledColor);
        enabledColor.setCallback(() -> {
            Settings.EnabledColor = Colors.COLORS[enabledColor.getCurrentIndex()];
            enabledColor.setBackground(Settings.EnabledColor);
        });
        enabledColor.setCurrentIndex(Colors.getColorIndex(Settings.EnabledColor));

        guis = new ArrayList<>();
        guis.add(tracerDropdown);
        guis.add(espDropdown);
        guis.add(renderDropdown);
        guis.add(miscDropdown);

        tracerDropdown.addElement(chestTracer);
        tracerDropdown.addElement(itemTracer);
        tracerDropdown.addElement(hostileMobTracer);
        tracerDropdown.addElement(peacefulMobTracer);
        tracerDropdown.addElement(playerTracer);

        espDropdown.addElement(chestEsp);
        espDropdown.addElement(itemEsp);
        espDropdown.addElement(hostileMobEsp);
        espDropdown.addElement(peacefulMobEsp);
        espDropdown.addElement(playerEsp);

        renderDropdown.addElement(fullbrightFade);
        miscDropdown.addElement(fpsColors);
        fpsColorDropdown.addElement(slowFps);
        fpsColorDropdown.addElement(okFps);
        fpsColorDropdown.addElement(fastFps);
        miscDropdown.addElement(fpsColorDropdown);
        miscDropdown.addElement(backgroundColor);
        miscDropdown.addElement(foregroundColor);
        miscDropdown.addElement(enabledColor);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert this.client != null;
        var tr = this.client.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        tr.draw(matrixStack, Text.translatable("key.gavinsmod.gui.settings"), 10, 0, 0xFFFFFF);
        // Draw a gui button to cycle through colors for tracers
        guis.forEach(gui -> {
            gui.setBackground(Settings.BackgroundColor);
            gui.render(matrixStack, tr);
        });
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Gui g : guis) {
            if (g.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
}
