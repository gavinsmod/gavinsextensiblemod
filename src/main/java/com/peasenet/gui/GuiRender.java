package com.peasenet.gui;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.ModCategory;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
public class GuiRender extends SpruceScreen {
    private final Screen parent;
    private boolean xray;

    public GuiRender(@Nullable Screen screen) {
        super(new LiteralText("Render"));
        this.parent = screen;
    }

    @Override
    protected void init() {
        super.init();
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, new LiteralText("Back"),
                btn -> this.client.setScreen(this.parent)).asVanilla());

        this.addDrawableChild(getOptionList(Position.of(0, 22), this.width, this.height - 35 - 22));
    }

    private SpruceOptionListWidget getOptionList(Position position, int width, int height) {
        var list = new SpruceOptionListWidget(position, width, height);

        list.addAll(GavinsMod.mods.stream()
            .filter(mod -> mod.getCategory() == ModCategory.RENDER)
            .map(mod -> new SpruceBooleanOption(mod.getTranslationKey(),
                mod::isActive,
                newValue -> mod.toggle(),
                new LiteralText(mod.getName()), true))
            .toArray(SpruceOption[]::new));

        return list;
    }

}
