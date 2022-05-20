package com.peasenet.gui;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
public class MainScreen extends SpruceScreen {
    private final Screen parent;

    public MainScreen(@Nullable Screen parent) {
        super(new LiteralText("SpruceUI Test Main Menu"));
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        int startY = 20;
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, new LiteralText("Back"),
                btn -> this.client.setScreen(this.parent)));
        // Movement button
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 125, startY), 100, 20, new LiteralText("§aMovement"),
                btn -> this.client.setScreen(new GuiMovement(this))));
        // Render button
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 + 25, startY), 100, 20, new LiteralText("§aRender"),
                btn -> this.client.setScreen(new GuiRender(this))));
    }
}
