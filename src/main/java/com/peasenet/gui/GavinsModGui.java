package com.peasenet.gui;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mods;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.option.SpruceBooleanOption;
import dev.lambdaurora.spruceui.option.SpruceOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

import javax.annotation.Nullable;

/**
 * @author gt3ch1
 * @version 5/23/2022
 * A base gui class for managing different mods.
 */
public class GavinsModGui extends SpruceScreen {

    /**
     * The parent of the gui.
     */
    protected final Screen parent;

    private final Mods.Category category;

    /**
     * Creates a new gui with the given parent. Upon exiting, the menu will return to the parent menu.
     * @param screen The parent of this gui.
     */
    public GavinsModGui(@Nullable Screen screen, Mods.Category category) {
        super(new LiteralText("Render"));
        this.parent = screen;
        this.category = category;
    }

    /**
     * Gets a list of options given the category of this gui.
     * @param position The position to render the options.
     * @param width The width to render the options.
     * @param height The height to render the options.
     * @return
     */
    protected SpruceOptionListWidget getOptionList(Position position, int width, int height) {
        var list = new SpruceOptionListWidget(position, width, height);

        list.addAll(GavinsMod.mods.stream()
                .filter(mod -> mod.getCategory() ==  this.category)
                .map(mod -> new SpruceBooleanOption(mod.getTranslationKey(),
                        mod::isActive,
                        newValue -> mod.toggle(),
                        new LiteralText(mod.getName()), true))
                .toArray(SpruceOption[]::new));

        return list;
    }

    @Override
    public void init(){
        super.init();
        this.addDrawableChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 75, this.height - 29), 150, 20, new LiteralText("Back"),
                btn -> this.client.setScreen(this.parent)).asVanilla());

        this.addDrawableChild(getOptionList(Position.of(this.width/2 - 150, 22), this.width/2 + 75, this.height - 75));
    }
}
