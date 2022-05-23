package com.peasenet.gui;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mods;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import javax.annotation.Nullable;

public class GavinsModGui extends SpruceScreen {
    protected final Screen parent;

    public GavinsModGui(@Nullable Screen screen) {
        super(new LiteralText("Render"));
        this.parent = screen;
    }


    protected SpruceOptionListWidget getOptionList(Position position, int width, int height, Mods.Category category) {
        var list = new SpruceOptionListWidget(position, width, height);

        list.addAll(GavinsMod.mods.stream()
                .filter(mod -> mod.getCategory() == category)
                .map(mod -> new SpruceBooleanOption(mod.getTranslationKey(),
                        mod::isActive,
                        newValue -> mod.toggle(),
                        new LiteralText(mod.getName()), true))
                .toArray(SpruceOption[]::new));

        return list;
    }
}
