package com.peasenet.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
public class GuiMainMenu extends Screen {
    static final int START_X1 = 10;
    static final int START_Y1 = 10;
    static final int START_X2 = 95;
    static final int START_Y2 = 21;
    private final GuiClick gui;

    public GuiMainMenu(GuiClick gui) {
        super(new LiteralText("Gavin's Mod"));
        this.gui = gui;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        assert this.client != null;
        var tr = this.client.textRenderer;
        gui.render(matrixStack, tr, this.title, START_X1, START_Y1, START_X2, START_Y2);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        assert gui != null;
        gui.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
