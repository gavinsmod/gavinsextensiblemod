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
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * @author gt3ch1
 * @version 7/1/2022
 * A gui that allows the user to add, delete, or modify a waypoint.
 */
public class GuiWaypoint extends GuiElement {

    /**
     * The text field used to name the waypoint.
     */
    TextFieldWidget textField;

    /**
     * The x coordinate text field.
     */
    TextFieldWidget xCoordinate;

    /**
     * The y coordinate text field.
     */
    TextFieldWidget yCoordinate;

    /**
     * The z coordinate text field.
     */
    TextFieldWidget zCoordinate;

    /**
     * The button used to save the waypoint.
     */
    ClickSetting saveSettings = new ClickSetting("none", "gavinsmod.settings.save");

    /**
     * The button used to cancel the waypoint.
     */
    ClickSetting cancelSettings = new ClickSetting("none", "gavinsmod.settings.cancel");

    /**
     * The button used to delete the waypoint.
     */
    ClickSetting deleteSettings = new ClickSetting("none", "gavinsmod.settings.delete");

    /**
     * The button used to toggle the waypoint's visibility.
     */
    ToggleSetting waypointToggle = new ToggleSetting("none", "gavinsmod.settings.enabled");

    /**
     * The button used to toggle the waypoint's esp.
     */
    ToggleSetting espToggle = new ToggleSetting("none", "gavinsmod.settings.esp");

    /**
     * The button used to toggle the waypoint's tracer.
     */
    ToggleSetting tracerToggle = new ToggleSetting("none", "gavinsmod.settings.tracer");

    /**
     * The background box.
     */
    Gui box;

    /**
     * The button that is used to change the waypoints color.
     */
    ColorSetting colorCycle = new ColorSetting("none", "gavinsmod.settings.render.waypoints.color");

    /**
     * The width of the gui.
     */
    int width = 145;

    /**
     * The height of the gui.
     */
    int height = 100;

    /**
     * The padding of each element.
     */
    int padding = 5;


    /**
     * The offset and padding in the x and y planes.
     */
    int offsetX, paddingX, offsetY, paddingY;

    /**
     * The waypoint that is being edited.
     */
    private Waypoint w;

