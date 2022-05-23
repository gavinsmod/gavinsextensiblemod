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
        super(screen, Mods.Category.RENDER);
    }
}
