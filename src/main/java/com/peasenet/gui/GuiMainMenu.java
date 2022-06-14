package com.peasenet.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.gui.elements.Gui;
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
            if (gui.isDragging()) {
                gui.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
                return true;
            }
            if (!gui.isDragging() && mouseWithinGui(mouseX, mouseY, gui)) {
                gui.setDragging(true);
            }
        }
        return false;
    }

    private boolean mouseWithinGui(double mouseX, double mouseY, Gui gui) {
        return mouseX >= gui.getX() && mouseX <= gui.getX2() && mouseY >= gui.getY() && mouseY <= gui.getY2();
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