    /**
     * Creates a gui that allows the user to add a new waypoint.
     */
    public GuiWaypoint() {
        super(Text.translatable("gavinsmod.mod.render.waypoints"));
        parent = GavinsMod.guiSettings;
        w = null;
        saveSettings.setCallback(() -> {
            Vec3i flooredPos = getFlooredPlayerPos();
            w = new Waypoint(flooredPos);
            w.setName(textField.getText());
            w.setColor(colorCycle.getColor());
            w.setEnabled(waypointToggle.getValue());
            w.setEspEnabled(espToggle.getValue());
            w.setTracerEnabled(tracerToggle.getValue());
            w.setX(Integer.parseInt(xCoordinate.getText()));
            w.setY(Integer.parseInt(yCoordinate.getText()));
            w.setZ(Integer.parseInt(zCoordinate.getText()));
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

    /**
     * Creates a gui that allows the user to edit or delete an existing waypoint.
     *
     * @param w - The waypoint to edit.
     */
    public GuiWaypoint(Waypoint w) {
        super(Text.translatable("gavinsmod.mod.render.waypoints"));
        this.w = w;
        waypointToggle.setValue(w.isEnabled());
        colorCycle.setColor(w.getColor());
        saveSettings.setCallback(() -> {
            w.setName(textField.getText());
            w.setColor(colorCycle.getColor());
            w.setEnabled(waypointToggle.getValue());
            w.setEspEnabled(espToggle.getValue());
            w.setTracerEnabled(tracerToggle.getValue());
            w.setX(Integer.parseInt(xCoordinate.getText()));
            w.setY(Integer.parseInt(yCoordinate.getText()));
            w.setZ(Integer.parseInt(zCoordinate.getText()));
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

    @NotNull
    private Vec3i getFlooredPlayerPos() {
        var playerPos = GavinsModClient.getPlayer().getPos();
        return new Vec3i(Math.floor(playerPos.x), Math.floor(playerPos.y) + 1, Math.floor(playerPos.z));
    }

    @Override
    public void init() {
        parent = GavinsMod.guiSettings;
        offsetX = (GavinsModClient.getMinecraftClient().getWindow().getScaledWidth() / 2) - width / 2;
        offsetY = (GavinsModClient.getMinecraftClient().getWindow().getScaledHeight() / 2) - height / 2;
        paddingX = offsetX + padding;
        paddingY = offsetY + padding;
        box = new Gui(new PointD(offsetX, offsetY), width, height, Text.literal(""));
        textField = new TextFieldWidget(GavinsModClient.getMinecraftClient().getTextRenderer(), offsetX + 40, offsetY + 10, 100, 10, Text.literal(""));
        xCoordinate = new TextFieldWidget(GavinsModClient.getMinecraftClient().getTextRenderer(), paddingX + 11, offsetY + 80 + padding, 30, 10, Text.literal(""));
        yCoordinate = new TextFieldWidget(GavinsModClient.getMinecraftClient().getTextRenderer(), paddingX + 56, offsetY + 80 + padding, 30, 10, Text.literal(""));
        zCoordinate = new TextFieldWidget(GavinsModClient.getMinecraftClient().getTextRenderer(), paddingX + 101, offsetY + 80 + padding, 30, 10, Text.literal(""));
        // make sure xCoordinate only contains numbers
        if (w != null) {
            textField.setText(w.getName());
            xCoordinate.setText(String.valueOf(w.getX()));
            yCoordinate.setText(String.valueOf(w.getY()));
            zCoordinate.setText(String.valueOf(w.getZ()));
        } else {
            var playerPos = getFlooredPlayerPos();
            xCoordinate.setText(String.valueOf(playerPos.getX()));
            yCoordinate.setText(String.valueOf(playerPos.getY()));
            zCoordinate.setText(String.valueOf(playerPos.getZ()));
        }
        xCoordinate.setTextPredicate(checkIfSignedInt());
        yCoordinate.setTextPredicate(checkIfSignedInt());
        zCoordinate.setTextPredicate(checkIfSignedInt());
        addSelectableChild(textField);
        addSelectableChild(xCoordinate);
        addSelectableChild(yCoordinate);
        addSelectableChild(zCoordinate);
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
        super.init();
    }

    @NotNull
    private Predicate<String> checkIfSignedInt() {
        return s -> {
            if (s.isEmpty()) return true;
            if (s.equals("-") || s.equals(".")) return true;
            // match any signed integer within Integer.MIN_VALUE and Integer.MAX_VALUE
            var regex = "^-?[0-9]+$";
            if (s.matches(regex)) {
                try {
                    var i = Integer.parseInt(s);
                    return i >= Integer.MIN_VALUE && i <= Integer.MAX_VALUE;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        };
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        box.render(matrixStack, client.textRenderer, mouseX, mouseY, delta);

        client.textRenderer.draw(matrixStack, Text.literal("Name: "), paddingX, offsetY + 11, Settings.getColor("gui.color.foregorund").getAsInt());
        client.textRenderer.draw(matrixStack, Text.literal("X:"), paddingX + 1, offsetY + 82 + padding, Settings.getColor("gui.color.foregorund").getAsInt());
        client.textRenderer.draw(matrixStack, Text.literal("Y:"), paddingX + 46, offsetY + 82 + padding, Settings.getColor("gui.color.foregorund").getAsInt());
        client.textRenderer.draw(matrixStack, Text.literal("Z:"), paddingX + 91, offsetY + 82 + padding, Settings.getColor("gui.color.foregorund").getAsInt());
        textField.render(matrixStack, mouseX, mouseY, delta);
        xCoordinate.render(matrixStack, mouseX, mouseY, delta);
        yCoordinate.render(matrixStack, mouseX, mouseY, delta);
        zCoordinate.render(matrixStack, mouseX, mouseY, delta);
        super.render(matrixStack, mouseX, mouseY, delta);
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
}
