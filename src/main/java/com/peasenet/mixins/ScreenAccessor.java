package com.peasenet.mixins;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * An accessor to the Screen class.
 * @author GT3CH1
 * @version 12-04-2025
 * @since 12-04-2025
 */

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Mutable
    @Accessor("font")
    void setFont(Font font);
}
