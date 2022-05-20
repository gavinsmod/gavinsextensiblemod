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
        ArrayList<SpruceOption> options = new ArrayList<>();
        // Get all mods that are under "Render"
        for (var mod : GavinsMod.mods) {
            if (mod.getCategory() == ModCategory.RENDER) {
                var option = new SpruceBooleanOption(mod.getTranslationKey(),
                        mod::isActive,
                        newValue -> mod.toggle(),
                        new LiteralText(mod.getName()), true);
                options.add(option);
            }
        }
        // Check if options is even, if true, then addOptionEntry can be populated
        // with two options, if not, the addOptionEntry can be populated with one, and null.
        if (options.size() % 2 == 0) {
            for (int i = 0; i < options.size(); i += 2) {
                list.addOptionEntry(options.get(i), options.get(i + 1));
            }
        } else {
            for (int i = 0; i < options.size() - 1; i++) {
                list.addOptionEntry(options.get(i), options.get(i + 1));
            }
            list.addOptionEntry(options.get(options.size() - 1), null);
        }

        return list;
    }

}
