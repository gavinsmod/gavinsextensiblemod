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
        int startY = this.height / 4 + 48;
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, new LiteralText("Back"),
                btn -> this.client.setScreen(this.parent)));
    }
}
