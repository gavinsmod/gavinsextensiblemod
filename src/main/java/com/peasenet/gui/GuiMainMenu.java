package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Type;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;

import static com.peasenet.main.GavinsMod.VERSION;

/**
 * @author gt3ch1
 * @version 6/13/2022
 */
public class GuiMainMenu extends Screen {

    // A list of guis to display
    private final ArrayList<Gui> guis;

    /**
     * Creates a new main menu with a list of guis to display.
     *
     * @param guis - The list of guis to display.
     */
    public GuiMainMenu(ArrayList<Gui> guis) {
        super(Text.literal("Gavin's Mod " + VERSION));
        this.guis = guis;
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
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (var gui : guis) {
            // Check if the mouse is over the gui by a padding of 10
            if (gui.isDragging())
                return gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            if (mouseX >= gui.getX() - 10 && mouseX <= gui.getX2() + 10 && mouseY >= gui.getY() - 10 && mouseY <= gui.getY2() + 10) {
                if (gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    gui.setDragging(true);
                    return true;
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (var gui : guis) {
            gui.setDragging(false);
        }
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
}
