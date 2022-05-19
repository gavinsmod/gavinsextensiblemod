package com.peasenet.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author gt3ch1
 * @version 5/18/2022
 */
@Mixin(Entity.class)
public interface EntityAccessor {

    @Accessor("onGround")
    public boolean getOnGround();

    @Accessor("onGround")
    public void setOnGround(boolean onGround);
}
