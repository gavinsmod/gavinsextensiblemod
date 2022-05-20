package com.peasenet.mixins;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author gt3ch1
 * @version 5/19/2022
 */
@Mixin(Screen.class)
public interface ScreenAccessor {
    @Invoker("addDrawableChild")
    <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);
}
