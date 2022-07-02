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

package com.peasenet.gui.mod.waypoint;

import com.peasenet.gui.GuiElement;
import com.peasenet.gui.elements.Gui;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.color.Colors;
import com.peasenet.util.math.PointD;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * @version 7/1/2022
 */
public class GuiWaypoint extends GuiElement {

    Screen parent;
    TextFieldWidget textField;
    ClickSetting saveSettings = new ClickSetting("none", "gavinsmod.settings.save");
    ClickSetting cancelSettings = new ClickSetting("none", "gavinsmod.settings.cancel");
    ClickSetting deleteSettings = new ClickSetting("none", "gavinsmod.settings.delete");
    ToggleSetting waypointToggle = new ToggleSetting("none", "gavinsmod.settings.enabled");
    ToggleSetting espToggle = new ToggleSetting("none", "gavinsmod.settings.esp");
    ToggleSetting tracerToggle = new ToggleSetting("none", "gavinsmod.settings.tracer");
    Gui box;
    ColorSetting colorCycle = new ColorSetting("waypoint.color", "gavinsmod.settings.render.waypoints.color");
    int width = 145;
    int height = 100;
    int padding = 5;
    int offsetX, paddingX, offsetY, paddingY;
    private Waypoint w;

    public GuiWaypoint() {
        super(Text.literal("Waypoint"));
        parent = GavinsMod.guiSettings;
        w = null;
        saveSettings.setCallback(() -> {
            var playerPos = GavinsModClient.getPlayer().getPos();
            w = new Waypoint(playerPos.add(0, 1, 0));
            w.setName(textField.getText());
            w.setColor(Colors.getColorIndex(colorCycle.getColor()));
            w.setEnabled(waypointToggle.getValue());
            w.setEspEnabled(espToggle.getValue());
            w.setTracerEnabled(tracerToggle.getValue());
            Settings.addWaypoint(w);
            Mods.getMod("waypoints").reloadSettings();
            GavinsMod.guiSettings.reloadGui();
            parent = GavinsMod.guiSettings;
            close();
        });
        cancelSettings.setCallback(() -> client.setScreen(parent));
        guis.add(colorCycle.getGui());
        colorCycle.setColorIndex(Colors.getRandomColor());
        waypointToggle.setValue(true);
        espToggle.setValue(true);
        tracerToggle.setValue(true);
        guis.add(saveSettings.getGui());
        guis.add(cancelSettings.getGui());
        guis.add(deleteSettings.getGui());
        guis.add(waypointToggle.getGui());
        guis.add(espToggle.getGui());
        guis.add(tracerToggle.getGui());
    }

    public GuiWaypoint(Waypoint w) {
        super(Text.literal("Waypoint"));
        this.w = w;
        waypointToggle.setValue(w.isEnabled());
        colorCycle.setColorIndex(w.getColorIndex());
        saveSettings.setCallback(() -> {
            w.setName(textField.getText());
            w.setColor(Colors.getColorIndex(colorCycle.getColor()));
            w.setEnabled(waypointToggle.getValue());
            w.setEspEnabled(espToggle.getValue());
            w.setTracerEnabled(tracerToggle.getValue());
            Settings.addWaypoint(w);
            Mods.getMod("waypoints").reloadSettings();
            GavinsMod.guiSettings.reloadGui();
            parent = GavinsMod.guiSettings;
            close();
        });
        cancelSettings.setCallback(() -> client.setScreen(parent));
        deleteSettings.setCallback(() -> {
            Settings.deleteWaypoint(w);
            Mods.getMod("waypoints").reloadSettings();
            GavinsMod.guiSettings.reloadGui();
            close();
        });
        espToggle.setValue(w.isEspEnabled());
        tracerToggle.setValue(w.isTracerEnabled());
        guis.add(colorCycle.getGui());
        guis.add(saveSettings.getGui());
        guis.add(cancelSettings.getGui());
        guis.add(deleteSettings.getGui());
        guis.add(waypointToggle.getGui());
        guis.add(espToggle.getGui());
        guis.add(tracerToggle.getGui());
    }

