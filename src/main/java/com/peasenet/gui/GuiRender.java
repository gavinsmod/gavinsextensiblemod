package com.peasenet.gui;

import com.peasenet.mods.Mods;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
public class GuiRender extends GavinsModGui {

    public GuiRender(@Nullable Screen screen) {
        super(screen);
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, new LiteralText("Back"),
                btn -> this.client.setScreen(this.parent)).asVanilla());

        this.addDrawableChild(getOptionList(Position.of(0, 22), this.width, this.height - 35 - 22, Mods.Category.RENDER));
    }



}