    @Override
    public void init() {
        parent = GavinsMod.guiSettings;
        offsetX = (GavinsModClient.getMinecraftClient().getWindow().getScaledWidth() / 2) - width / 2;
        offsetY = (GavinsModClient.getMinecraftClient().getWindow().getScaledHeight() / 2) - height / 2;
        paddingX = offsetX + padding;
        paddingY = offsetY + padding;
        box = new Gui(new PointD(offsetX, offsetY), width, height, Text.literal(""));
        textField = new TextFieldWidget(GavinsModClient.getMinecraftClient().getTextRenderer(), offsetX + 40, offsetY + 10, 100, 10, Text.literal("")) {
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (button == 1 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                    setText("");
                    return true;
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public void setText(String text) {
                super.setText(text);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                //                    updateSearch();
                return super.keyPressed(keyCode, scanCode, modifiers);
            }

            @Override
            public boolean charTyped(char chr, int modifiers) {
                //                    updateSearch();
                return super.charTyped(chr, modifiers);
            }
        };
        if (w != null) textField.setText(w.getName());
        addSelectableChild(textField);
        setFocused(textField);
        var buttonWidth = 42;
        var wholeButtonWidth = (buttonWidth * 3) + (padding * 2);
        var threeButtonY = offsetY + 34 + padding;
        colorCycle.setWidth(wholeButtonWidth);
        colorCycle.getGui().setPosition(new PointD(paddingX, offsetY + 20 + padding));

        saveSettings.getGui().setPosition(new PointD(paddingX, threeButtonY));
        saveSettings.getGui().setWidth(buttonWidth);
        saveSettings.getGui().setBackground(Colors.GREEN);
        cancelSettings.getGui().setPosition(new PointD(paddingX + padding + buttonWidth, threeButtonY));
        cancelSettings.getGui().setWidth(buttonWidth);
        cancelSettings.getGui().setBackground(Colors.YELLOW);
        deleteSettings.getGui().setPosition(new PointD(paddingX + padding * 2 + (buttonWidth * 2), threeButtonY));
        deleteSettings.getGui().setWidth(buttonWidth);
        deleteSettings.getGui().setBackground(Colors.RED);
        waypointToggle.getGui().setPosition(new PointD(paddingX, offsetY + padding + 48));
        waypointToggle.getGui().setWidth(wholeButtonWidth);
        espToggle.getGui().setPosition(new PointD(paddingX, offsetY + 62 + padding));
        espToggle.getGui().setWidth(wholeButtonWidth / 2 - padding / 2);
        tracerToggle.getGui().setPosition(new PointD(offsetX + padding + padding / 2 + wholeButtonWidth / 2, offsetY + 62 + padding));
        tracerToggle.getGui().setWidth(wholeButtonWidth / 2 - padding / 2);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        box.render(matrixStack, client.textRenderer, mouseX, mouseY, delta);
        for (Gui g : guis) {
            g.show();
            g.render(matrixStack, client.textRenderer, mouseX, mouseY, delta);
        }
        var coord = GavinsModClient.getMinecraftClient().getPlayer().getPos();
        if (w != null)
            coord = w.getVec();
        client.textRenderer.draw(matrixStack, Text.literal("Name: "), paddingX, offsetY + 11, Settings.getColor("gui.color.foregorund").getAsInt());
        client.textRenderer.draw(matrixStack, Text.literal("X: %d, Y: %d, Z: %d".formatted((int) coord.getX(), (int) coord.getY(), (int) coord.getZ())), paddingX + 20, offsetY + 82 + padding, Settings.getColor("gui.color.foregorund").getAsInt());
        textField.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (var child : guis) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                textField.setTextFieldFocused(false);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
